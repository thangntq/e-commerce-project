package com.shopme.admin.user.controller;

import com.shopme.admin.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {

    private final UserService service;

    @PostMapping("/check_email")
    public String checkDuplicateEmail(@Param("id")Integer id,@Param("email") String email){

        return service.isEmailUnique(id,email) ? "OK" : "Duplicated";
    }
}
