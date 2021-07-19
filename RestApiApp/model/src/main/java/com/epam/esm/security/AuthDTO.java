package com.epam.esm.security;

import lombok.Data;

@Data
public class AuthDTO {

    private String email;

    private String password;
}
