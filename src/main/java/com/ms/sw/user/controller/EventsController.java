package com.ms.sw.user.controller;

import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.dto.EventDto;
import com.ms.sw.user.model.User;
import com.ms.sw.user.service.EventsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for managing user events.
 *
 * <p>Provides endpoints for retrieving and creating events
 * associated with the currently authenticated user.</p>
 */
@RestController
@RequestMapping("events")
@Slf4j
public class EventsController {

    private final EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    /**
     * Retrieves all events for the current user.
     *
     * @param user authenticated user resolved from the security context
     * @return list of all user events
     */
    @GetMapping("/all")
    public ResponseEntity<List<EventDto>> getAllEvents(@CurrentUser User user) {
        log.info("GET /events/all -> getAllEvents -> user={}", user.getUsername());
        List<EventDto> eventDtos = eventsService.getAllEvents(user.getUsername());
        return ResponseEntity.ok().body(eventDtos);
    }

    /**
     * Retrieves upcoming events for the current user.
     *
     * @param user authenticated user resolved from the security context
     * @return list of upcoming events
     */
    @GetMapping()
    public ResponseEntity<List<EventDto>> getEvents(@CurrentUser User user) {
        log.info("GET /events/ -> getEvents -> user={}", user.getUsername());
        List<EventDto> eventDtos = eventsService.getUpcomingEvents(user.getUsername());
        return ResponseEntity.ok().body(eventDtos);
    }

    /**
     * Creates a new event for the current user.
     *
     * @param user     authenticated user
     * @param eventDto event data to create
     * @return HTTP 201 (Created) on success
     */
    @PostMapping("/add")
    public ResponseEntity<EventDto> addEvent(@CurrentUser User user, @RequestBody EventDto eventDto) {
        log.info("GET /events/add -> addEvent -> user={}", user.getUsername());
        eventsService.addEvent(user,eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
