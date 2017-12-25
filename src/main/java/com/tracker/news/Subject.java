package com.tracker.news;

/**
 * Created by Perebeinis on 19.12.2017.
 */
public interface Subject {
    public void register(Observer obj);
    public void unregister(Observer obj);
    public void notifyObservers();
    public Object getUpdate(Observer obj);
}
