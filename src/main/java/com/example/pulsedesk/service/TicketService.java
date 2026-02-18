package com.example.pulsedesk.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pulsedesk.dto.TicketResponse;
import com.example.pulsedesk.exception.ResourceNotFoundException;
import com.example.pulsedesk.model.Comment;
import com.example.pulsedesk.model.Ticket;
import com.example.pulsedesk.model.TicketPriority;
import com.example.pulsedesk.repository.CommentRepository;
import com.example.pulsedesk.repository.TicketRepository;

import jakarta.transaction.Transactional;

@Service
public class TicketService {
	private final TicketRepository ticketRepository;
	private final CommentRepository commentRepository;
	
	private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public TicketService(TicketRepository ticketRepository, CommentRepository commentRepository) {
        this.ticketRepository = ticketRepository;
		this.commentRepository = commentRepository;
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return mapToResponse(ticket);
    }
    
    public List<TicketResponse> getTicketsByPriority(String priority) {
        TicketPriority ticketPriority = TicketPriority.fromString(priority.toUpperCase());
        
        return ticketRepository.findByPriority(ticketPriority)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Transactional
    public void deleteTicket(Long id) {
        Comment comment = commentRepository.findByTicketId(id);
        
        if (comment == null) {
            throw new ResourceNotFoundException("No comment found linked to ticket ID: " + id);
        }

        comment.setTicket(null);
        comment.setConvertedToTicket(false);

        commentRepository.save(comment);
    }
    
    @Transactional
    public void deleteAllTickets() {
        ticketRepository.deleteAll();
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        return new TicketResponse(
            ticket.getId(),
            ticket.getTitle(),
            ticket.getCategory().name(),
            ticket.getPriority().name(),
            ticket.getSummary(),
            ticket.getCreatedAt().format(ISO_FORMATTER)
        );
    }
}
