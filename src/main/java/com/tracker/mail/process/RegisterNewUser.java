package com.tracker.mail.process;

import com.tracker.mail.send.impl.GmailSender;
import org.springframework.ui.ModelMap;

/**
 * Created by Perebeinis on 18.04.2018.
 */
public class RegisterNewUser {
    public ModelMap registerNewUser(ModelMap model){
        new GmailSender().sendMail("TEST_THEME", "lkajshdl ahslkdh jalsk jhdalsd","oleksandr.perebeinis@gmail.com");

        return model;
    }
}
