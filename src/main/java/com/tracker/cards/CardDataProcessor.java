package com.tracker.cards;

import com.mongodb.client.MongoDatabase;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.*;

public class CardDataProcessor {

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected FrontElementConfigurationParser frontElementConfigurationParser;

    @Autowired
    protected MongoDatabase database;

    @Autowired
    protected Properties pathsConfigProperties;

    private static final String SET_CONSTANT = "set";
    private static final String TITLE_CONSTANT = "title";
    private static final String NAME_CONSTANT = "name";
    private static final String TYPE_CONSTANT = "type";
    private static final String BUTTONS_CONSTANT = "buttons";
    private static final String CUSTOM_CLASS_NAME_CONSTANT = "customClassName";
    private static final String TYPE_FOR_SAVING = "typeForSaving";
    private static final String MANDATORY_CONDITIONS_CONSTANT = "mandatoryCondition";
    private static Map<String,JSONArray> cardAttributes = new HashMap<>();
    private static Map<String,JSONObject> cardElements = new HashMap<>();
    private static Map<String,Boolean> filterHasAssoc = new HashMap<>();

    private static final ArrayList<String> assocElements = new ArrayList<>();
    static {
        assocElements.add("file");
    }

    private static CardDataProcessor instance;

    public static CardDataProcessor getInstance() {
        if (instance == null) {
            synchronized (CardDataProcessor.class) {
                if (instance == null) {
                    instance = new CardDataProcessor();
                }
            }
        }
        return instance;
    }

    public CardDataProcessor() {
        createCardData();
    }

    public JSONArray  parseData(Document document, String parentTagName, String elementType){
        JSONArray setList = new JSONArray();
        NodeList nodeList = document.getElementsByTagName(parentTagName);
        for (int i = 0; i < nodeList.getLength(); i++){
            Node setElementNode = nodeList.item(i);
            String setName = setElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
            String setTitle = setElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
            JSONObject jsonSetObject = new JSONObject();
            jsonSetObject.put(NAME_CONSTANT, setName );
            jsonSetObject.put(TITLE_CONSTANT, setTitle);


            NodeList nodeAttributesList = setElementNode.getChildNodes();
            JSONArray attributeList = new JSONArray();
            for (int j = 0; j < nodeAttributesList.getLength(); j++){
                Node attributeElementNode = nodeAttributesList.item(j);
                if(attributeElementNode.getAttributes()!=null){
                    String elementName = attributeElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                    String title = attributeElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
                    String type = attributeElementNode.getAttributes().getNamedItem(TYPE_CONSTANT).getTextContent();
                    String typeForSaving = attributeElementNode.getAttributes().getNamedItem(TYPE_FOR_SAVING)!=null ? attributeElementNode.getAttributes().getNamedItem(TYPE_FOR_SAVING).getTextContent() : "text";
                    String className = attributeElementNode.getAttributes().getNamedItem(CUSTOM_CLASS_NAME_CONSTANT).getTextContent();
                    String mandatoryCondition = attributeElementNode.getAttributes().getNamedItem(MANDATORY_CONDITIONS_CONSTANT)!=null ? attributeElementNode.getAttributes().getNamedItem(MANDATORY_CONDITIONS_CONSTANT).getTextContent() :"";

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(NAME_CONSTANT,elementName);
                    jsonObject.put(TITLE_CONSTANT,title);
                    jsonObject.put(TYPE_CONSTANT,type);
                    jsonObject.put(TYPE_FOR_SAVING, typeForSaving);
                    jsonObject.put(CUSTOM_CLASS_NAME_CONSTANT,className);
                    jsonObject.put(MANDATORY_CONDITIONS_CONSTANT,mandatoryCondition);
                    attributeList.put(jsonObject);


                    if(assocElements.contains(typeForSaving)){
                        filterHasAssoc.put(elementType, true);
                    }
                }
            }

            if(parentTagName.equals(SET_CONSTANT)){
                if(cardAttributes.get(elementType) == null) {
                    JSONArray attributeListCopy = new JSONArray(attributeList.toString());
                    cardAttributes.put(elementType,attributeListCopy);
                }else{
                    JSONArray attributeListCopy = new JSONArray(attributeList.toString());
                    for (Object jsonObject : attributeListCopy) {
                        cardAttributes.get(elementType).put(jsonObject);
                    }

                }
            }

            jsonSetObject.put(parentTagName, attributeList);
            setList.put(jsonSetObject);
        }

        return setList;
    }


    public  JSONObject createCardData() {
        JSONObject jsonObject = new JSONObject();
        if (pathsConfigProperties != null) {
            try {
                Enumeration propertyNames = pathsConfigProperties.propertyNames();

                while (propertyNames.hasMoreElements()) {
                    String key = (String) propertyNames.nextElement();
                    InputStream inputStream = this.getClass().getResourceAsStream(pathsConfigProperties.getProperty(key));
                    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document document = documentBuilder.parse(inputStream);
                    document.getDocumentElement().normalize();

                    JSONArray setList = parseData(document, SET_CONSTANT, key);
                    JSONArray buttons = parseData(document, BUTTONS_CONSTANT, key);

                    jsonObject.put(SET_CONSTANT, setList);
                    jsonObject.put(BUTTONS_CONSTANT, buttons);
                    cardElements.put(key, jsonObject);
                }

                return jsonObject;

            } catch(Exception e){
                e.printStackTrace();
            }
        }


        return jsonObject;
    }

    public JSONObject getCardDataForElementType(String elementType){
        if(cardElements.size() ==0 ){
            synchronized (CardDataProcessor.class) {
                if (cardElements.size() ==0 ) {
                    createCardData();
                }
            }
        }
        return cardElements.get(elementType);
    }

    public Map<String, JSONObject> getCardElements() {
        return cardElements;
    }

    public Map<String, JSONArray> getCardAttributes() {
        return cardAttributes;
    }
}
