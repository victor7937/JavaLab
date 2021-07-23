package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.security.AuthDTO;
import com.epam.esm.security.AuthResponse;
import com.epam.esm.security.provider.UserAuthenticationProviderImpl;
import com.epam.esm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthController {

    private static final String EXCEPTION_CAUGHT_MSG = "Exception was caught in Auth Controller";

    private final UserService userService;
    private final UserAuthenticationProviderImpl authenticationProvider;

    @Autowired
    public AuthController(UserService userService, UserAuthenticationProviderImpl authenticationProvider) {
        this.userService = userService;
        this.authenticationProvider = authenticationProvider;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthDTO authDTO){
        return new AuthResponse(authDTO.getEmail(), authenticationProvider.authenticate(authDTO));
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody UserDTO userDTO) throws ServiceException {
        userService.registration(userDTO);
        AuthDTO authDTO = new AuthDTO(userDTO.getEmail(), userDTO.getPassword());
        return new AuthResponse(userDTO.getEmail(), authenticationProvider.authenticate(authDTO));
    }

}
