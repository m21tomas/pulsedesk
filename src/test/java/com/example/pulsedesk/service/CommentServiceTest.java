package com.example.pulsedesk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pulsedesk.dto.AiTriageResponse;
import com.example.pulsedesk.exception.ResourceNotFoundException;
import com.example.pulsedesk.model.Comment;
import com.example.pulsedesk.model.Ticket;
import com.example.pulsedesk.model.TicketCategory;
import com.example.pulsedesk.repository.CommentRepository;
import com.example.pulsedesk.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
	@Mock
    private CommentRepository commentRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private HuggingFaceService huggingFaceService;

    @InjectMocks
    private CommentService commentService;
    
    @Test
    void createTicketWhenAiSaysTrue() {

        String text = "My account was suspended";

        AiTriageResponse mockResponse = new AiTriageResponse();
        mockResponse.setCreateTicket(true);
        mockResponse.setCategory("ACCOUNT");
        mockResponse.setPriority("HIGH");
        mockResponse.setTitle("Account Suspension");
        mockResponse.setSummary("User requests reactivation");

        when(huggingFaceService.analyzeComment(text))
                .thenReturn(mockResponse);
        
        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setContent(text);
        savedComment.setCreatedAt(LocalDateTime.now());
        savedComment.setConvertedToTicket(true);

        when(commentRepository.save(any(Comment.class)))
                .thenReturn(savedComment);

        commentService.submitComment(text);

        verify(ticketRepository, times(1)).save(any());
        verify(commentRepository, times(1)).save(any());
    }
    
    @Test
    void doNotCreateTicketWhenAiSaysFalse() {
        String text = "The app is fine";

        AiTriageResponse mockResponse = new AiTriageResponse();
        mockResponse.setCreateTicket(false);
        mockResponse.setCategory("OTHER");
        mockResponse.setPriority("LOW");
        mockResponse.setTitle("App fine");
        mockResponse.setSummary("Positive feedback on the app");

        when(huggingFaceService.analyzeComment(text))
                .thenReturn(mockResponse);
        
        Comment savedComment = new Comment();
        savedComment.setId(2L);
        savedComment.setContent(text);
        savedComment.setCreatedAt(LocalDateTime.now());
        savedComment.setConvertedToTicket(false);

        when(commentRepository.save(any(Comment.class)))
                .thenReturn(savedComment);

        commentService.submitComment(text);

        verify(ticketRepository, never()).save(any(Ticket.class));
        verify(commentRepository, times(1)).save(any(Comment.class));
        
        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentCaptor.capture());
        assertFalse(commentCaptor.getValue().isConvertedToTicket());
    }
    
    @Test
    void throwsExceptionWhenCommentNotFound() {
        Long invalidId = 99L;
        when(commentRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.getCommentById(invalidId);
        });
    }
    
    @Test
    void throwExceptionWhenAiDownButSaveComment() {
        String text = "Help me!";
        when(huggingFaceService.analyzeComment(anyString()))
            .thenThrow(new RuntimeException("AI Triage Failed: Service Down"));

        assertThrows(RuntimeException.class, () -> {
            commentService.submitComment(text);
        });
        
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(ticketRepository, never()).save(any());
    }
    
    @Test
    void setsCategoryToOther_WhenAiReturnsInvalidCategory() {
        String text = "Something strange happened";

        AiTriageResponse mockResponse = new AiTriageResponse();
        mockResponse.setCreateTicket(true);
        mockResponse.setCategory("PIZZA"); // Invalid category
        mockResponse.setPriority("LOW");
        mockResponse.setTitle("Strange issue");
        mockResponse.setSummary("AI returned an invalid category");

        when(huggingFaceService.analyzeComment(text)).thenReturn(mockResponse);
        
        Comment dummyComment = new Comment();
        dummyComment.setId(10L);
        dummyComment.setCreatedAt(LocalDateTime.now());
        when(commentRepository.save(any(Comment.class))).thenReturn(dummyComment);

        commentService.submitComment(text);

        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(ticketCaptor.capture());
        
        assertEquals(TicketCategory.OTHER, ticketCaptor.getValue().getCategory());
    }
}
