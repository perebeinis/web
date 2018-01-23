package com.tracker.observer.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.observer.ChangingData;
import com.tracker.observer.NotifyData;
import com.tracker.observer.Observer;
import com.tracker.observer.Subject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Perebeinis on 23.01.2018.
 */
public class CreateNewsDataSubscriber extends Observer{
    private Subject subject = new ChangingData();

    @Autowired
    private MongoDatabase database;

    @Override
    public void update() {
        NotifyData news = (NotifyData) subject.getUpdate(this);
        System.out.println("1) create new news = "+news);
    }

    @Override
    public void setSubject(Subject sub) {
        this.subject = sub;
    }

}
