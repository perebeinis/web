package com.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.unbescape.html.HtmlEscape;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Application home page and login.
 */
@Controller
public class MainController {

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/")
    public String root(Locale locale) {
        return "redirect:/welcome";
    }

    /** Home page. */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String printWelcome(Locale locale, ModelMap model) {
        String welcome = messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale);
        model.addAttribute("title", welcome);
        String loginMsg = messageSource.getMessage("loginMsg.title", new Object[]{""}, locale);
        model.addAttribute("loginMsg", loginMsg);
        return "welcome";
    }

    /** Home page. */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /** User zone index. */
    @RequestMapping("/user/index")
    public String userIndex() {
        return "user/index";
    }

    /** Administration zone index. */
    @RequestMapping("/admin/index")
    public String adminIndex() {
        return "admin/index";
    }

    /** Shared zone index. */
    @RequestMapping("/shared/index")
    public String sharedIndex() {
        return "shared/index";
    }

    /** Login form. */
    @RequestMapping("/login")
    public String login(Locale locale, ModelMap model) {
        model.addAttribute("tabTitle", messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale));
        model.addAttribute("signInTitle", messageSource.getMessage("loginMsg.title", new Object[]{""}, locale));
        model.addAttribute("placeholderUserName", messageSource.getMessage("placeholderUserName", new Object[]{""}, locale));
        model.addAttribute("placeholderPassword", messageSource.getMessage("placeholderPassword", new Object[]{""}, locale));
        model.addAttribute("signIn", messageSource.getMessage("signIn", new Object[]{""}, locale));
        return "login";
    }

    /** Login form with error. */
    @RequestMapping("/login-error")
    public String loginError(ModelMap model,Locale locale) {
        model.addAttribute("tabTitle", messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale));
        model.addAttribute("signInTitle", messageSource.getMessage("loginMsg.title", new Object[]{""}, locale));
        model.addAttribute("placeholderUserName", messageSource.getMessage("placeholderUserName", new Object[]{""}, locale));
        model.addAttribute("placeholderPassword", messageSource.getMessage("placeholderPassword", new Object[]{""}, locale));
        model.addAttribute("signIn", messageSource.getMessage("signIn", new Object[]{""}, locale));
        model.addAttribute("loginError", true);
        return "login";
    }

    /** Simulation of an exception. */
    @RequestMapping("/simulateError")
    public void simulateError() {
        throw new RuntimeException("This is a simulated error message");
    }

    /** Error page. */
    @RequestMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        model.addAttribute("errorCode", "Error " + request.getAttribute("javax.servlet.error.status_code"));
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<ul>");
        while (throwable != null) {
            errorMessage.append("<li>").append(HtmlEscape.escapeHtml5(throwable.getMessage())).append("</li>");
            throwable = throwable.getCause();
        }
        errorMessage.append("</ul>");
        model.addAttribute("errorMessage", errorMessage.toString());
        return "error";
    }

    /** Error page. */
    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }


}
