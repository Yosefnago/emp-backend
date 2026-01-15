package com.ms.sw.user.service;

import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.dto.EventDto;
import com.ms.sw.user.model.Events;
import com.ms.sw.user.model.User;
import com.ms.sw.user.repo.EventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EventsService {

    private final EventsRepository eventsRepository;

    public EventsService(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    public List<EventDto> getAllEvents(String username) {

        return eventsRepository.getEventsByUsername(username);
    }
    public List<EventDto> getUpcomingEvents(String username) {
        return eventsRepository.getUpcomingEvents(username);
    }
    public Events addEvent(User user, EventDto eventDto) {

        Events newEvent = new Events();

        newEvent.setEventName(eventDto.eventName());
        newEvent.setDescription(eventDto.description());
        newEvent.setEventDate(eventDto.eventDate());
        newEvent.setEventTime(eventDto.eventTime());
        newEvent.setLocation(eventDto.location());
        newEvent.setPriority(eventDto.priority());
        newEvent.setNumberOfAttendance(eventDto.numberOfAttendance());
        newEvent.setUser(user);

        return eventsRepository.save(newEvent);
    }
}
