package com.ms.sw.user.controller;

import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.dto.PasswordUpdateRequest;
import com.ms.sw.user.dto.PasswordUpdateResponse;
import com.ms.sw.user.dto.UserProfileDto;
import com.ms.sw.user.model.User;
import com.ms.sw.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET CURRENT USER INFO DETAILS
    @GetMapping
    public ResponseEntity<UserProfileDto> getUser(@CurrentUser User user) {
        log.info("GET /user/ -> getUser -> user={}", user.getUsername());

        var res = userService.getUserProfileDto(user.getUsername());

      return ResponseEntity.ok(res);
    }

    //UPDATE USER PROFILE
    @PutMapping
    public ResponseEntity<Void> updateUserProfileData(@CurrentUser User user,@RequestBody UserProfileDto userProfileDto) {
        log.info("PUT /user/ -> updateUserProfileData -> user={}", user.getUsername());

        userService.updateUserInfo(user.getUsername(), userProfileDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/security")
    public ResponseEntity<PasswordUpdateResponse> passwordUpdate(@CurrentUser User user, @RequestBody PasswordUpdateRequest req){
        log.info("POST /user/security -> passwordUpdate -> user={}", user.getUsername());
        var response = userService.changePassword(user.getUsername(),req);

        return ResponseEntity.ok(response);
    }

}
