package com.epam.esm.validator;

public interface ServiceValidator <T,K> {
    boolean validate(T entity);
    boolean isIdValid(K id);
}
