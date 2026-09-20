package com.sakeva.tickets.config;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sakeva.tickets.domain.entities.QrCode;
import com.sakeva.tickets.domain.entities.QrCodeStatusEnum;
import com.sakeva.tickets.domain.entities.Ticket;
import com.sakeva.tickets.exceptions.QrCodeGenerationException;
import com.sakeva.tickets.repositories.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class QrCodeConfig {

    @Bean
    public QRCodeWriter qrCodeWriter() {
        return new QRCodeWriter();
    }
}
