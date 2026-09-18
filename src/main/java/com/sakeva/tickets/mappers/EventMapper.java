package com.sakeva.tickets.mappers;

import com.sakeva.tickets.domain.CreateEventRequest;
import com.sakeva.tickets.domain.CreateTicketTypeRequest;
import com.sakeva.tickets.domain.dtos.*;
import com.sakeva.tickets.domain.entities.Event;
import com.sakeva.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventResponseDto(Event event);

    GetEventTicketTypesResponseDto toGetEventTicketTypesResponseDto(TicketType ticketType);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);
}
