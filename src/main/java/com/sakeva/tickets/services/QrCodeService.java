package com.sakeva.tickets.services;

import com.sakeva.tickets.domain.entities.QrCode;
import com.sakeva.tickets.domain.entities.Ticket;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);

}
