package com.epam.esm;

import com.epam.esm.generating.TagsGenerator;
import com.epam.esm.util.ApplicationContextProvider;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL_FORMS})
@EnableJpaRepositories(basePackages = {"com.epam.esm.repository"})
public class RestApiAppApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(RestApiAppApplication.class, args);
    }

}
