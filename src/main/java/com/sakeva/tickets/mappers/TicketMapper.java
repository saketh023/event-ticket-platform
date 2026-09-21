package com.sakeva.tickets.mappers;

import com.sakeva.tickets.domain.dtos.ListTicketResponseDto;
import com.sakeva.tickets.domain.dtos.ListTicketTicketTypeResponseDto;
import com.sakeva.tickets.domain.entities.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    ListTicketTicketTypeResponseDto toTicketTicketTypeResponseDto(ListTicketTicketTypeResponseDto dto);

    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);

}
