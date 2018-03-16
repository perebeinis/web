package com.tracker.dao.process.data.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.tracker.config.security.authentification.CustomUserObject;
import com.tracker.config.security.authentification.impl.UserDetailsServiceImpl;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.audit.AuditObject;
import com.tracker.dao.process.audit.AuditService;
import com.tracker.dao.process.data.DataProcessor;
import com.tracker.dao.process.data.DataProcessorService;
import com.tracker.dao.process.data.elements.DataElement;
import org.apache.log4j.helpers.ISO8601DateFormat;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

public class SingleExecutorTaskDataProcessor implements DataProcessor {

    private Document createDataObject = new Document();
    private BasicDBObject updateDataObject = new BasicDBObject();
    private List<AuditObject> auditObjects = new ArrayList<>();

    @Override
    public String processData(JSONArray incomingDataObject, String elementType, String elementId) {
        if (StringUtils.isEmpty(elementId)) {
            return createData(incomingDataObject, elementType, elementId);
        } else {
            return updateData(incomingDataObject, elementType, elementId);
        }
    }

    @Override
    public String createData(JSONArray incomingDataObject, String elementType, String elementId) {
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        MongoDatabase database = (MongoDatabase) context.getBean(BaseConstants.DATABASE);
        UserDetailsServiceImpl userDetailsService = (UserDetailsServiceImpl) context.getBean(BaseConstants.CUSTOM_USER_DETAILS_SERVICE);

        MongoCollection<Document> collection = database.getCollection(BaseConstants.getCollection(elementType));
        addDefaultData();
        for (Object formField : incomingDataObject) {
            JSONObject formFieldElement = (JSONObject) formField;
            String fieldName = (String) formFieldElement.get(BaseConstants.NAME);
            String fieldType = (String) formFieldElement.get(BaseConstants.TYPE);
            String value = (String) formFieldElement.get(BaseConstants.DATA);

            //Set currentTaskExecutor
            processCurrentExecutor(fieldType,userDetailsService,value);

            Supplier<DataElement> element = DataProcessorService.getInstance().getSavingElementsType().get(fieldType);
            Object fieldValue = element != null ?
                    DataProcessorService.getInstance().getSavingElementsType().get(fieldType).get().getData(database, formFieldElement) :
                    DataProcessorService.getInstance().getSavingElementsType().get(BaseConstants.TEXT).get().getData(database, formFieldElement);

            processFieldValue(fieldValue,fieldType, fieldName);
            auditObjects.add(new AuditObject(fieldName, fieldType, fieldValue));
        }

        ObjectId objectId = createDataObject.getObjectId(BaseConstants.DOCUMENT_ID);
        collection.insertOne(createDataObject);
        AuditService.getInstance().auditData(BaseConstants.CREATE, auditObjects,elementType, objectId);

        System.out.println("created new element with type = " + elementType);
        return createDataObject.getObjectId(BaseConstants.DOCUMENT_ID).toString();
    }

