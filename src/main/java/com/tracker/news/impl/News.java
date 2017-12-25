package com.tracker.news.impl;

import com.tracker.news.Observer;
import com.tracker.news.Subject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Perebeinis on 19.12.2017.
 */
public class News implements Subject {
    private List<Observer> observers;
    private JSONObject news;
    private boolean changed;
    private static final Object monitor = new Object();

    public News(){
        this.observers=new ArrayList<>();
    }
    @Override
    public void register(Observer obj) {
        if(obj == null) {
            throw new NullPointerException("Null Observer");
        }
        synchronized (monitor) {
            if(!observers.contains(obj)) {
                observers.add(obj);
            }
        }
    }

    @Override
    public void unregister(Observer obj) {
        synchronized (monitor) {
            observers.remove(obj);
        }
    }

    @Override
    public void notifyObservers() {
        List<Observer> observersLocal = null;
        //synchronization is used to make sure any news registered after message is received is not notified
        synchronized (monitor) {
            if (!changed) return;
            observersLocal = new ArrayList<>(this.observers);
            this.changed=false;
        }
        for (Observer observer : observersLocal) {
            observer.update();
        }

    }

    @Override
    public Object getUpdate(Observer obj) {
        return this.news;
    }

    //method to post message to the topic
    public void postData(JSONObject news){
        System.out.println("New news was created :"+news);
        this.news = news;
        this.changed=true;
        notifyObservers();
    }
}
