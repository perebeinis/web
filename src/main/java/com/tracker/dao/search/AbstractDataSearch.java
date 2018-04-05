package com.tracker.dao.search;

import com.google.gson.JsonParser;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.tracker.cards.CardDataProcessor;
import com.tracker.config.security.authentification.CustomUserObject;
import com.tracker.config.security.authentification.impl.UserDetailsServiceImpl;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.elements.impl.UserAssocDataElement;
import com.tracker.dao.search.impl.DefaultSearcher;
import com.tracker.dao.search.request.CreateRequestQuery;
import com.tracker.dao.search.request.impl.*;
import com.tracker.dao.search.response.SearchResponseElement;
import com.tracker.dao.search.response.impl.DefaultDataResponseElement;
import com.tracker.dao.search.response.impl.UsersAssocResponseElement;
import org.apache.log4j.helpers.ISO8601DateFormat;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public abstract class AbstractDataSearch {
    public static final String userCollection = "userdetails";
    public static final String startConst = "start";
    public static final String lengthConst = "length";
    public static final String drawConst = "draw";
    public static final String orderConst = "order";
    public static final String recordsTotalConst = "recordsTotal";
    public static final String recordsFilteredConst = "recordsFiltered";
    public static final String dataConst = "data";
    public static final String search = "search";
    public static final String name = "name";
    public static final String searchDataConst = "searchData";

    final static Map<String, Supplier<CreateRequestQuery>> requestQueryForGetCardMap = new HashMap<>();
    final static Map<String, Supplier<CreateRequestQuery>> requestQueryForSearchMap = new HashMap<>();
    final static Map<String, Supplier<SearchResponseElement>> responseQueryForSearchMap = new HashMap<>();

    static {
        requestQueryForGetCardMap.put(BaseConstants.FILE, CreateRequestQueryFile::new);
        requestQueryForGetCardMap.put(BaseConstants.TEXT, CreateRequestQueryText::new);
        requestQueryForGetCardMap.put(BaseConstants.CURRENT_EXECUTOR, CreateRequestQueryAddressee::new);
        requestQueryForGetCardMap.put(BaseConstants.USER_ASSOC, CreateRequestQueryUserAssoc::new);
        requestQueryForGetCardMap.put(BaseConstants.AUDIT_DATA, CreateRequestQueryAuditData::new);
        requestQueryForGetCardMap.put(BaseConstants.COMMENTS, CommentsAssocQuery::new);
        requestQueryForGetCardMap.put(BaseConstants.CREATOR, CreateRequestQueryUserAssoc::new);

        requestQueryForSearchMap.put(BaseConstants.FILE, CreateRequestQueryFile::new);
        requestQueryForSearchMap.put(BaseConstants.TEXT, CreateRequestQueryText::new);
        requestQueryForSearchMap.put(BaseConstants.USER_ASSOC, CreateRequestQueryUserAssoc::new);
        requestQueryForSearchMap.put(BaseConstants.CREATOR, CreateRequestQueryUserAssoc::new);
        requestQueryForSearchMap.put(BaseConstants.AUDIT_DATA, CreateRequestQueryAuditData::new);
        requestQueryForSearchMap.put(BaseConstants.CURRENT_EXECUTOR, CurrentExecutorQuery::new);
        requestQueryForSearchMap.put(BaseConstants.SEARCH_TEXT_QUERY, SearchTextQuery::new);

        responseQueryForSearchMap.put(BaseConstants.CREATOR, UsersAssocResponseElement::new);
        responseQueryForSearchMap.put(BaseConstants.USER_ASSOC, UsersAssocResponseElement::new);
        responseQueryForSearchMap.put(BaseConstants.DEFAULT, DefaultDataResponseElement::new);
    }

    public JSONObject getData(MongoDatabase mongoDatabase, JSONObject searchParams) {
        Integer start = (Integer) searchParams.get(startConst);
        Integer length = (Integer) searchParams.get(lengthConst);
        Integer draw = (Integer) searchParams.get(drawConst);

        JSONObject searchData = (JSONObject) ((JSONObject) searchParams.get(search)).get(searchDataConst);
        String searchType = (String) ((JSONObject) searchParams.get(search)).get(BaseConstants.SEARCH_TYPE);
        Iterator<?> keys = searchData.keys();

        List<Bson> filters = new ArrayList<>();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = (String) searchData.get(key);
            if (!StringUtils.isEmpty(value)) {
                JSONObject fieldData = getFieldData(searchType, key);
                Supplier<CreateRequestQuery> element = requestQueryForSearchMap.get(key);
                if (element != null) {
                    Bson result = element.get().createQueryForElement(key, value);
                    if (result != null) filters.add(result);
                } else {
                    Bson result = requestQueryForSearchMap.get(BaseConstants.SEARCH_TEXT_QUERY).get().createQueryForElement(key, value);
                    if (result != null) filters.add(result);
                }
            }
        }

        MongoCollection<Document> collection = mongoDatabase.getCollection(BaseConstants.getCollection(searchType));
        AtomicInteger counterAllDocs = new AtomicInteger(0);
        collection.aggregate(filters).iterator().forEachRemaining(
                key -> counterAllDocs.incrementAndGet()
        );

        List<String> columns = new ArrayList<>();
        if (searchParams.has(BaseConstants.COLUMNS)) {
            JSONArray columnsList = (JSONArray) searchParams.get(BaseConstants.COLUMNS);
            for (Object object : columnsList) {
                JSONObject data = (JSONObject) object;
                if (((String) data.get(BaseConstants.DATA)).contains(BaseConstants.ASSOC)) {
                    filters.add(new CreateRequestQueryUserAssoc().createQueryForElement((String) data.get(BaseConstants.DATA), ""));
                }
                columns.add((String) data.get(BaseConstants.DATA));
            }

            JSONArray orderArray = (JSONArray) searchParams.get(BaseConstants.ORDER);
            for (Object object : orderArray) {
                JSONObject data = (JSONObject) object;
                int sort = data.get(BaseConstants.DIR).equals(BaseConstants.ASC) ? 1 : -1;
                filters.add(new AggregateAdditionalParameterDataQuery().createQueryForElement(BaseConstants.SORT, new Document(columns.get((Integer) data.get(BaseConstants.COLUMN)), sort)));
                break;
            }
        }


        filters.add(new AggregateAdditionalParameterDataQuery().createQueryForElement(BaseConstants.SKIP, start));
        filters.add(new AggregateAdditionalParameterDataQuery().createQueryForElement(BaseConstants.LIMIT, length));

        AggregateIterable<Document> iterator = collection.aggregate(filters);
        ArrayList<Document> documents = new ArrayList();
        iterator.into(documents);

        JSONArray jsonArray = new JSONArray();
        for (Document document : documents) {
            JSONObject jsonObject = new JSONObject(new JsonParser().parse(document.toJson()).getAsJsonObject().toString());
            Iterator<?> objectKeys = jsonObject.keys();
            JSONObject result = jsonObject;
            if (columns.size() > 0) {
                while (objectKeys.hasNext()) {
                    String key = (String) objectKeys.next();
                    Object value = jsonObject.get(key);
                    if (columns.contains(key)) {
                        Supplier<SearchResponseElement> element = responseQueryForSearchMap.get(key);
                        boolean foundDate = value instanceof JSONObject && ((JSONObject) value).get(BaseConstants.DATE) != null;
                        if (foundDate) {
                            Date date = new Date((Long) ((JSONObject) value).get(BaseConstants.DATE));
                            result.putOpt(key, ISO8601DateFormat.getDateTimeInstance().format(date));
                        } else if (element != null) {
                            result.putOpt(key, element.get().getData(value));
                        } else {
                            result.putOpt(key, responseQueryForSearchMap.get(BaseConstants.DEFAULT).get().getData(value));
                        }
                    }
                }
            }
            jsonArray.put(result);
        }

        JSONObject result = new JSONObject();
        result.put(drawConst, draw);
        result.put(recordsTotalConst, counterAllDocs.get());
        result.put(recordsFilteredConst, counterAllDocs.get());
        result.put(dataConst, jsonArray);

        return result;

    }

    public JSONObject getDataById(MongoDatabase mongoDatabase, String elementType, String elementId) {
        JSONObject result = new JSONObject();
        MongoCollection<Document> collection = mongoDatabase.getCollection(BaseConstants.getCollection(elementType));

        Bson match = new Document(BaseConstants.MATCH, new Document(BaseConstants.DOCUMENT_ID, new ObjectId(elementId)));

        List<Bson> filters = new ArrayList<>();
        filters.add(match);
        filters = searchDataByParams(filters, elementType);

        AggregateIterable<Document> iterator = collection.aggregate(filters);
        ArrayList<Document> documents = new ArrayList();
        iterator.into(documents);

        if (documents.size() > 0) {

            for (Document document : documents) {
                JSONObject jsonObject = new JSONObject(new JsonParser().parse(document.toJson()).getAsJsonObject().toString());
                Iterator<?> objectKeys = jsonObject.keys();
                while (objectKeys.hasNext()) {
                    String key = (String) objectKeys.next();
                    Object value = jsonObject.get(key);
                    result.put(key, value);
                }
            }

        }
        return result;
    }


    public JSONObject updateDataById(MongoDatabase mongoDatabase, String elementType, String elementId, JSONObject dataForUpdate) {
        JSONObject result = new JSONObject();
        MongoCollection<Document> collection = mongoDatabase.getCollection(BaseConstants.getCollection(elementType));
        Bson setData = new Document(BaseConstants.SET, createUpdateData(dataForUpdate));
        UpdateResult updateResult = collection.updateOne(new Document(BaseConstants.DOCUMENT_ID, new ObjectId(elementId)), setData);
        System.out.println("data updated");
        return result;
    }



    private BasicDBObject createUpdateData(JSONObject updateData) {
        BasicDBObject updatingDataObject = new BasicDBObject();
        Iterator<?> keys = updateData.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            updatingDataObject.put(key, updateData.get(key));
        }
        return updatingDataObject;
    }

    public static List<Bson> searchDataByParams(List<Bson> filters, String elementType) {
        CardDataProcessor.getInstance().getCardDataForElementType(elementType);
        JSONArray jsonArray = CardDataProcessor.getInstance().getCardAttributes(elementType);
        for (Object object : jsonArray) {
            JSONObject cardData = (JSONObject) object;
            Supplier<CreateRequestQuery> element = requestQueryForGetCardMap.get(cardData.get(BaseConstants.TYPES_FOR_SAVING));
            if (element != null) {
                Bson result = element.get().createQueryForElement((String) cardData.get(name), "");
                if (result != null) filters.add(result);
            }
        }
        return filters;
    }

    private JSONObject getFieldData(String elementType, String fieldName) {
        JSONArray jsonArray = CardDataProcessor.getInstance().getCardAttributes(elementType);
        JSONObject result = new JSONObject();
        for (Object object : jsonArray) {
            JSONObject fieldElement = (JSONObject) object;
            String name = (String) fieldElement.get(BaseConstants.NAME);
            if (name.equals(fieldName)) {
                result = fieldElement;
                break;
            }
        }
        return result;

    }

}
