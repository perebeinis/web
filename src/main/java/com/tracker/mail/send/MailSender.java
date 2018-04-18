package com.tracker.mail.send;

/**
 * Created by Perebeinis on 17.04.2018.
 */
public interface MailSender{
    boolean sendMail(String theme, String text, String toEmail);
}
