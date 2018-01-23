package com.tracker.dao.search;

import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.cards.CardDataProcessor;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.impl.DefaultSearcher;
import com.tracker.dao.search.request.CreateRequestQuery;
import com.tracker.dao.search.request.impl.CreateRequestQueryFile;
import com.tracker.dao.search.request.impl.CreateRequestQueryText;
import com.tracker.dao.search.request.impl.CreateRequestQueryUserAssoc;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
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

    final static Map<String, Supplier<CreateRequestQuery>> requestQueryMap = new HashMap<>();
    static {
        requestQueryMap.put("file", CreateRequestQueryFile::new);
        requestQueryMap.put("text", CreateRequestQueryText::new);
        requestQueryMap.put("userAssoc", CreateRequestQueryUserAssoc::new);
    }



    public JSONObject getData(MongoDatabase mongoDatabase, JSONObject searchParams) {
        Integer start = (Integer) searchParams.get(startConst);
        Integer length = (Integer) searchParams.get(lengthConst);
        Integer draw = (Integer) searchParams.get(drawConst);
        ArrayList<Document> docs = new ArrayList();
        JSONObject searchData = (JSONObject) ((JSONObject) searchParams.get(search)).get(searchDataConst);
        String searchType = (String) ((JSONObject) searchParams.get(search)).get("searchType");
        BasicDBObject query = createSearchData(searchData);

        MongoCollection<Document> collection = mongoDatabase.getCollection(BaseConstants.getCollection(searchType));

        FindIterable iteratorAll = collection.find(query);
        iteratorAll.into(docs);
        Integer count = docs.size();
        docs = new ArrayList();

        FindIterable iterator = collection.find(query).skip(start).limit(length);
        iterator.into(docs);

        JSONArray jsonArray = new JSONArray();
        docs.forEach((document) -> {
            jsonArray.put(new JSONObject(new JsonParser().parse(document.toJson()).getAsJsonObject().toString()));
        });


        JSONObject result = new JSONObject();
        result.put(drawConst, draw);
        result.put(recordsTotalConst, count);
        result.put(recordsFilteredConst, count);
        result.put(dataConst, jsonArray);

        System.out.println("finish search users from query "+query);

        return result;
    }

    public JSONObject getDataById(MongoDatabase mongoDatabase, String elementType, String elementId){
        JSONObject result  = new JSONObject();
        MongoCollection<Document> collection = mongoDatabase.getCollection(BaseConstants.getCollection(elementType));

        Bson match = new Document(BaseConstants.MATCH, new Document(BaseConstants.DOCUMENT_ID, new ObjectId(elementId)));

        List<Bson> filters = new ArrayList<>();
        filters.add(match);
        filters = searchDataByParams(filters,elementType);

        AggregateIterable<Document> iterator = collection.aggregate(filters);
        ArrayList<Document> docs = new ArrayList();
        iterator.into(docs);

        if(docs.size()>0){
            result = new JSONObject(new JsonParser().parse(docs.get(0).toJson()).getAsJsonObject().toString());
        }
        return result;
    }


    private BasicDBObject createSearchData(JSONObject searchData){
        BasicDBObject regexQuery = new BasicDBObject();
        Iterator<?> keys = searchData.keys();
        while(keys.hasNext() ) {
            String key = (String)keys.next();
            regexQuery.put(key,new BasicDBObject("$regex", searchData.get(key)+".*")
                    .append("$options", "i"));
        }
        return regexQuery;
    }

    public static List<Bson> searchDataByParams(List<Bson> filters, String elementType){
        CardDataProcessor.getInstance().getCardDataForElementType(elementType);
        JSONArray jsonArray = CardDataProcessor.getInstance().getCardAttributes().get(elementType);
        for (Object object : jsonArray) {
            JSONObject cardData = (JSONObject) object;
            Supplier<CreateRequestQuery> element = requestQueryMap.get(cardData.get(BaseConstants.TYPES_FOR_SAVING));
            if(element != null) {
                Bson result = element.get().createQueryForElement((String) cardData.get(name),"");
                if(result!=null) filters.add(result);
            }
        }
        return filters;
    }

}
