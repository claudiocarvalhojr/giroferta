package mail;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public final class MailChatSpecial extends Thread {
	
	private static final String hostName = "mail.giroferta.com";
	private static final String smtpPort = "587";
	private static final String mailGiro = "giroferta@giroferta.com";
	private static final String passwordMailGiro = "G1rOfert@!15";
	private static final String charSet = "UTF-8";
	private String context;
	private String senderName;
	private String senderEmail;
	private String senderMessage;
	private String receiverName;
	private String receiverEmail; 
	private String receiverReference;
	private String serverImages;
	
	public MailChatSpecial(String context, String senderName, String senderEmail, String senderMessage, String receiverName, String receiverEmail, String receiverReference, String serverImages) {
		this.context = context;
		this.senderName = senderName;
		this.senderEmail = senderEmail;
		this.senderMessage = senderMessage;
		this.receiverName = receiverName;
		this.receiverEmail = receiverEmail;
		this.receiverReference = receiverReference;
		this.serverImages = serverImages;
	}
	
	@Override
	public void run() {
		HtmlEmail email = new HtmlEmail();
		email.setCharset(charSet);
		email.setSSLOnConnect(true);
		email.setHostName(hostName);
		email.setSslSmtpPort(smtpPort);
		email.setAuthenticator(new DefaultAuthenticator(mailGiro, passwordMailGiro));
		try {
			
		    email.addTo(receiverEmail);
		    email.setFrom(mailGiro, "Giroferta");
//		    email.setDebug(true);
		    email.setSubject("Nova mensagem no Giroferta");
		     
			URL url = new URL(serverImages + "images/logo-horizontal.png");
			String cid = email.embed(url, "Giroferta logo");
			
		    StringBuilder content = new StringBuilder();
		    content.append(
	    		"<table border=\"0\" style=\"font-family: Verdana;\">" +
	    			"<tr style=\"background-color: #B32C36;\">" +
	    				"<td style=\"padding: 10px 15px;\">" +
	    					"<img alt=\"Logo Giroferta\" src=\"cid:" + cid + "\" style=\"width: 30%;\">" +
	    				"</td>" +
					"</tr>" +
					"<tr>" +
						"<td style=\"padding: 20px 15px 10px 15px;\">" +
							"Olá <strong>" + receiverName + "!</strong>" +
						"</td>" +
					"</tr>" +
//					"<tr>" +
//						"<td style=\"padding: 10px 15px; font-size: 13px;\">" +
//							"Existe uma nova mensagem para você no Giroferta, enviada por <strong>" + senderName + "</strong>" +
//						"</td>" +
//					"</tr>" +
//					"<tr>" +
//						"<td style=\"padding: 10px 15px; font-size: 13px;\">" +
//							"Acesse sua conta através do link abaixo para visualizá-la:" + 
//						"</td>" +
//					"</tr>" +

					"<tr>" +
						"<td style=\"padding: 10px 15px; font-size: 13px;\">" +
							"A seguinte mensagem foi enviada para você através do Giroferta.com por <strong>" + senderName + "</strong>, email para retorno: <b>" + senderEmail + "</b>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td style=\"padding: 10px 15px; font-size: 13px;\">" +
							"Produto: <b>" + receiverReference  + "</b><br /><br/ >\"<i>" + senderMessage + "</i>\"<br />" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td style=\"padding: 10px 15px; font-size: 13px;\">" +
							"Para mais detalhes, acesse sua conta através do link abaixo para visualizá-la:" + 
						"</td>" +
					"</tr>" +
					
					"<tr>" +
						"<td style=\"padding: 10px 15px 20px 15px;\">" +
							"<a href=\"" + context + "\">www.giroferta.com</a>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td style=\"background-color: #B32C36;\">" +
						"&nbsp;" +
						"</td>" +
					"</tr>" +
				"</table>"
			);
		    
		    email.setHtmlMsg(content.toString());
		    email.send();
		} catch (EmailException e) {
		    e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 	
	}
	
}
