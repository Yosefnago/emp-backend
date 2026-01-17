package com.ms.sw.user.service;

import com.ms.sw.user.dto.ActivityLogsDto;
import com.ms.sw.user.model.ActionType;
import com.ms.sw.user.model.ActivityLogs;
import com.ms.sw.user.repo.ActivityLogsRepository;
import com.ms.sw.views.dto.ActivityLogsListDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;import org.springframework.data.domain.PageRequest;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(ActionType actionType, String affectedEmployee, String username) {
        try {

            ActivityLogs activityLogs = new ActivityLogs();
            activityLogs.setFromUser(username);
            activityLogs.setAction(actionType);
            activityLogs.setAffectedEmployee(affectedEmployee);
            activityLogs.setDateAction(LocalDate.now());
            activityLogs.setTimeAction(LocalTime.now());

            activityLogsRepository.save(activityLogs);

        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }
    public List<ActivityLogsListDto> getAllActivities(String username) {
        return activityLogsRepository.getAllActivity(username);
    }

}
