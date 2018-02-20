package com.tracker.dao.process.data;

import com.google.gson.JsonParser;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.audit.AuditObject;
import com.tracker.dao.process.audit.AuditService;
import com.tracker.dao.process.data.elements.DataElement;
import com.tracker.dao.process.data.elements.impl.FileDataElement;
import com.tracker.dao.process.data.elements.impl.TextDataElement;
import com.tracker.dao.process.data.elements.impl.UserAssocDataElement;
import com.tracker.dao.process.data.impl.DefaultDataProcessor;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Perebeinis on 15.02.2018.
 */
public class DataProcessorService {

    @Autowired
    private MongoDatabase database;

    final static Map<String, Supplier<DataElement>> savingElementsType = new HashMap<>();

    static {
        savingElementsType.put("file", FileDataElement::new);
        savingElementsType.put("text", TextDataElement::new);
        savingElementsType.put("userAssoc", UserAssocDataElement::new);
    }

    private static DataProcessorService instance;

    public static DataProcessorService getInstance() {
        if (instance == null) {
            synchronized (DataProcessorService.class) {
                if (instance == null) {
                    instance = new DataProcessorService();
                }
            }
        }
        return instance;
    }

    public String processIncomingData() {
        return "";
    }

    public String createData(String elementType, JSONArray incomingData) {
        MongoCollection<Document> collection = database.getCollection(BaseConstants.getCollection(elementType));
        Document document = new Document();
        List<AuditObject> auditObjects = new ArrayList<>();

        for (Object formField : incomingData) {
            JSONObject formFieldElement = (JSONObject) formField;
            String fieldName = (String) formFieldElement.get(BaseConstants.NAME);
            String fieldType = (String) formFieldElement.get(BaseConstants.TYPE);

            Supplier<DataElement> element = savingElementsType.get(fieldType);
            Object fieldValue = element != null ? savingElementsType.get(fieldType).get().getData(database, formFieldElement) : savingElementsType.get(BaseConstants.TEXT).get().getData(database, formFieldElement);
            document.put(fieldName, fieldValue);
            auditObjects.add(new AuditObject(fieldName, fieldType, fieldValue));
        }

        collection.insertOne(document);
        ObjectId objectId = document.getObjectId(BaseConstants.DOCUMENT_ID);
        AuditService.getInstance().auditData(BaseConstants.CREATE, auditObjects, elementType, objectId);
        System.out.println("created new element with type = " + elementType);
        return objectId.toString();
    }


    public JSONObject getElementById(String elementType, String elementId){
        MongoCollection<Document> collection = database.getCollection(BaseConstants.getCollection(elementType));
        Bson match = new Document(BaseConstants.MATCH, new Document(BaseConstants.DOCUMENT_ID, new ObjectId(elementId)));

        List<Bson> filters = new ArrayList<>();
        filters.add(match);

        AggregateIterable<Document> iterator = collection.aggregate(filters);
        ArrayList<Document> docs = new ArrayList();
        iterator.into(docs);

        JSONObject result  = new JSONObject();
        if(docs.size()>0){
            result = new JSONObject(new JsonParser().parse(docs.get(0).toJson()).getAsJsonObject().toString());
        }

        return result;
    }

    public Map<String, Supplier<DataElement>> getSavingElementsType() {
        return savingElementsType;
    }
}
