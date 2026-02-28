package com.ms.sw.user.controller;

import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.dto.DepartmentDetailsDto;
import com.ms.sw.user.model.User;
import com.ms.sw.user.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/department")
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<DepartmentDetailsDto> getDepartmentDetails(
            @CurrentUser User user, @PathVariable String name){
        log.info("GET /department/{} -> getDepartmentDetails -> user={}", name,user.getUsername());

        var response = departmentService
                .getDepartmentDetails(user.getUsername(),  name);

        return ResponseEntity.ok(response);
    }
}
