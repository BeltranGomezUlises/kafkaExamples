/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaconsumer.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class UtilsMail {

    public static void sendMail(Properties props, String msg, String toMail) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName(props.getProperty("mail.host"));
        email.setSmtpPort(Integer.valueOf(props.getProperty("mail.port")));
        email.setAuthenticator(new DefaultAuthenticator(props.getProperty("mail.userMail"), props.getProperty("mail.userPass")));
        email.setSSL(true);
        email.setFrom(props.getProperty("mail.userMail"), props.getProperty("mail.userNameFrom"));
        email.setSubject(props.getProperty("mail.subject"));
        email.setMsg(msg);
        email.addTo(toMail);
        email.send();
    }

    public static void sendHtmlMail(Properties props, String htmlMail, String toMail) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(props.getProperty("mail.host"));
        email.setSmtpPort(Integer.valueOf(props.getProperty("mail.port")));
        email.setAuthenticator(new DefaultAuthenticator(props.getProperty("mail.userMail"), props.getProperty("mail.userPass")));
        email.setSSL(true);
        email.setFrom(props.getProperty("mail.userMail"), props.getProperty("mail.userNameFrom"));
        email.setSubject(props.getProperty("mail.subject"));
        email.addTo(toMail);
        email.setHtmlMsg(htmlMail);
        email.send();
    }

    public static void testSendMail() throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("usuariosexpertos@gmail.com", "90Y9B8yh$123"));
        email.setSSL(true);
        email.setFrom("ubg700@gmail.com", "Persona del correo");
        email.setSubject("TestMail");
        email.setMsg("This is a test mail ... :-)");
        email.addTo("beltrangomezulises@gmail.com");
        email.send();
    }

    public static void testSendHTMLMail() throws EmailException, MalformedURLException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthentication("usuariosexpertos@gmail.com", "90Y9B8yh$123");
        email.setSSL(true);
        email.setFrom("ubg700@gmail.com", "persona del correo");
        email.setSubject("Test email with inline image");
        email.addTo("beltrangomezulises@gmail.com", "test");
        // embed the image and get the content id
        URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
        String cid = email.embed(url, "Apache logo");
        // set the html message
        email.setHtmlMsg("<html>The apache logo - <img src=\"cid:" + cid + "\"></html>");
        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");
        // send the email
        email.send();
    }

}
