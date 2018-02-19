package com.tracker.config.localization;

import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.util.Locale;
import java.util.Map;

public interface MessageResolveService extends MessageSourceAware {
    JSONObject getMessages(MessageSource messageSource, Locale locale);
}
