package com.epam.esm.security.provider;

import com.epam.esm.entity.User;
import com.epam.esm.security.AuthDTO;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;

@Component
public class UserAuthenticationProviderImpl implements UserAuthenticationProvider {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public UserAuthenticationProviderImpl(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String authenticate(AuthDTO authDTO){
        String email = authDTO.getEmail();
        String password = authDTO.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return tokenProvider.createToken(email);
    }

    @Override
    public String getUsernameFromRequest(HttpServletRequest request){
        return tokenProvider.getUsername(tokenProvider.resolveToken(request));
    }

}
