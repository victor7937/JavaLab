package com.epam.esm.util;

import com.epam.esm.exception.PartialUpdateException;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Util class for creating the main part of SQL update query by comparing and checking given fields
 * of current and modified objects for not equality
 * @param <T> - type of objects for checking
 */
public class PartialUpdater<T,K> {

    private T current;

    private K modified;

    private List<String> fieldNames;

    public PartialUpdater(T current, K modified, List<String> fieldNames) {
        this.current = current;
        this.modified = modified;
        this.fieldNames = fieldNames;
    }

    public void generatePartialUpdateData () throws PartialUpdateException {
        for (String property : fieldNames) {
            Object currentValue;
            Object modifiedValue;
            try {
                currentValue = PropertyUtils.getProperty(current, property);
                modifiedValue = PropertyUtils.getProperty(modified, property);
                if (!Objects.equals(currentValue, modifiedValue)){
                    PropertyUtils.setProperty(current, property, modifiedValue);
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
               throw new PartialUpdateException(e);
            }
        }
    }
}
