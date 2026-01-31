package com.ms.sw.user.service;

import com.ms.sw.user.dto.EventDto;
import com.ms.sw.user.model.Events;
import com.ms.sw.user.model.User;
import com.ms.sw.user.repo.EventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing user events.
 *
 * <p>Provides methods to retrieve all events, upcoming events, and to add new events
 * for a specific user.</p>
 */
@Service
@Slf4j
public class EventsService {

    private final EventsRepository eventsRepository;

    public EventsService(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    /**
     * Retrieves all events for a given user.
     *
     * @param username the username of the user
     * @return list of {@link EventDto} representing all events of the user
     */
    public List<EventDto> getAllEvents(String username) {

        return eventsRepository.getEventsByUsername(username);
    }

    /**
     * Retrieves upcoming events for a given user.
     *
     * <p>Upcoming events are typically events with a date greater than or equal to today.</p>
     *
     * @param username the username of the user
     * @return list of {@link EventDto} representing upcoming events
     */
    public List<EventDto> getUpcomingEvents(String username) {
        return eventsRepository.getUpcomingEvents(username);
    }

    /**
     * Adds a new event for a specific user.
     *
     * @param user     the user creating the event
     * @param eventDto the event details to be added
     */
    public void addEvent(User user, EventDto eventDto) {

        Events newEvent = new Events();

        newEvent.setEventName(eventDto.eventName());
        newEvent.setDescription(eventDto.description());
        newEvent.setEventDate(eventDto.eventDate());
        newEvent.setEventTime(eventDto.eventTime());
        newEvent.setLocation(eventDto.location());
        newEvent.setPriority(eventDto.priority());
        newEvent.setNumberOfAttendance(eventDto.numberOfAttendance());
        newEvent.setUser(user);

        eventsRepository.save(newEvent);
    }
}
