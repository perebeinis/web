package com.tracker.config.security.authentification;

import com.tracker.constants.BaseConstants;
import org.bson.types.ObjectId;
import org.json.JSONObject;

public class CustomUserObject {
    private JSONObject allUserData;
    public String name;
    public ObjectId userId;
    public String password;
    public String roles;

    public CustomUserObject(ObjectId userId, String name, String password, String roles, JSONObject allUserData) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.roles = roles;
        this.allUserData = allUserData;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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

    public String getAllUserFullName() {
        return allUserData.get(BaseConstants.LAST_NAME) + " "+allUserData.get(BaseConstants.FIRST_NAME);
    }

    public void setAllUserData(JSONObject allUserData) {
        this.allUserData = allUserData;
    }
}
