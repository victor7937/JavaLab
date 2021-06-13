package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagValidator implements ServiceValidator<Tag, Integer>{

    @Override
    public boolean validate(Tag entity) {
        return entity != null && entity.getName() != null && !entity.getName().isBlank();
    }

    @Override
    public boolean isIdValid(Integer id) {
        return id != null && id > 0;
    }
}
