package com.epam.esm.util;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StatusCodeGenerator {

    private static final Map<Class<?>, Integer> classCodes = new HashMap<>();

    static {
        classCodes.put(GiftCertificateController.class, 1);
        classCodes.put(TagController.class,2);
        classCodes.put(UserController.class, 3);
    }


    public static int getCode (HttpStatus status, Class<?> controllerClass){
        return status.value() * 10 + Optional.ofNullable(classCodes.get(controllerClass)).orElse(0);
    }


}
