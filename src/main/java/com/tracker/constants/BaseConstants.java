package com.tracker.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Perebeinis on 10.01.2018.
 */
public interface BaseConstants {
    public static String USERS_COLLECTION = "userdetails";
    public static String AUDIT = "audit";
    public static String MESSAGE = "message";
    public static String ISSUE_COLLECTION = "tasks";
    public static String ISSUE_CONSTANT = "issue";
    public static String USER_TYPE = "user";
    public static String DOCUMENTS_COLLECTION = "documents";
    public static String DOCUMENT_ID = "_id";
    public static String CURRENT_EXECUTOR = "currentExecutor";
    public static String SEARCH_TEXT_QUERY = "searchTextQuery";
    public static String MY_ID = "MY_ID";
    public static String IN = "$in";
    public static String LOOKUP = "$lookup";
    public static String ELEM_MATCH = "$elemMatch";
    public static String FROM = "from";
    public static String ROOT = "root";
    public static String MONGO_ID = "$oid";
    public static String SKIP = "$skip";
    public static String LIMIT = "$limit";
    public static String MATCH = "$match";
    public static String SET = "$set";
    public static String PUSH = "$push";
    public static String REGEX = "$regex";
    public static String CREATOR = "creatorAssoc";
    public static String CREATOR_USER = "creator";
    public static String ORDER_BY = "$orderby";
    public static String ORDER = "order";
    public static String ASC = "asc";
    public static String COLUMN = "column";
    public static String COLUMNS = "columns";
    public static String DIR = "dir";
    public static String SORT = "$sort";
    public static String OPTIONS = "$options";
    public static String AS = "as";
    public static String CUSTOM_USER_DETAILS_SERVICE = "customUserDetailsService";
    public static String LOCAL_FIELD = "localField";
    public static String FOREIGN_FIELD = "foreignField";
    public static final String HEADER_ELEMENT_CONSTANT = "header-elements";
    public static final String MENU_ELEMENT_CONSTANT = "menu-elements";
    public static final String CARD_FILED_VALUES = "cardFiledValues";


    /* user constants*/
    public static String USER_ID = "user_id";
    public static String COMMENT_ID = "comment_id";
    public static String FIRST_NAME = "firstName";
    public static String TITLE_FIRST_NAME = "constants.firstName";
    public static String TITLE_LAST_NAME = "constants.lastName";
    public static String TITLE_EMAIL = "constants.email";
    public static String LAST_NAME = "lastName";
    public static String USER_ASSOC = "userAssoc";
    public static String ASSOC = "Assoc";
    public static String USER_PASS = "user_pass";
    public static String USER_ROLES = "roles";
    public static String NAME = "name";
    public static String DEFAULT = "default";
    public static String ISSUE = "issue";
    public static String NEWS = "news";
    public static String COMMENTS = "comments";
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
    public static String CHILD = "child";
    public static String ACTOR = "actor";
    public static String DATABASE = "database";
    public static String FILE = "file";
    public static String SEARCH_TYPE = "searchType";
    public static String USER_DATA = "userData";
    public static String MESSAGES = "messages";
    public static String NEWS_LIST = "newsList";
    public static String MENU_LIST = "menuList";
    public static String HEADER_LIST = "headerList";
    public static String DEFAULT_ENCODING = "UTF-8";
    public static String MODE = "mode";
    public static String MODE_CREATE = "create";
    public static String MODE_VIEW = "view";
    public static String ELEMENT_TYPE = "elementType";
    public static String ELEMENT = "element";
    public static String SEARCHERS = "searchers";
    public static String STATE = "state";
    public static String IN_PROCESS = "inProcess";
    public static String COMPLETE = "complete";
    public static String STRING_TYPE = "string";
    public static String DATE = "$date";
    public static String PARENT = "parent";
    public static String COMMENT_VALUE = "commentValue";
    public static String COMMENT = "comment";
    public static String COMMENT_DOCUMENT_ID = "documentId";
    public static String APP_TITLE = "bug-tracker.title";
    public static String TAB_TITLE = "tabTitle";
    public static String SIGN_IN_TITLE = "signInTitle";
    public static String SIGN_IN = "signIn";
    public static String REGISTER = "register";
    public static String REGISTRATION_TITLE = "registration.title";
    public static String USER_ALREADY_EXIST_IN_SYSTEM = "userAlreadyExistInSystem";
    public static String LOGIN_ERROR = "loginError";
    public static String LOGIN_MSG_TITLE = "loginMsg.title";
    public static String PLACE_HOLDER_USERNAME = "placeholderUserName";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String USER_EMAIL = "user_email";
    public static String EMAIL = "email";
    public static String USER_GROUP = "user_group";
    public static String PLACE_HOLDER_PASS = "placeholderPassword";
    public static String JSON_EXTENSION = ".json";
    public static String HTMl_LINE_BREAK = "<br>";



    public static final Map<String, String> collectionsByType = new HashMap<>();

    /* TASKS*/
    public static String CURRENT_TASK_EXECUTOR = "currentExecutor";


    public static String getCollection(String elementType){
        if(elementType.equals(ISSUE_CONSTANT)){
            return ISSUE_COLLECTION;
        }else if(elementType.equals(MESSAGE)){
            return MESSAGE;
        }else if(elementType.equals(AUDIT)){
            return AUDIT;
        }else if(elementType.equals(USER_TYPE)){
            return USERS_COLLECTION;
        }else {
            return elementType;
        }
    }
}
