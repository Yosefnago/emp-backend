package com.ms.sw.user.controller;

import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.dto.EventDto;
import com.ms.sw.user.model.User;
import com.ms.sw.user.service.EventsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("events")
public class EventsController {

    private final EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventDto>> getAllEvents(@CurrentUser User user) {

        List<EventDto> eventDtos = eventsService.getAllEvents(user.getUsername());
        return ResponseEntity.ok().body(eventDtos);
    }
    @GetMapping()
    public ResponseEntity<List<EventDto>> getEvents(@CurrentUser User user) {

        List<EventDto> eventDtos = eventsService.getUpcomingEvents(user.getUsername());
        return ResponseEntity.ok().body(eventDtos);
    }
    @PostMapping("/add")
    public ResponseEntity<EventDto> addEvent(@CurrentUser User user, @RequestBody EventDto eventDto) {

        eventsService.addEvent(user,eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
