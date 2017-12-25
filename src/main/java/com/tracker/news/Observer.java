package com.tracker.news;

/**
 * Created by Perebeinis on 19.12.2017.
 */
public interface Observer {
    public void update();
    public void setSubject(Subject sub);
}
