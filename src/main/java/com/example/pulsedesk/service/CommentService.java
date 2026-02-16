package com.example.pulsedesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pulsedesk.dto.AiTriageResponse;
import com.example.pulsedesk.dto.CommentResponse;
import com.example.pulsedesk.exception.ResourceNotFoundException;
import com.example.pulsedesk.model.Comment;
import com.example.pulsedesk.model.Ticket;
import com.example.pulsedesk.model.TicketCategory;
import com.example.pulsedesk.model.TicketPriority;
import com.example.pulsedesk.repository.CommentRepository;
import com.example.pulsedesk.repository.TicketRepository;

@Service
public class CommentService {
	private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final HuggingFaceService huggingFaceService;
    
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public CommentService(CommentRepository commentRepository,
                          TicketRepository ticketRepository,
                          HuggingFaceService huggingFaceService) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
        this.huggingFaceService = huggingFaceService;
    }
    
    @Transactional
    public CommentResponse submitComment(String text) {
        Comment comment = new Comment();
        comment.setContent(text);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setConvertedToTicket(false);
        
        comment = commentRepository.save(comment);

        AiTriageResponse aiResponse = huggingFaceService.analyzeComment(text);

        if (aiResponse.getCreateTicket()) {

            Ticket ticket = new Ticket();
            ticket.setTitle(aiResponse.getTitle());
            ticket.setCategory(TicketCategory.fromString(aiResponse.getCategory()));
            ticket.setPriority(TicketPriority.fromString(aiResponse.getPriority()));
            ticket.setSummary(aiResponse.getSummary());
            ticket.setCreatedAt(LocalDateTime.now());
            ticket.setComment(comment);
            comment.setTicket(ticket);

            ticketRepository.save(ticket);
            
            comment.setConvertedToTicket(true);
        }
        return mapToResponse(comment);
    }
    
    public List<CommentResponse> getAllComments() {
    	return commentRepository.findAllWithTickets().stream()
    	        .map(comment -> mapToResponse(comment))
    	        .toList();
    }
    
    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        
        return mapToResponse(comment);
    }
    
    private CommentResponse mapToResponse(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedAt().format(ISO_FORMATTER),
            comment.isConvertedToTicket() ? "Yes" : "No"
        );
    }
    
    @Transactional
    public void deleteComment(Long id) {
    	Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        commentRepository.delete(comment);
    }
}
