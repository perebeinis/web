package com.tracker.dao.process.data.impl;

import com.mongodb.BasicDBObject;
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
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MessageDataProcessor implements DataProcessor {

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

        MongoCollection<Document> collection = database.getCollection(BaseConstants.getCollection(elementType));
        for (Object formField : incomingDataObject) {
            JSONObject formFieldElement = (JSONObject) formField;
            String fieldName = (String) formFieldElement.get(BaseConstants.NAME);
            String fieldType = (String) formFieldElement.get(BaseConstants.TYPE);
            String value = (String) formFieldElement.get(BaseConstants.DATA);

            Supplier<DataElement> element = DataProcessorService.getInstance().getSavingElementsType().get(fieldType);
            Object fieldValue = element != null ?
                    DataProcessorService.getInstance().getSavingElementsType().get(fieldType).get().getData(database, formFieldElement) :
                    DataProcessorService.getInstance().getSavingElementsType().get(BaseConstants.TEXT).get().getData(database, formFieldElement);

            processFieldValue(fieldValue,fieldType, fieldName);
            auditObjects.add(new AuditObject(fieldName, fieldType, fieldValue));
        }

        ObjectId objectId = (ObjectId) createDataObject.get(BaseConstants.DOCUMENT_ID);
        collection.insertOne(createDataObject);
        AuditService.getInstance().auditData(BaseConstants.CREATE, auditObjects,elementType, objectId);

        System.out.println("created new element with type = " + elementType);
        return createDataObject.get(BaseConstants.DOCUMENT_ID).toString();
    }

    @Override
    public String updateData(JSONArray incomingDataObject, String elementType, String elementId) {
        System.out.println("UPDATE message");
        return elementId;
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

    @Override
    public String removeData() {
        return "";
    }
}
