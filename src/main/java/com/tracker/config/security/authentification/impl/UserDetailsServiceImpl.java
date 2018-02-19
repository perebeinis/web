package com.tracker.config.security.authentification.impl;

import com.google.gson.JsonParser;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.tracker.cards.CardDataProcessor;
import com.tracker.config.security.authentification.CustomUserObject;;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.AbstractDataSearch;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private MongoDatabase database;
    private static List<CustomUserObject> users = new ArrayList();
    private static Map<ObjectId,JSONObject> userListCacheData = new HashMap<>();

    public UserDetailsServiceImpl(MongoDatabase database, CardDataProcessor cardDataProcessor) {
        this.database = database;
        reloadUsers();
    }

    /*
    * Authentificate user from database
    */
    public void reloadUsers(){
        List<Bson> filters = new ArrayList<>();
        filters = AbstractDataSearch.searchDataByParams(filters, BaseConstants.USER_TYPE);

        AggregateIterable<Document> iterator = this.database.getCollection(BaseConstants.USERS_COLLECTION).aggregate(filters);
        ArrayList<Document> usersList = new ArrayList();
        iterator.into(usersList);
        users = new ArrayList<>();
//        this.users.add(new CustomUserObject(new ObjectId(), "admin", "admin", "ADMIN,USER", new JSONObject()));
        usersList.forEach((document) -> {
           JSONObject userData = new JSONObject(new JsonParser().parse(document.toJson()).getAsJsonObject().toString());
           userListCacheData.put((ObjectId) document.get(BaseConstants.DOCUMENT_ID), userData);
           this.users.add(new CustomUserObject((ObjectId) document.get(BaseConstants.DOCUMENT_ID), (String) document.get(BaseConstants.USER_ID), (String) document.get(BaseConstants.USER_PASS), (String) document.get(BaseConstants.USER_ROLES), userData));
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomUserObject> user = users.stream()
                .filter(u -> u.name.equals(username))
                .findAny();
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found by name: " + username);
        }
        return toUserDetails(user.get());
    }

    @Override
    public CustomUserObject loadUserById(ObjectId id) throws UsernameNotFoundException {
        Optional<CustomUserObject> user = users.stream()
                .filter(u -> u.userId.equals(id))
                .findAny();
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found by id: " + id);
        }
        return user.get();
    }

    @Override
    public CustomUserObject loadUserDataByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomUserObject> user = users.stream()
                .filter(u -> u.name.equals(username))
                .findAny();
        return user.get();
    }

    private UserDetails toUserDetails(CustomUserObject userObject) {
        return User.withUsername(userObject.name)
                .password(userObject.password)
                .roles(userObject.roles.split(",")).build();
    }

    public JSONObject getUserDataById(ObjectId userId){
        return userListCacheData.get(userId);
    }

}
