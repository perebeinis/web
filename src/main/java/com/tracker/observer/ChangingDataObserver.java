package com.tracker.observer;

import com.mongodb.client.MongoDatabase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Perebeinis on 23.01.2018.
 */
public class ChangingDataObserver {

    private ChangingData data;

    private MongoDatabase database;

    public void init(List<Observer> observers) {
        this.data = new ChangingData();
        for (Observer observer : observers) {
            data.register(observer);
            observer.setSubject(this.data);
        }
    }

    public void createEvent(String elementType, JSONArray formData, String elementId){
        NotifyData notifyData = new NotifyData(elementType, elementId, formData);
        data.processIncomingData(notifyData);
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

}
