package com.ms.sw.user.controller;

import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.dto.UserProfileDto;
import com.ms.sw.user.model.User;
import com.ms.sw.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserProfileDto> getUser(@CurrentUser User user) {
      log.info("getUser " );

      var res = userService.getUserProfileDto(user.getUsername());

      System.out.println("Res "+ res);
      return ResponseEntity.ok(res);
    }

}
