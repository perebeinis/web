package com.tracker.dao.groups;

import com.mongodb.BasicDBObject;
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
    private Map<String, String> systemGroupsCache = new HashMap<>();

    @Autowired
    private MongoDatabase database;

    public Map<String, String> getAllGroups() {
        MongoCollection<Document> collection = database.getCollection(BaseConstants.GROUPS);
        FindIterable<Document> documents = collection.find();
        for (Document document : documents) {
            systemGroupsCache.put(document.getString(BaseConstants.NAME), document.getObjectId(BaseConstants.DOCUMENT_ID).toString());
        }
        return systemGroupsCache;
    }

    public String processUserGroup(String groupName, boolean alreadyExist, String userId) {
        if (alreadyExist) {
            MongoCollection<Document> groups = database.getCollection(BaseConstants.getCollection(BaseConstants.GROUPS));
            BasicDBObject query = new BasicDBObject(BaseConstants.DOCUMENT_ID, new ObjectId(systemGroupsCache.get(groupName)));
            Document groupObject = groups.find(query).first();

            groups.updateOne(new Document(BaseConstants.DOCUMENT_ID, new ObjectId(systemGroupsCache.get(groupName))),
                    new Document(BaseConstants.PUSH,
                            new Document(BaseConstants.GROUP_USERS, new ObjectId(userId))));

            return systemGroupsCache.get(groupName);
        } else {
            MongoCollection<Document> collection = database.getCollection(BaseConstants.GROUPS);
            Document document = new Document();
            List<AuditObject> auditObjects = new ArrayList<>();
            document.put(BaseConstants.NAME, groupName);

            List<ObjectId> groupAdmins = new ArrayList<>();
            groupAdmins.add(new ObjectId(userId));

            document.put(BaseConstants.GROUP_ADMINS, groupAdmins);
            document.put(BaseConstants.CREATOR, groupAdmins);
            document.put(BaseConstants.GROUP_USERS, new JSONArray());

            collection.insertOne(document);
            ObjectId objectId = document.getObjectId(BaseConstants.DOCUMENT_ID);
            AuditService.getInstance().auditData(BaseConstants.CREATE, auditObjects, BaseConstants.GROUPS, objectId);
            System.out.println("created new element with type = " + BaseConstants.GROUPS);

            return objectId.toString();
        }

    }

    public String getGroupById(String groupId) {
        return "";
    }
}
