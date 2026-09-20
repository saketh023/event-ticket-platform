package com.sakeva.tickets.services;

import com.sakeva.tickets.domain.CreateEventRequest;
import com.sakeva.tickets.domain.UpdateEventRequest;
import com.sakeva.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);
    Optional<Event> getEventForOrganizer(UUID organizerId, UUID id);
    Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event);
    void deleteEventForOrganizer(UUID organizerId, UUID id);
    Page<Event> listPublishedEvents(Pageable pageable);
    Page<Event> searchPublishedEvents(String query, Pageable pageable);
    Optional<Event> getPublishedEvent(UUID id);
}
