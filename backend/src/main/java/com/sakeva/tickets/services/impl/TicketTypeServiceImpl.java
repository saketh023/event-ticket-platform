package com.sakeva.tickets.services.impl;

import com.sakeva.tickets.domain.entities.Ticket;
import com.sakeva.tickets.domain.entities.TicketStatusEnum;
import com.sakeva.tickets.domain.entities.TicketType;
import com.sakeva.tickets.domain.entities.User;
import com.sakeva.tickets.exceptions.TicketTypeNotFoundException;
import com.sakeva.tickets.exceptions.TicketsSoldOutException;
import com.sakeva.tickets.exceptions.UserNotFoundException;
import com.sakeva.tickets.repositories.TicketRepository;
import com.sakeva.tickets.repositories.TicketTypeRepository;
import com.sakeva.tickets.repositories.UserRepository;
import com.sakeva.tickets.services.QrCodeService;
import com.sakeva.tickets.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %s not found", userId)));

        TicketType ticketType = ticketTypeRepository
                .findByIdWithLock(ticketTypeId)
                .orElseThrow(() -> new TicketTypeNotFoundException(
                        String.format("TicketType with ID %s not found", ticketTypeId)));

        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketType.getId());
        Integer totalAvailable = ticketType.getTotalAvailable();

        if (purchasedTickets + 1 > totalAvailable) {
            throw new TicketsSoldOutException();
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);

        Ticket savedTicket = ticketRepository.save(ticket);
        qrCodeService.generateQrCode(savedTicket);

        return ticketRepository.save(savedTicket);
    }
}
