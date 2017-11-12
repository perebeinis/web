package com.tracker.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

public class UserData {

    public void getUserData(MongoDatabase database){
        System.out.println("aa");
        MongoCollection<Document> collection = database.getCollection("userdetails");
        System.out.println("Collection sampleCollection selected successfully");

        // Getting the iterable object
        FindIterable<Document> iterDoc = collection.find();
        int i = 1;

        // Getting the iterator
        Iterator it = iterDoc.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
            i++;
        }
    }
}
