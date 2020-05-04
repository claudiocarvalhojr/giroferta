package mail;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public final class MailAlert extends Thread {
	
	private static final String hostName = "mail.giroferta.com";
	private static final String smtpPort = "587";
	private static final String mailGiro = "giroferta@giroferta.com";
	private static final String passwordMailGiro = "G1rOfert@!15";
	private static final String charSet = "UTF-8";
//	private static final String[] emailsAdmin = {"claudio@giroferta.com","vanderlei@giroferta.com"};
//	private static final String[] emailsAdmin = {"golive@giroferta.com"};
	private String[] emailsAdmin;
	private String subject;
	private String content;
	private String giroUrl;
	
	public MailAlert(String subject, String content, String[] recipients, String giroUrl) {
		this.subject = subject;
		this.content = content;
		this.emailsAdmin = recipients;
		this.giroUrl = giroUrl;
	}
	
	@Override
	public void run() {
		if (giroUrl.equals("https://www.giroferta.com/")) {
			SimpleEmail email = new SimpleEmail();
			email.setCharset(charSet);
			email.setSSLOnConnect(true);
			email.setHostName(hostName);
			email.setSslSmtpPort(smtpPort);
			email.setAuthenticator(new DefaultAuthenticator(mailGiro, passwordMailGiro));
			try {
				email.addTo(emailsAdmin);
				email.setFrom(mailGiro, "Giroferta");
//				email.setDebug(true);
				email.setSubject(subject);
				email.setMsg(content);
				email.send();
			} catch (EmailException e) {
				e.printStackTrace();
			}
		}
	}
	
}
