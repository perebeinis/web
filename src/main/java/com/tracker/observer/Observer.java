package com.tracker.observer;

import com.mongodb.client.MongoDatabase;

/**
 * Created by Perebeinis on 23.01.2018.
 */
public abstract class Observer {
    public abstract void setSubject(Subject sub);
    public abstract void update();
}
