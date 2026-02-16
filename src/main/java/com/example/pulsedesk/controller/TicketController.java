package com.example.pulsedesk.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pulsedesk.dto.TicketResponse;
import com.example.pulsedesk.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {
	private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }
    
    @GetMapping("/priority/{priority}")
    public List<TicketResponse> getTicketsByPriority(@PathVariable String priority) {
        return ticketService.getTicketsByPriority(priority);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
    	ticketService.deleteTicket(id);
        
        return ResponseEntity.noContent().build();
    }
}
