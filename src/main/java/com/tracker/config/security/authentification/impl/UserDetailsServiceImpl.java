package com.tracker.config.security.authentification.impl;

import com.google.gson.JsonParser;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.tracker.config.security.authentification.CustomUserObject;;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.AbstractDataSearch;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private MongoDatabase database;
    private static List<CustomUserObject> users = new ArrayList();

    public UserDetailsServiceImpl(MongoDatabase database) {
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
        usersList.forEach((document) -> {
           this.users.add(new CustomUserObject((String) document.get(BaseConstants.USER_ID), (String) document.get(BaseConstants.USER_PASS), (String) document.get(BaseConstants.USER_ROLES), new JSONObject(new JsonParser().parse(document.toJson()).getAsJsonObject().toString())));
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

}
