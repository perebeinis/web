package com.tracker.observer;

import com.tracker.observer.Observer;
import com.tracker.observer.Subject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Perebeinis on 23.01.2018.
 */
public class ChangingData implements Subject {

    private List<Observer> observers;
    private NotifyData notifyData;
    private boolean changed;
    private static final Object monitor = new Object();

    public ChangingData(){
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
        return this.notifyData;
    }

    //method to post message to the topic
    public void processIncomingData(NotifyData notifyData){
        System.out.println("New news was created :"+notifyData);
        this.notifyData = notifyData;
        this.changed=true;
        notifyObservers();
    }
}
