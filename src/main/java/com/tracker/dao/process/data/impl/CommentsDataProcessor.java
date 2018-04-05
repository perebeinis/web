package com.tracker.dao.process.data.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.tracker.config.security.authentification.CustomUserObject;
import com.tracker.config.security.authentification.impl.UserDetailsServiceImpl;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.DataProcessor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Perebeinis on 02.04.2018.
 */
public class CommentsDataProcessor implements DataProcessor {
    @Override
    public String processData(Object incomingDataObject, String elementType, String elementId) {
        JSONObject incomingData = (JSONObject) incomingDataObject;
        String parent = incomingData.getString(BaseConstants.PARENT);
        String commentValue = incomingData.getString(BaseConstants.COMMENT_VALUE);
        String documentId = incomingData.getString(BaseConstants.COMMENT_DOCUMENT_ID);

        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        MongoDatabase database = (MongoDatabase) context.getBean(BaseConstants.DATABASE);

        BasicDBObject query = new BasicDBObject();
        query.put(BaseConstants.DOCUMENT_ID, new ObjectId(documentId));
        MongoCollection<Document> tasks = database.getCollection(BaseConstants.getCollection(BaseConstants.ISSUE_COLLECTION));
        Document document = tasks.find(query).first();


        ObjectId commentAssocId = (ObjectId) document.get(BaseConstants.COMMENTS);
        if (document.get(BaseConstants.COMMENTS) == null) {
            MongoCollection<Document> comments = database.getCollection(BaseConstants.getCollection(BaseConstants.COMMENTS));
            Document newCommentDoc = new Document();
            comments.insertOne(newCommentDoc);
            commentAssocId = newCommentDoc.getObjectId(BaseConstants.DOCUMENT_ID);

            Bson commentsDoc = new Document(BaseConstants.COMMENTS, commentAssocId);

            tasks.updateOne(new Document(BaseConstants.DOCUMENT_ID, new ObjectId(documentId)), new Document(BaseConstants.SET, commentsDoc));
            addComment(database, commentAssocId, commentValue, true, parent);

        } else {
            addComment(database, commentAssocId, commentValue, false, parent);
        }

        System.out.println("added");
        return documentId;
    }

    private void addComment(MongoDatabase database, ObjectId commentAssocId, String commentText, boolean createNew, String parentNodeId) {
        MongoCollection<Document> comments = database.getCollection(BaseConstants.getCollection(BaseConstants.COMMENTS));

        BasicDBObject query = new BasicDBObject(BaseConstants.DOCUMENT_ID, commentAssocId);
        Document commentObject = comments.find(query).first();
        Document newComment = createCommentObject(commentText);
        List<Document> childs = new ArrayList<>();
        childs.add(newComment);


        boolean addSubComment = !parentNodeId.equals(BaseConstants.ROOT);

        if (createNew) {
            comments.updateOne(new Document(BaseConstants.DOCUMENT_ID, commentAssocId),
                    new Document(BaseConstants.SET,
                            new Document(BaseConstants.CHILD,
                                    childs)));


        }else if(addSubComment) {
            Document data = processCommentObject(commentObject, new ObjectId(parentNodeId), newComment);
            comments.replaceOne(new Document(BaseConstants.DOCUMENT_ID, commentAssocId), data, new UpdateOptions().upsert(true));

        } else {
            comments.updateOne(new Document(BaseConstants.DOCUMENT_ID, commentAssocId),
                    new Document(BaseConstants.PUSH,
                            new Document(BaseConstants.CHILD, newComment)));
        }

    }

    private Document processCommentObject(Document commentDocument, ObjectId searchCommentId, Document newComment) {
        commentDocument.remove(BaseConstants.DOCUMENT_ID);
        ObjectId foundCommentId = commentDocument.getObjectId(BaseConstants.COMMENT_ID);
        if (foundCommentId!=null && foundCommentId.equals(searchCommentId)) {
            ((List<Document>)commentDocument.get(BaseConstants.CHILD)).add(newComment);
        } else {
            List<Document> commentList = (List<Document>) commentDocument.get(BaseConstants.CHILD);
            for (Document document : commentList) {
                processCommentObject(document, searchCommentId, newComment);
            }
        }
        return commentDocument;
    }

    private Document createCommentObject(String commentText) {
        Document newComment = new Document();
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsServiceImpl userDetailsService = (UserDetailsServiceImpl) context.getBean(BaseConstants.CUSTOM_USER_DETAILS_SERVICE);
        CustomUserObject customUserObject = userDetailsService.loadUserDataByUsername(authentication.getName());

        newComment.put(BaseConstants.COMMENT_ID, new ObjectId());
        newComment.put(BaseConstants.CREATED, new Date());
        newComment.put(BaseConstants.CREATOR_USER, customUserObject.getAllUserFullName());
        newComment.put(BaseConstants.COMMENT_VALUE, commentText);
        newComment.put(BaseConstants.CHILD, new JSONArray());
        return newComment;
    }

    @Override
    public String createData(Object incomingDataObject, String elementType, String elementId) {
        return null;
    }

    @Override
    public String updateData(Object incomingDataObject, String elementType, String elementId) {
        return null;
    }

    @Override
    public String removeData() {
        return null;
    }
}
