package com.tracker.config.security.authentification;

import org.json.JSONObject;

public class CustomUserObject {
    private JSONObject allUserData;
    public String name;
    public String password;
    public String roles;

    public CustomUserObject(String name, String password, String roles, JSONObject allUserData) {
        this.name = name;
        this.password = password;
        this.roles = roles;
        this.allUserData = allUserData;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return roles;
    }

    public void setRole(String role) {
        this.roles = role;
    }

    public JSONObject getAllUserData() {
        return allUserData;
    }

    public void setAllUserData(JSONObject allUserData) {
        this.allUserData = allUserData;
    }
}
