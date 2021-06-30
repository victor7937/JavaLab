package com.epam.esm;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestApiAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiAppApplication.class, args);
    }

}
