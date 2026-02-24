package com.ms.sw.user.controller;

import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.dto.PasswordUpdateRequest;
import com.ms.sw.user.dto.PasswordUpdateResponse;
import com.ms.sw.user.dto.UserProfileDto;
import com.ms.sw.user.model.User;
import com.ms.sw.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET CURRENT USER INFO DETAILS
    @GetMapping
    public ResponseEntity<UserProfileDto> getUser(@CurrentUser User user) {
      log.info("getUser " );

      var res = userService.getUserProfileDto(user.getUsername());

      System.out.println("Res "+ res);
      return ResponseEntity.ok(res);
    }

    //UPDATE USER PROFILE
    @PutMapping
    public ResponseEntity<Void> updateUserProfileData(@CurrentUser User user,@RequestBody UserProfileDto userProfileDto) {
        log.info("UserController::updateUserProfileData invoked by -> {}", user.getUsername());

        userService.updateUserInfo(user.getUsername(), userProfileDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/security")
    public ResponseEntity<PasswordUpdateResponse> passwordUpdate(@CurrentUser User user, @RequestBody PasswordUpdateRequest req){
        log.info("UserController::passwordUpdate invoked by -> {}", user.getUsername());
        var response = userService.changePassword(user.getUsername(),req);

        return ResponseEntity.ok(response);
    }

}
