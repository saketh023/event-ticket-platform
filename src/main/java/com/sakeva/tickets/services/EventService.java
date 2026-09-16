package com.sakeva.tickets.services;

import com.sakeva.tickets.domain.CreateEventRequest;
import com.sakeva.tickets.domain.entities.Event;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
}
