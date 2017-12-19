package com.tracker.news.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.news.Observer;
import com.tracker.news.Subject;
import org.bson.Document;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Perebeinis on 19.12.2017.
 */
public class NewsSubscriber implements Observer {

    private String subscriberName;
    private Subject subject;
    private MongoDatabase database;
    private static final String NEWS_TABLE = "news";

    public NewsSubscriber(String subscriberName,MongoDatabase database) {
        this.subscriberName = subscriberName;
        this.database = database;
    }

    @Override
    public void update() {
        JSONObject news = (JSONObject) subject.getUpdate(this);
        if (news == null) {
            System.out.println(subscriberName + ":: No new news");
        } else
            System.out.println(subscriberName + ":: Consuming message::" + news.toString());
    }

    @Override
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    private JSONObject createNewNews(JSONObject news){
        MongoCollection<Document> collection = database.getCollection(NEWS_TABLE);
        Document document = new Document();
        Iterator<?> keys = news.keys();
        while(keys.hasNext() ) {
            String key = (String)keys.next();
            document.put(key, news.get(key));
        }
        collection.insertOne(document);
        System.out.println("NEWS CREATED");
        return new JSONObject();
    }

}