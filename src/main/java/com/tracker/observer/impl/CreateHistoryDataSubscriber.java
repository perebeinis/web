package com.tracker.observer.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.observer.ChangingData;
import com.tracker.observer.NotifyData;
import com.tracker.observer.Observer;
import com.tracker.observer.Subject;
import org.json.JSONObject;

/**
 * Created by Perebeinis on 23.01.2018.
 */
public class CreateHistoryDataSubscriber extends Observer{
    private Subject subject = new ChangingData();

    private MongoDatabase database;

    @Override
    public void update() {
        NotifyData history = (NotifyData) subject.getUpdate(this);
        System.out.println("2) create new history element = "+history);
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public void setSubject(Subject sub) {
        this.subject = sub;
    }
}
