package com.sakeva.tickets.controllers;

import com.sakeva.tickets.domain.dtos.ListPublishedEventResponseDto;
import com.sakeva.tickets.mappers.EventMapper;
import com.sakeva.tickets.repositories.EventRepository;
import com.sakeva.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/published-events")
public class PublishedEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents(Pageable pageable) {
        return ResponseEntity.ok(
                eventService.listPublishedEvents(pageable).map(eventMapper::toListPublishedEventResponseDto));
    }
}
