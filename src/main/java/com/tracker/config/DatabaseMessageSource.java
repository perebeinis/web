package com.tracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DatabaseMessageSource implements MessageSource {

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return resolveMessage(code, args, locale);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return resolveMessage(code, args, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        for (String code : resolvable.getCodes()) {
            String message = resolveMessage(code, resolvable.getArguments(), locale);
            if (message != null) {
                return message;
            }
        }
        return null;
    }

    private String resolveMessage(String code, Object[] args, Locale locale) {
        return ""; // TODO Load message from database...
    }
}