    @Override
    public String updateData(JSONArray incomingDataObject, String elementType, String elementId) {
        JSONObject currentElement = DataProcessorService.getInstance().getElementById(elementType, elementId);

        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        MongoDatabase database = (MongoDatabase) context.getBean(BaseConstants.DATABASE);
        MongoCollection<Document> collection = database.getCollection(BaseConstants.getCollection(elementType));
        UserDetailsServiceImpl userDetailsService = (UserDetailsServiceImpl) context.getBean(BaseConstants.CUSTOM_USER_DETAILS_SERVICE);

        List<AuditObject> auditObjects = new ArrayList<>();
        for (Object formField : incomingDataObject) {
            JSONObject formFieldElement = (JSONObject) formField;
            String fieldName = (String) formFieldElement.get(BaseConstants.NAME);
            String fieldType = (String) formFieldElement.get(BaseConstants.TYPE);
            String value = (String) formFieldElement.get(BaseConstants.DATA);
            if (!value.equals(currentElement.get(fieldName).toString())) {
                Supplier<DataElement> element = DataProcessorService.getInstance().getSavingElementsType().get(fieldType);

                Object fieldValue = element != null ?
                        DataProcessorService.getInstance().getSavingElementsType().get(fieldType).get().getData(database, formFieldElement) :
                        DataProcessorService.getInstance().getSavingElementsType().get(BaseConstants.TEXT).get().getData(database, formFieldElement);

                updateDataObject.put(fieldName, fieldValue);
                auditObjects.add(new AuditObject(fieldName, fieldType, currentElement.get(fieldName).toString() +" = "+ fieldValue));

                updateDataObject.put(BaseConstants.CURRENT_TASK_EXECUTOR, null);
                CustomUserObject lastExecutorData = userDetailsService.loadUserById(new ObjectId(((JSONObject) currentElement.get(BaseConstants.CURRENT_TASK_EXECUTOR)).get(BaseConstants.MONGO_ID).toString()));
                String userFullName = lastExecutorData.getAllUserData().get(BaseConstants.FIRST_NAME) + " "+lastExecutorData.getAllUserData().get(BaseConstants.LAST_NAME);
                auditObjects.add(new AuditObject(BaseConstants.CURRENT_TASK_EXECUTOR, BaseConstants.TEXT, userFullName +" = "));
            }
        }

        if (updateDataObject.size() > 0) {
            Bson setData = new Document(BaseConstants.SET, updateDataObject);
            collection.updateOne(new Document(BaseConstants.DOCUMENT_ID, new ObjectId(elementId)), setData);
            AuditService.getInstance().auditData(BaseConstants.UPDATE, auditObjects, elementType, new ObjectId(elementId));
            System.out.println("UPDATE DATA");
        }

        return elementId;
    }

    private void processCurrentExecutor(String fieldType, UserDetailsServiceImpl userDetailsService, String value){
        if (fieldType.equals(BaseConstants.USER_ASSOC) && createDataObject.get(BaseConstants.CURRENT_TASK_EXECUTOR) == null) {
            String firstExecutorObjectId = value.split(",")[0];
            CustomUserObject customUserObject = userDetailsService.loadUserById(new ObjectId(firstExecutorObjectId));
            JSONObject userData = customUserObject.getAllUserData();
            String userFullName = userData.get(BaseConstants.FIRST_NAME) + " " + userData.get(BaseConstants.LAST_NAME);
            createDataObject.put(BaseConstants.CURRENT_TASK_EXECUTOR, new ObjectId(firstExecutorObjectId));
            auditObjects.add(new AuditObject(BaseConstants.CURRENT_TASK_EXECUTOR, BaseConstants.TEXT, userFullName));
        }
    }

    private void processFieldValue(Object fieldValue, String fieldType, String fieldName){
        if(fieldType.equals(BaseConstants.FILE)){
            if(createDataObject.get(fieldName)!=null){
                ((List<ObjectId>) createDataObject.get(fieldName)).add((ObjectId) fieldValue);
            }else{
                List<ObjectId> objectIds = new ArrayList<>();
                objectIds.add((ObjectId) fieldValue);
                createDataObject.put(fieldName, objectIds);
            }
        }else{
            createDataObject.put(fieldName, fieldValue);
        }
    }

    private void addDefaultData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        UserDetailsServiceImpl userDetailsService = (UserDetailsServiceImpl) context.getBean(BaseConstants.CUSTOM_USER_DETAILS_SERVICE);

        ObjectId userId = userDetailsService.loadUserIdByUsername(authentication.getName());
        List<ObjectId> objectIds = new ArrayList<>();
        objectIds.add(userId);
        createDataObject.put(BaseConstants.CREATOR, objectIds);
        createDataObject.put(BaseConstants.CREATED,  ISO8601DateFormat.getDateTimeInstance().format(new Date()));

        String userFullName = userDetailsService.getUserDataById(userId).get(BaseConstants.LAST_NAME)+" "+userDetailsService.getUserDataById(userId).get(BaseConstants.FIRST_NAME);
        auditObjects.add(new AuditObject(BaseConstants.CREATOR, BaseConstants.CREATOR, userFullName));
    }

    @Override
    public String removeData() {
        return "";
    }
}
