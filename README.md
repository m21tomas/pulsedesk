# 🚀 PulseDesk: AI-Powered Multilingual Support Triage

**PulseDesk** is a modern backend service designed to solve a common enterprise problem: efficiently triaging customer feedback in a multilingual environment. Using state-of-the-art Large Language Models (LLMs), PulseDesk automatically translates, categorizes, and prioritizes incoming comments in real-time.

## 🌟 Key Features
* **Multilingual Intelligent Triage**: Native support for English, Russian, and Lithuanian.
* **Automated Translation**: Non-English comments are automatically translated into English for internal ticket management.
* **High-Availability AI Strategy**: Implements a "Model Fallback" chain to ensure 100% uptime even if a specific AI provider is down.
* **Glass-Morphism UI**: A sleek, modern dashboard for visualizing live comment-to-ticket conversion.
* **API Documentation**: Fully documented endpoints via Swagger/OpenAPI.

## 🛠️ Tech Stack
* **Backend**: Java 17, Spring Boot 4.0.2, Spring Data JPA.
* **Database**: H2 (In-Memory) for rapid demonstration.
* **AI Integration**: Hugging Face Inference API (Router).
* **Frontend**: HTML5, Tailwind CSS, JavaScript (Fetch API).
* **Documentation**: SpringDoc OpenAPI (Swagger).

## 🧠 Advanced Technical Implementation

### AI Resilience Logic
PulseDesk does not rely on a single AI model. To handle the volatility of 2026 AI providers, I implemented a fallback mechanism in `HuggingFaceService.java`. If the primary model fails or returns invalid formatting, the system automatically tries the next candidate:

1.  **Qwen 2.5 7B (Together AI)** - Primary for speed & multilingual accuracy.
2.  **Mistral 7B v0.2 (Featherless AI)** - Secondary for stability.
3.  **Gemma 2 9B** - Tertiary fallback.

### Robust JSON Parsing
Large Language Models occasionally return "chatty" responses or missing delimiters. PulseDesk utilizes a **custom Regex-based sanitization layer** and a lenient **Jackson 3 configuration** to "peel" JSON content out of Markdown code blocks and repair missing root braces before persistence.

## 🚀 Getting Started

### Prerequisites
* Java 17 JDK or higher
* Maven 3.6+
* A Hugging Face API Token (with access to the Inference API)

### Installation
1. **Clone the repository:**
```bash
git clone [https://github.com/your-username/pulsedesk.git](https://github.com/your-username/pulsedesk.git)
cd pulsedesk
```
2. **Set your API Token:**
Set your token as an environment variable:
```bash
export HF_API_TOKEN=your_token_here
```
3. **Run the application:**
**Windows:**
```bash
mvnw spring-boot:run
```
**Windows (Git Bash) / Linux / macOS**
```bash
./mvnw spring-boot:run
```
or using installed Maven:
```bash
mvn spring-boot:run
```
4. **Access the Dashboard:**
Open <http://localhost:8080> in your browser.

## 📈 Testing the System
The application is pre-loaded via `DataInitializer.java` with 6 diverse test cases:
* **English:** Bug reports and general praise.
* **Lithuanian:** Feature requests and support team feedback.
* **Russian:** Complex billing issues and gibberish detection.

## 📝 API Endpoints
You can explore the full API schema via Swagger at:
`http://localhost:8080/swagger-ui/index.html`