package com.sakeva.tickets.services.impl;

import com.sakeva.tickets.domain.CreateEventRequest;
import com.sakeva.tickets.domain.entities.Event;
import com.sakeva.tickets.domain.entities.TicketType;
import com.sakeva.tickets.domain.entities.User;
import com.sakeva.tickets.exceptions.UserNotFoundException;
import com.sakeva.tickets.repositories.EventRepository;
import com.sakeva.tickets.repositories.UserRepository;
import com.sakeva.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // generates a constructor based on any instance variables specified in the class that are final
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
    User organizer =
        userRepository.findById(organizerId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with id %s not found", organizerId)
        ));

    List<TicketType> ticketTypesToCreate =
        event.getTicketTypes().stream()
            .map(
                ticketType -> {
                  TicketType ticketTypeToCreate = new TicketType();
                  ticketTypeToCreate.setName(ticketType.getName());
                  ticketTypeToCreate.setPrice(ticketType.getPrice());
                  ticketTypeToCreate.setDescription(ticketType.getDescription());
                  ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                  return ticketTypeToCreate;
                })
            .toList();

    Event eventToCreate = new Event();
    eventToCreate.setName(event.getName());
    eventToCreate.setStart(event.getStart());
    eventToCreate.setEnd(event.getEnd());
    eventToCreate.setVenue(event.getVenue());
    eventToCreate.setSalesStart(event.getSalesStart());
    eventToCreate.setSalesEnd(event.getSalesEnd());
    eventToCreate.setStatus(event.getStatus());
    eventToCreate.setOrganizer(organizer);
    eventToCreate.setTicketTypes(ticketTypesToCreate);

    return eventRepository.save(eventToCreate);
    }
}
