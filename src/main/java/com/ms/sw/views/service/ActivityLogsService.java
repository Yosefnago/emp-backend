package com.ms.sw.service;

import com.ms.sw.Dto.ActivityLogsDto;
import com.ms.sw.customUtils.CurrentUser;
import com.ms.sw.entity.ActivityLogs;
import com.ms.sw.repository.ActivityLogsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ActivityLogsService {

    private final ActivityLogsRepository activityLogsRepository;

    public ActivityLogsService(ActivityLogsRepository activityLogsRepository) {
        this.activityLogsRepository = activityLogsRepository;
    }

    public List<ActivityLogsDto> getLastActivity(String username) {

       return activityLogsRepository.getLastActivityByUsername(username,PageRequest.of(0,3));

    }
    public void logAction(String action,String username) {
        ActivityLogs activityLogs = createActivityLogs(action,username);
        activityLogsRepository.save(activityLogs);
    }
    private ActivityLogs createActivityLogs(String action,String username) {
        ActivityLogs log = new ActivityLogs();
        log.setFromUser(username);
        log.setAction(action);
        log.setDateAction(LocalDate.now());
        log.setTimeAction(LocalTime.now());
        return log;
    }

}
