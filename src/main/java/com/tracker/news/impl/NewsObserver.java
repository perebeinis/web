package com.tracker.news.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.news.Observer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Perebeinis on 19.12.2017.
 */
public class NewsObserver {
    private News news;

    private MongoDatabase database;

    public void init() {
        this.news = new News();
        Observer newsObserver = new NewsSubscriber("news", database);
        news.register(newsObserver);
        newsObserver.setSubject(news);
    }

    public void createNews(JSONObject jsonObject){
        news.postData(jsonObject);
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }
}
