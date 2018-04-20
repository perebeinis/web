package com.tracker.mail.send.impl;

import com.tracker.mail.send.MailSender;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Perebeinis on 18.04.2018.
 */
public class GmailSender implements MailSender {
    private static final String emailPort = "587";
    private static final String smtpAuth = "true";
    private static final String starttls = "true";
    private static final String emailHost = "smtp.gmail.com";

    private static final String fromEmail = "sanok.perebeynis@gmail.com";
    private static final String fromPassword = "sanok16011993";
    private static final String SMTP = "smtp";
    private static final String SMTP_PORT = "mail.smtp.port";
    private static final String SMTP_AUTH = "mail.smtp.auth";
    private static final String SMTP_START_TTLS = "mail.smtp.starttls.enable";
    private static final String HTML_ENCODING = "text/html";

    @Override
    public boolean sendMail(String theme, String text, String toEmail) {
        Properties emailProperties = System.getProperties();
        emailProperties.put(SMTP_PORT, emailPort);
        emailProperties.put(SMTP_AUTH, smtpAuth);
        emailProperties.put(SMTP_START_TTLS, starttls);


        Session mailSession = Session.getDefaultInstance(emailProperties, null);
        MimeMessage emailMessage = new MimeMessage(mailSession);
        try {
            emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            emailMessage.setSubject(theme);
            emailMessage.setContent(text, HTML_ENCODING);

            Transport transport = mailSession.getTransport(SMTP);
            transport.connect(emailHost, fromEmail, fromPassword);
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
            return true;

        }catch (Exception e){
            System.out.println("Error = "+e);
        }
        return false;
    }
}
