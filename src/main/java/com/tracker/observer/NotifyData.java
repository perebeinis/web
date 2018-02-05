package com.tracker.observer;

import org.json.JSONArray;

/**
 * Created by Perebeinis on 23.01.2018.
 */
public class NotifyData {
    private String elementType;
    private String elementId;
    private JSONArray formData;

    public NotifyData(String elementType, String elementId, JSONArray formData) {
        this.elementType = elementType;
        this.elementId = elementId;
        this.formData = formData;
    }

    public String getElementType() {
        return elementType;
    }

    public String getElementId() {
        return elementId;
    }

    public JSONArray getFormData() {
        return formData;
    }
}
