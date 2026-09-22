package com.sakeva.tickets.services;

import com.sakeva.tickets.domain.entities.Ticket;

import java.util.UUID;

public interface TicketTypeService {
    Ticket purchaseTicket(UUID id, UUID ticketTypeId);
}
