package com.sakeva.tickets.mappers;

import com.sakeva.tickets.domain.CreateEventRequest;
import com.sakeva.tickets.domain.CreateTicketTypeRequest;
import com.sakeva.tickets.domain.dtos.CreateEventRequestDto;
import com.sakeva.tickets.domain.dtos.CreateEventResponseDto;
import com.sakeva.tickets.domain.dtos.CreateTicketTypeRequestDto;
import com.sakeva.tickets.domain.dtos.CreateTicketTypeResponseDto;
import com.sakeva.tickets.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);
}
