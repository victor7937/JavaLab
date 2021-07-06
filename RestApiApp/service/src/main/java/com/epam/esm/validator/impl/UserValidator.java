package com.epam.esm.validator.impl;

import com.epam.esm.entity.User;
import com.epam.esm.validator.ServiceValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements ServiceValidator<User> {

    @Override
    public boolean validate(User model) {
        return model != null && model.getName() != null && !model.getName().isBlank()
                && model.getSurname() != null && !model.getSurname().isBlank()
                && model.getEmail() != null && isStringIdValid(model.getEmail());
    }

    @Override
    public boolean isStringIdValid(String id) {
        return ServiceValidator.super.isStringIdValid(id) && EmailValidator.getInstance().isValid(id);
    }
}
