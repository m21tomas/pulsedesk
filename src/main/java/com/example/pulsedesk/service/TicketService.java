package com.example.pulsedesk.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pulsedesk.dto.TicketResponse;
import com.example.pulsedesk.exception.ResourceNotFoundException;
import com.example.pulsedesk.model.Comment;
import com.example.pulsedesk.model.Ticket;
import com.example.pulsedesk.model.TicketPriority;
import com.example.pulsedesk.repository.TicketRepository;

import jakarta.transaction.Transactional;

@Service
public class TicketService {
	private final TicketRepository ticketRepository;
	private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
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
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        Comment comment = ticket.getComment();
        if (comment != null) {
            comment.setConvertedToTicket(false);
        }
        ticketRepository.delete(ticket);
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
            ticket.getCreatedAt().format(ISO_FORMATTER),
            ticket.getComment().getId()
        );
    }
}
