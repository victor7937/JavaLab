package com.epam.esm.validator;


/**
 * Interface for validation data at the service layer.
 * Validates some entity that goes through the service layer and its id.
 * @param <T> - entity for validation type
 * @param <K> - type of its id
 */
public interface ServiceValidator <T> {

    /**
     * @param model - model object for checking
     * @return - true if model is correct, else false
     */
    boolean validate(T model);

    default boolean isLongIdValid(Long id) {
        return id != null && id > 0L;
    };

    default boolean isStringIdValid(String id) {
        return id != null && !id.isBlank();
    };


    default boolean isPageParamsValid(int pageSize, int pageNumber){
        return pageSize > 0 && pageNumber > 0;
    }
}
