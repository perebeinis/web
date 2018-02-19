package com.tracker.dao.process.audit;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.audit.elements.AuditElement;
import com.tracker.dao.process.audit.elements.impl.TextAuditElement;
import com.tracker.dao.process.audit.elements.impl.UserAuditElement;
import com.tracker.dao.process.data.DataProcessor;
import com.tracker.dao.process.data.elements.DataElement;
import com.tracker.dao.process.data.elements.impl.FileDataElement;
import com.tracker.dao.process.data.elements.impl.TextDataElement;
import com.tracker.dao.process.data.elements.impl.UserAssocDataElement;
import org.apache.log4j.helpers.ISO8601DateFormat;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Perebeinis on 16.02.2018.
 */
public class AuditService {
    @Autowired
    private MongoDatabase database;

    @Autowired
    private UserDetailsService customUserDetailsService;

    final static Map<String, Supplier<AuditElement>> auditElementsType = new HashMap<>();

    static {
        auditElementsType.put(BaseConstants.DEFAULT, TextAuditElement::new);
        auditElementsType.put(BaseConstants.USER_ASSOC, UserAuditElement::new);
    }

    private static AuditService instance;

    public static AuditService getInstance() {
        if (instance == null) {
            synchronized (AuditService.class) {
                if (instance == null) {
                    instance = new AuditService();
                }
            }
        }
        return instance;
    }

    public void auditData(String actionName, List<AuditObject> auditObjects){
        MongoCollection<Document> collection = database.getCollection(BaseConstants.getCollection(BaseConstants.AUDIT));
        Document auditDocument = new Document();
        auditDocument.put(BaseConstants.ACTION_NAME,actionName);
        auditDocument.put(BaseConstants.TIME, ISO8601DateFormat.getDateTimeInstance().format(new Date()));
        auditDocument.put(BaseConstants.CREATED, ISO8601DateFormat.getDateTimeInstance().format(new Date()));

        Document auditData = new Document();
        JSONArray jsonArray = new JSONArray();
        for (AuditObject auditObject : auditObjects) {
            Supplier<AuditElement> element = auditElementsType.get(auditObject.getFieldType());

            jsonArray.put(element != null ?
            element.get().createAuditData(auditObject.getFieldName(),auditObject.getFieldValue()) :
            auditElementsType.get(BaseConstants.DEFAULT).get().createAuditData(auditObject.getFieldName(), auditObject.getFieldValue()));

        }

        auditDocument.put(BaseConstants.AUDIT_DATA,jsonArray);
        collection.insertOne(auditDocument);
        System.out.println("add action to history");
    }
}
