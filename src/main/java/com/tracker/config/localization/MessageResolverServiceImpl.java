package com.tracker.config.localization;

import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class MessageResolverServiceImpl implements MessageResolveService{
//    private static final Logger LOGGER = Logger.getLogger(MessageResolverServiceImpl.class);


    public JSONObject getMessages(MessageSource messageSource, Locale locale){
        Properties properties =  ((ExposedResourceMessageBundleSource) messageSource).getMessages(locale);
        JSONObject jsonObject= new JSONObject();
        for(Map.Entry<Object,Object> entry: properties.entrySet()){
            if(entry.getKey() != null && entry.getValue() != null) {
                jsonObject.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return jsonObject;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {

    }
}
