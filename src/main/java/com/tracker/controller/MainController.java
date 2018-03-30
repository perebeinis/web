package com.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
public class MainController {

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/")
    public String root(Locale locale) {
        return "redirect:/search?filter=news";
    }

    @RequestMapping("/login")
    public String login(Locale locale, ModelMap model) {
        return "login";
    }


    @RequestMapping("/login-error")
    public String loginError(ModelMap model, Locale locale) {
        model.addAttribute("tabTitle", messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale));
        model.addAttribute("signInTitle", messageSource.getMessage("loginMsg.title", new Object[]{""}, locale));
        model.addAttribute("placeholderUserName", messageSource.getMessage("placeholderUserName", new Object[]{""}, locale));
        model.addAttribute("placeholderPassword", messageSource.getMessage("placeholderPassword", new Object[]{""}, locale));
        model.addAttribute("signIn", messageSource.getMessage("signIn", new Object[]{""}, locale));
        model.addAttribute("loginError", true);
        return "login";
    }


    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }


}
