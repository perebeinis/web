package com.tracker.dao.groups;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.audit.AuditObject;
import com.tracker.dao.process.audit.AuditService;
import com.tracker.dao.process.data.elements.DataElement;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Perebeinis on 05.05.2018.
 */
public class ProcessUserGroups {
    private Map<String, String> groupsCache = new HashMap<>();

    @Autowired
    private MongoDatabase database;

    public List<String> getAllGroups(){
        List<String> allGroups = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection(BaseConstants.GROUPS);
        FindIterable<Document> documents = collection.find();
        for (Document document : documents) {
            groupsCache.put(document.getString(BaseConstants.NAME), document.getObjectId(BaseConstants.DOCUMENT_ID).toString());
            allGroups.add(document.getString(BaseConstants.NAME));
        }

        return allGroups;
    }

    public String createNewGroup(String groupName){
        MongoCollection<Document> collection = database.getCollection(BaseConstants.GROUPS);
        Document document = new Document();
        List<AuditObject> auditObjects = new ArrayList<>();
        document.put(BaseConstants.NAME, groupName);
        document.put(BaseConstants.TYPE, BaseConstants.GROUP_TYPE);
        document.put(BaseConstants.CHILD, new JSONArray());

        collection.insertOne(document);
        ObjectId objectId = document.getObjectId(BaseConstants.DOCUMENT_ID);
        AuditService.getInstance().auditData(BaseConstants.CREATE, auditObjects, BaseConstants.GROUPS, objectId);
        System.out.println("created new element with type = " + BaseConstants.GROUPS);

        return objectId.toString();
    }

    public String getGroupById(String groupId){
        return "";
    }
}
