package predictor;

import java.util.Calendar;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	private static final String user = "fluleap";
	private static final String pass = "bicfluleap";

	static void send(String to, String name, String email, String subject,
			String msg) throws AddressException, MessagingException {

		// create an instance of Properties class
		Properties prop = new Properties();

		// Specifies the IP address of your default mail server
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587"); // optional
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");

		// Pass properties object and authenticator object for authentication to
		// session instance
		Session session = Session.getInstance(prop,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(user, pass);
					}
				});

		// Create an instance of MimeMessage
		// it accepts MIME types and headers

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(user));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject("New FluLeap message from " + name);
		message.setText(writeMessage(name, email, subject, msg));

		// Transport class is used to deliver the message to recipient
		Transport.send(message);
	}

	private static String writeMessage(String name, String email,
			String subject, String message) {
		String text = "";

		text += "Subject: " + subject + "\n";
		text += "Date: " + Calendar.getInstance().getTime() + "\n";
		text += "From: " + name + " <" + email + ">\n\n";
		text += "----------------------------------------------------------------------------\n\n";
		text += message;

		return text;
	}

}
