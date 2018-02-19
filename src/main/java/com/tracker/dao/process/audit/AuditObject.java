package com.tracker.dao.process.audit;

/**
 * Created by Perebeinis on 16.02.2018.
 */
public class AuditObject {
    private String fieldName;
    private String fieldType;
    private Object fieldValue;

    public AuditObject(String fieldName, String fieldType, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
