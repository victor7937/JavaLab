package com.epam.esm.security.provider;

import com.epam.esm.security.AuthDTO;

import javax.servlet.http.HttpServletRequest;

public interface UserAuthenticationProvider {
    String authenticate(AuthDTO authDTO);
    String getUsernameFromRequest(HttpServletRequest request);
}
