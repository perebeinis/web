package com.tracker.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.dao.create.DataCreator;
import com.tracker.dao.create.user.UserDataCreator;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

public class UserData {

    public void getUserData(MongoDatabase database){
        System.out.println("aa");
        DataCreator dataCreator = new UserDataCreator();
        dataCreator.createData(database);
    }

    public void createNewUser(){

    }
}
