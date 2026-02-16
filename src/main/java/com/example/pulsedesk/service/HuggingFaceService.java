package com.example.pulsedesk.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.pulsedesk.DataInitializer;
import com.example.pulsedesk.dto.AiTriageResponse;

import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Service
public class HuggingFaceService {
	
	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	
	@Value("${huggingface.api.url}")
    private String apiUrl;

    @Value("${huggingface.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper mapper;
    
    private final List<String> modelCandidates = List.of(
        "Qwen/Qwen2.5-7B-Instruct:together",
        "Qwen/Qwen2.5-7B-Instruct:featherless-ai",
        "mistralai/Mistral-7B-Instruct-v0.2:featherless-ai",
        "mistralai/Mistral-7B-Instruct-v0.2:together",
        "google/gemma-2-9b-it:featherless-ai"
    );
    
    public HuggingFaceService() {
        this.mapper = JsonMapper.builder()
        	    .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
        	    .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES) // Helps with 'False' vs 'false'
        	    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        	    .build();
    }
    
    public AiTriageResponse analyzeComment(String userComment) {
        String prompt = buildPrompt(userComment);

        for (String modelId : modelCandidates) {
            try {
                logger.info("Using AI model: {}", modelId);
                return executeRequest(prompt, modelId);
            } catch (Exception e) {
                logger.warn("Model {} failed: {}. Trying next...", modelId, e.getMessage());
            }
        }

        throw new RuntimeException("CRITICAL: All AI models failed to provide a valid response.");
    }
    
    private AiTriageResponse executeRequest(String prompt, String modelId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        Map<String, Object> requestBody = Map.of(
            "messages", List.of(Map.of("role", "user", "content", prompt)),
            "model", modelId,
            "max_tokens", 150,
            "temperature", 0.1
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String rawResponse = restTemplate.postForObject(apiUrl, request, String.class);

        JsonNode root = mapper.readTree(rawResponse);
        String content = root.path("choices").get(0).path("message").path("content").asText();

        String cleanJson = sanitizeContent(content);
        
        return mapper.readValue(cleanJson, AiTriageResponse.class);
    }
    
    private String sanitizeContent(String content) {
        String cleaned = content.trim();
        
        // Remove Markdown code blocks if present
        cleaned = cleaned.replaceAll("(?s)```(?:json)?\\n?(.*?)\\n?```", "$1").trim();

        // Ensure root braces exist (Fixes Gemma-style output)
        if (!cleaned.startsWith("{")) {
            cleaned = "{" + cleaned;
        }
        if (!cleaned.endsWith("}")) {
            cleaned = cleaned + "}";
        }

        // Find the actual JSON boundaries in case of extra text
        int start = cleaned.indexOf("{");
        int end = cleaned.lastIndexOf("}");
        
        if (start == -1 || end == -1) {
            throw new RuntimeException("No JSON object found in content: " + content);
        }

        return cleaned.substring(start, end + 1);
    }
    	    
	private String buildPrompt(String commentContent) {
        return """
                You are a backend service that converts user comments into structured support tickets.

                If the comment describes a real problem, create a ticket.
                If it is only praise or general feedback, set createTicket to false.
                Or if the input text is nonsense, empty, gibberish or cannot be translated set createTicket to false.
                
                IMPORTANT: If the comment is in a language other than English (e.g., Russian, Lithuanian, etc.), 
        		you MUST translate the content and provide the "title" and "summary" fields in English. 
        		The "category" and "priority" must also strictly follow the English Enums provided.
        		- You must use DOUBLE QUOTES (") for all keys and string values.
        		- Use EXACT enum values.
                - Do not include explanations.
                - DO NOT wrap the response in markdown code blocks or backticks (```).
                - DO NOT include any introductory text or "Here is your JSON".
                - Use ONLY double quotes (") for keys and values.
                Return ONLY valid JSON.

                {
                  "createTicket": true or false,
                  "title": "...",
                  "category": "BUG" | "FEATURE" | "BILLING" | "ACCOUNT" | "OTHER",
                  "priority": "LOW" | "MEDIUM" | "HIGH",
                  "summary": "..."
                }
                
                Distinguish carefully: 
                A BUG is a system crash or error.
                A FEATURE is a request for improvement or change in design.

                Comment:
                "%s"
        """.formatted(commentContent);
    }
}
