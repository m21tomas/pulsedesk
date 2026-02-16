package com.example.pulsedesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pulsedesk.model.Ticket;
import com.example.pulsedesk.model.TicketPriority;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByPriority(TicketPriority priority);
}
