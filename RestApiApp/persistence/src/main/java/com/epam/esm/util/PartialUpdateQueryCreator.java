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
public class PartialUpdateQueryCreator<T> {

    private T current;

    private T modified;

    private List<String> fieldNames;

    private List<Object> values;

    private String query;

    private boolean isUpdated;

    private static final String SQL_UPDATE_PART = "UPDATE %s SET ";
    private static final String DELIMITER = ", ";
    private static final String QUESTION_MARK = "?";
    private static final String QUESTION_MARK_WITH_EQUALS = "=?";

    public PartialUpdateQueryCreator(T current, T modified, List<String> fieldNames) {
        this.current = current;
        this.modified = modified;
        this.fieldNames = fieldNames;
        values = new ArrayList<>();
    }

    public List<Object> getValues() {
        return values;
    }

    public String createQuery(String tableName) {
        if (isUpdated) {
            return String.format(query, tableName);
        } else {
            return "";
        }
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void generatePartialUpdateData () throws PartialUpdateException {
        StringBuilder queryBuilder = new StringBuilder("");
        for (String property : fieldNames) {
            Object currentValue;
            Object modifiedValue;
            try {
                currentValue = PropertyUtils.getProperty(current, property);
                modifiedValue = PropertyUtils.getProperty(modified, property);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
               throw new PartialUpdateException(e);
            }
            if (!Objects.equals(currentValue, modifiedValue)){
                queryBuilder.append((queryBuilder.length() != 0 && queryBuilder.lastIndexOf(QUESTION_MARK)
                        == queryBuilder.length() - 1) ? DELIMITER : "").append(property).append(QUESTION_MARK_WITH_EQUALS);

                values.add(modifiedValue);
            }
        }
        if (queryBuilder.length() != 0) {
            queryBuilder.insert(0, SQL_UPDATE_PART);
            query = queryBuilder.toString();
            isUpdated = true;
        }
    }
}
