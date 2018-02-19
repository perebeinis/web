package com.tracker.dao.process.data.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.config.security.authentification.CustomUserObject;
import com.tracker.config.security.authentification.impl.UserDetailsServiceImpl;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.audit.AuditObject;
import com.tracker.dao.process.audit.AuditService;
import com.tracker.dao.process.data.DataProcessor;
import com.tracker.dao.process.data.DataProcessorService;
import com.tracker.dao.process.data.elements.DataElement;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SingleExecutorTaskDataProcessor implements DataProcessor {
    @Override
    public String processData(JSONArray incomingDataObject, String elementType, String elementId) {
        return createData(incomingDataObject,elementType, elementId);
    }

    @Override
    public String createData(JSONArray incomingDataObject, String elementType, String elementId) {
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        MongoDatabase database = (MongoDatabase) context.getBean(BaseConstants.DATABASE);
        UserDetailsServiceImpl userDetailsService = (UserDetailsServiceImpl) context.getBean(BaseConstants.CUSTOM_USER_DETAILS_SERVICE);

        MongoCollection<Document> collection = database.getCollection(BaseConstants.getCollection(elementType));
        Document document = new Document();
        List<AuditObject> auditObjects = new ArrayList<>();

        for (Object formField : incomingDataObject) {
            JSONObject formFieldElement = (JSONObject) formField;
            String fieldName = (String) formFieldElement.get(BaseConstants.NAME);
            String fieldType = (String) formFieldElement.get(BaseConstants.TYPE);
            String value = (String) formFieldElement.get(BaseConstants.DATA);

            //Set currentTaskExecutor
            if(fieldType.equals(BaseConstants.USER_ASSOC) && document.get(BaseConstants.CURRENT_TASK_EXECUTOR) == null){
                String firstExecutorObjectId = value.split(",")[0];
                CustomUserObject customUserObject = userDetailsService.loadUserById(new ObjectId(firstExecutorObjectId));
                JSONObject userData = customUserObject.getAllUserData();
                String userFullName = userData.get(BaseConstants.FIRST_NAME) + " "+userData.get(BaseConstants.LAST_NAME);
                document.put(BaseConstants.CURRENT_TASK_EXECUTOR, new ObjectId(firstExecutorObjectId));
                auditObjects.add(new AuditObject(BaseConstants.CURRENT_TASK_EXECUTOR, BaseConstants.TEXT, userFullName));
            }

            Supplier<DataElement> element = DataProcessorService.getInstance().getSavingElementsType().get(fieldType);
            Object fieldValue = element != null ?
                    DataProcessorService.getInstance().getSavingElementsType().get(fieldType).get().getData(database, formFieldElement) :
                    DataProcessorService.getInstance().getSavingElementsType().get(BaseConstants.TEXT).get().getData(database, formFieldElement);
            document.put(fieldName, fieldValue);
            auditObjects.add(new AuditObject(fieldName, fieldType, fieldValue));
        }

        collection.insertOne(document);
        AuditService.getInstance().auditData(BaseConstants.CREATE, auditObjects);

        ObjectId objectId = document.getObjectId(BaseConstants.DOCUMENT_ID);
        System.out.println("created new element with type = " + elementType);
        return objectId.toString();
    }

    @Override
    public void updateData() {

    }

    @Override
    public void removeData() {

    }
}
