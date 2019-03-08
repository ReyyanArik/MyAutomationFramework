package com.automationProject.util;

import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

public class SendEmail {
	private MailConfiguration mailConfiguration;
	private static final String SUBJECT = "Automation Report Subject";
	private static final String BODY = "Detailed report can be found at the link below: ";
	private static final String BR = "</br>";
	private static final String USER_DIR = System.getProperty("user.dir");
	private static final String filename = USER_DIR + "/test-output/report.rar";
	private static final String URL = "url";

	protected static final Logger log = Logger.getLogger(SendEmail.class);

	public void sendMail(String html) throws EmailException {
		loadProperties();

		String to = mailConfiguration.getTO();
		String from = mailConfiguration.getFROM();
		String host = mailConfiguration.getServerIP();

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, to);
			message.setSubject(SUBJECT);

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(BODY + BR + URL + BR + BR + html, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

//			messageBodyPart = new MimeBodyPart();
//			
//			DataSource source = new FileDataSource(filename);
//			messageBodyPart.setDataHandler(new DataHandler(source));
//			messageBodyPart.setFileName(new File(filename).getName());
//			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);
			Transport.send(message);
		} catch (MessagingException mex) {
			log.info(mex);
		}
	}

	private void loadProperties() {

		Properties config = Utilities.getProperty("properties/mail.properties");
		mailConfiguration = new MailConfiguration();
		mailConfiguration.setServerIP(config.getProperty(MailPropertyType.SERVER_IP.getValue()));
		mailConfiguration.setFROM(config.getProperty(MailPropertyType.MAIL_FROM.getValue()));
		mailConfiguration.setTO(config.getProperty(MailPropertyType.MAIL_TO.getValue()));
	}

}
