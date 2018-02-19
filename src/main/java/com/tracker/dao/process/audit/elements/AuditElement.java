package com.tracker.dao.process.audit.elements;

/**
 * Created by Perebeinis on 16.02.2018.
 */
public interface AuditElement {
    Object createAuditData(String fieldName, Object incomingData);
}
