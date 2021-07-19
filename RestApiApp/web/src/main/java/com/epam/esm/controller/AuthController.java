package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.security.AuthDTO;
import com.epam.esm.security.AuthResponse;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private static final String EMAIL_OR_PASSWORD_INCORRECT_MSG = "Incorrect email or password";

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthDTO authDTO){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
        String token = tokenProvider.createToken(authDTO.getEmail());
        return new AuthResponse(authDTO.getEmail(), token);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody AuthDTO authDTO){
        return authDTO.getEmail() + " authenticated";
    }

}
