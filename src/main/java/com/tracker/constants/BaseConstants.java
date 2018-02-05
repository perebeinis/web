package com.tracker.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Perebeinis on 10.01.2018.
 */
public interface BaseConstants {
    public static String USERS_COLLECTION = "userdetails";
    public static String ISSUE_COLLECTION = "tasks";
    public static String ISSUE_CONSTANT = "issue";
    public static String USER_TYPE = "user";
    public static String DOCUMENTS_COLLECTION = "documents";
    public static String DOCUMENT_ID = "_id";
    public static String CURRENT_EXECUTOR = "currentExecutor";
    public static String LOOKUP = "$lookup";
    public static String FROM = "from";
    public static String MATCH = "$match";
    public static String SET = "$set";
    public static String REGEX = "$regex";
    public static String OPTIONS = "$options";
    public static String AS = "as";
    public static String LOCAL_FIELD = "localField";
    public static String FOREIGN_FIELD = "foreignField";

    /* user constants*/
    public static String USER_ID = "user_id";
    public static String USER_ASSOC= "userAssoc";
    public static String USER_PASS = "user_pass";
    public static String USER_ROLES = "roles";
    public static String TYPES_FOR_SAVING = "typeForSaving";

    public static final Map<String, String> collectionsByType = new HashMap<>();

    /* TASKS*/
    public static String CURRENT_TASK_EXECUTOR = "currentExecutor";

    public static String getCollection(String elementType){
        if(elementType.equals("issue")){
            return ISSUE_COLLECTION;
        }else {
            return USERS_COLLECTION;
        }
    }
}
