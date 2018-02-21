package com.tracker.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Perebeinis on 10.01.2018.
 */
public interface BaseConstants {
    public static String USERS_COLLECTION = "userdetails";
    public static String AUDIT = "audit";
    public static String ISSUE_COLLECTION = "tasks";
    public static String ISSUE_CONSTANT = "issue";
    public static String USER_TYPE = "user";
    public static String DOCUMENTS_COLLECTION = "documents";
    public static String DOCUMENT_ID = "_id";
    public static String CURRENT_EXECUTOR = "currentExecutor";
    public static String LOOKUP = "$lookup";
    public static String ELEM_MATCH = "$elemMatch";
    public static String FROM = "from";
    public static String MONGO_ID = "$oid";
    public static String MATCH = "$match";
    public static String SET = "$set";
    public static String REGEX = "$regex";
    public static String ORDER_BY = "$orderby";
    public static String OPTIONS = "$options";
    public static String AS = "as";
    public static String CUSTOM_USER_DETAILS_SERVICE = "customUserDetailsService";
    public static String LOCAL_FIELD = "localField";
    public static String FOREIGN_FIELD = "foreignField";
    public static final String HEADER_ELEMENT_CONSTANT = "header-element";
    public static final String MENU_ELEMENT_CONSTANT = "menu-element";

    /* user constants*/
    public static String USER_ID = "user_id";
    public static String FIRST_NAME = "firstName";
    public static String LAST_NAME = "lastName";
    public static String USER_ASSOC= "userAssoc";
    public static String USER_PASS = "user_pass";
    public static String USER_ROLES = "roles";
    public static String NAME = "name";
    public static String DEFAULT = "default";
    public static String TEXT = "text";
    public static String TYPE = "type";
    public static String AUDIT_ELEMENT_ID = "auditElementId";
    public static String DATA = "data";
    public static String AUDIT_DATA = "auditData";
    public static String TIME = "time";
    public static String FILE_NAME = "fileName";
    public static String TYPES_FOR_SAVING = "typeForSaving";
    public static String ACTION_NAME = "actionName";
    public static String CREATE = "create";
    public static String UPDATE = "update";
    public static String CREATED = "created";
    public static String ACTOR = "actor";
    public static String DATABASE = "database";

    public static final Map<String, String> collectionsByType = new HashMap<>();

    /* TASKS*/
    public static String CURRENT_TASK_EXECUTOR = "currentExecutor";

    public static String getCollection(String elementType){
        if(elementType.equals(ISSUE_CONSTANT)){
            return ISSUE_COLLECTION;
        }else if(elementType.equals(AUDIT)){
            return AUDIT;
        }else {
            return USERS_COLLECTION;
        }
    }
}
