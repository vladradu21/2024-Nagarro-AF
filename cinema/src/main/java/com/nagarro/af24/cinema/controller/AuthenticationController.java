package com.nagarro.af24.cinema.controller;

import com.nagarro.af24.cinema.dto.LoginDTO;
import com.nagarro.af24.cinema.dto.RegisterDTO;
import com.nagarro.af24.cinema.dto.ResponseDTO;
import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterDTO registerDTO) {
        return authenticationService.register(registerDTO);
    }

    @PostMapping("/login")
    public ResponseDTO login(@RequestBody LoginDTO loginDTO) {
        return authenticationService.login(loginDTO);
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestParam String email) {
        authenticationService.sendResetPasswordEmail(email);
    }

    @PostMapping("/reset-password")
    public UserDTO resetPassword(@RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
        return authenticationService.resetPassword(email, otp, newPassword);
    }
}