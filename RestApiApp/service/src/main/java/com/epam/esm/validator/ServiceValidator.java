package com.epam.esm.validator;


/**
 * Interface for validation data at the service layer.
 * Validates some entity that goes through the service layer and its id.
 * @param <T> - entity for validation type
 * @param <K> - type of its id
 */
public interface ServiceValidator <T,K> {

    /**
     * @param entity - entity object for checking
     * @return - true if entity is correct, else false
     */
    boolean validate(T entity);

    /**
     * @param id - id of entity for validation
     * @return true if id is correct, else false
     */
    boolean isIdValid(K id);
}
