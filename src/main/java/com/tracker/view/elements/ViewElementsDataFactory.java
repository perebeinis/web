package com.tracker.view.elements;

import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.view.elements.impl.DefaultViewElementsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

public class ViewElementsDataFactory {

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected MongoDatabase database;

    @Autowired
    protected Properties pathsConfigProperties;


    final static Map<String, Supplier<ViewElementsData>> map = new HashMap<>();
    static {
        map.put(BaseConstants.DEFAULT, DefaultViewElementsData::getInstance);
    }

    public ModelMap getViewData(String elementType, ModelMap model, String elementId){
        Supplier<ViewElementsData> element = map.get(elementType);
        if(element != null) {
            return element.get().getData(model, elementId, elementType);
        }else{
            return map.get(BaseConstants.DEFAULT).get().getData(model, elementId, elementType);
        }
    }

    public ViewElementsData getDefaultViewElementData(){
        return map.get(BaseConstants.DEFAULT).get();
    }

    public ViewElementsData getViewElementData(String elementType){
        Supplier<ViewElementsData> element = map.get(elementType);
        if(element != null) {
            return element.get();
        }else{
            return map.get(BaseConstants.DEFAULT).get();
        }
    }
}
