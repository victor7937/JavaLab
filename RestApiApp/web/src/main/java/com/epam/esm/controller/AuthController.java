package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.AlreadyExistServiceException;
import com.epam.esm.exception.IncorrectDataServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.security.AuthDTO;
import com.epam.esm.security.AuthResponse;
import com.epam.esm.security.provider.UserAuthenticationProviderImpl;
import com.epam.esm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public AuthResponse signup(@RequestBody UserDTO userDTO){
        try {
            userService.registration(userDTO);
        } catch (AlreadyExistServiceException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ServiceException e){
            log.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        AuthDTO authDTO = new AuthDTO(userDTO.getEmail(), userDTO.getPassword());
        return new AuthResponse(userDTO.getEmail(), authenticationProvider.authenticate(authDTO));
    }

}
