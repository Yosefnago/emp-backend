package com.ms.sw.service;

import com.ms.sw.Dto.ActivityLogsDto;
import com.ms.sw.customUtils.CurrentUser;
import com.ms.sw.entity.ActivityLogs;
import com.ms.sw.repository.ActivityLogsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogsService {

    private final ActivityLogsRepository activityLogsRepository;

    public ActivityLogsService(ActivityLogsRepository activityLogsRepository) {
        this.activityLogsRepository = activityLogsRepository;
    }

    public void save(ActivityLogs activityLogs) {
        ActivityLogs logs = new ActivityLogs();
        logs.setFromUser(activityLogs.getFromUser());
        logs.setAction(activityLogs.getAction());
        logs.setDateAction(activityLogs.getDateAction());
        logs.setTimeAction(activityLogs.getTimeAction());
        activityLogsRepository.save(logs);
    }
    public List<ActivityLogsDto> getLastActivity(String username) {

       return activityLogsRepository.getLastActivityByUsername(username,PageRequest.of(0,3));

    }

}
