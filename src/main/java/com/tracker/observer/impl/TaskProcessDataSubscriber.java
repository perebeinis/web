package com.tracker.observer.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.DataSearchFactory;
import com.tracker.observer.ChangingData;
import com.tracker.observer.NotifyData;
import com.tracker.observer.Observer;
import com.tracker.observer.Subject;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Perebeinis on 23.01.2018.
 */
public class TaskProcessDataSubscriber extends Observer{
    private Subject subject = new ChangingData();

    @Autowired
    private MongoDatabase database;

    @Autowired
    private DataSearchFactory dataSearchFactory;

    @Override
    public void setSubject(Subject sub) {
        this.subject = sub;
    }

    @Override
    public void update() {
        NotifyData notifyData = (NotifyData) subject.getUpdate(this);
        String elementType = notifyData.getElementType();
        if(elementType.equals(BaseConstants.ISSUE_CONSTANT)){
            String elementId = notifyData.getElementId();
            JSONArray jsonArray = notifyData.getFormData();
            for (Object o : jsonArray) {
                JSONObject formElement = (JSONObject) o;
                String name = (String) formElement.get("name");
                if(name.equals(BaseConstants.USER_ASSOC)){
                    String assocUserId = (String) formElement.get("data");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(BaseConstants.CURRENT_TASK_EXECUTOR, new ObjectId(assocUserId.split(",")[0]));
                  //  dataSearchFactory.updateElementById(elementType,elementId,jsonObject);
                }

            }

        }

        System.out.println("3) task process start - " + notifyData.getElementType());
    }
}
