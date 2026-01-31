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

/**
 * Service for managing activity logs of users.
 *
 * <p>Provides methods to log actions, retrieve recent activities, and get all activities
 * performed by a specific user.</p>
 */
@Service
public class ActivityLogsService {

    private final ActivityLogsRepository activityLogsRepository;

    public ActivityLogsService(ActivityLogsRepository activityLogsRepository) {
        this.activityLogsRepository = activityLogsRepository;
    }

    /**
     * Retrieves the last few activity logs of a user.
     *
     * @param username the username whose activity logs are retrieved
     * @return list of {@link ActivityLogsDto} containing the last activities
     */
    public List<ActivityLogsDto> getLastActivity(String username) {

       return activityLogsRepository.getLastActivityByUsername(username,PageRequest.of(0,3));
    }

    /**
     * Logs a user action into the activity logs.
     *
     * <p>This operation runs in a new transaction to ensure logging occurs
     * independently of the main transaction.</p>
     *
     * @param actionType       type of action performed
     * @param affectedEmployee name of the affected employee (if applicable)
     * @param username         username of the user performing the action
     */
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
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves all activity logs for a given user.
     *
     * @param username the username whose activities are retrieved
     * @return list of {@link ActivityLogsListDto} containing all activities
     */
    public List<ActivityLogsListDto> getAllActivities(String username) {
        return activityLogsRepository.getAllActivity(username);
    }

}
