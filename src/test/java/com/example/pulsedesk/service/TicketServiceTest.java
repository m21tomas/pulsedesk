package com.example.pulsedesk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pulsedesk.dto.TicketResponse;
import com.example.pulsedesk.model.Comment;
import com.example.pulsedesk.model.Ticket;
import com.example.pulsedesk.model.TicketCategory;
import com.example.pulsedesk.model.TicketPriority;
import com.example.pulsedesk.repository.CommentRepository;
import com.example.pulsedesk.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

	@Mock
    private TicketRepository ticketRepository;
	
	@Mock
    private CommentRepository commentRepository;
	
	@InjectMocks
	private TicketService ticketService;
	
	@Test
	void deleteTicketSetsCommentFlagToFalse() {
	    Long ticketId = 1L;
	    Comment comment = new Comment();
	    Ticket ticket = new Ticket();
	    ticket.setId(ticketId);
	    comment.setConvertedToTicket(true);
	    comment.setCreatedAt(LocalDateTime.now());
	    comment.setTicket(ticket);

	    when(commentRepository.findByTicketId(ticketId)).thenReturn(comment);

	    ticketService.deleteTicket(ticketId);

	    assertFalse(comment.isConvertedToTicket()); // The most important check!
	    verify(commentRepository, times(1)).save(comment);
	}
	
	@Test
	void getTicketsByPriority_ReturnsFilteredList() {
	    String priorityStr = "HIGH";
	    Ticket ticket = new Ticket();
	    ticket.setPriority(TicketPriority.HIGH);
	    ticket.setCategory(TicketCategory.BUG);
	    ticket.setCreatedAt(LocalDateTime.now());

	    when(ticketRepository.findByPriority(TicketPriority.HIGH))
	            .thenReturn(List.of(ticket));

	    List<TicketResponse> responses = ticketService.getTicketsByPriority(priorityStr);

	    assertEquals(1, responses.size());
	    assertEquals("HIGH", responses.get(0).getPriority());
	}
	
	@Test
	void getTicketsByPriority_DefaultsToMedium_WhenPriorityIsUnknown() {
	    String unknownPriority = "SUPER_HIGH";
	    
	    when(ticketRepository.findByPriority(TicketPriority.MEDIUM))
	            .thenReturn(Collections.emptyList());

	    ticketService.getTicketsByPriority(unknownPriority);

	    verify(ticketRepository).findByPriority(TicketPriority.MEDIUM);
	}
}
