package com.sakeva.tickets.services;

import com.sakeva.tickets.domain.entities.QrCode;
import com.sakeva.tickets.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
