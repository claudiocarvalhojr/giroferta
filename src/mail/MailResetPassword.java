package mail;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public final class MailResetPassword implements Runnable {
	
	private static final String hostName = "mail.giroferta.com";
	private static final String smtpPort = "587";
	private static final String mailGiro = "giroferta@giroferta.com";
	private static final String passwordMailGiro = "G1rOfert@!15";
	private static final String charSet = "UTF-8";
	private String context;
	private String userName;
	private String userEmail;
	private String userToken;
	private String serverImages;
	
	public MailResetPassword(String context, String userName, String userEmail, String userToken, String serverImages) {
		this.context = context;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userToken = userToken;
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
			
		    email.addTo(userEmail);
		    email.setFrom(mailGiro, "Giroferta");
//		    email.setDebug(true);
		    email.setSubject("Instruções para troca de senha");
		     
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
							"Olá <strong>" + userName + "!</strong>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td style=\"padding: 10px 15px; font-size: 13px;\">" +
							"Nós recebemos uma solicitação para trocar a senha da sua conta no Giroferta. Por favor visite o link abaixo para definir uma nova senha: " + 
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td style=\"padding: 10px 15px 20px 15px;\">" +
							"<a href=\"" + context + "Controller?form=actions.UsersActions&action=formResetPassword&panel=true&email=" + userEmail + "&token=" + userToken + "\">Alterar minha senha</a>" +
						"</td>" +
					"</tr>" +
					"<tr>" +
						"<td style=\"padding: 10px 15px; font-size: 13px;\">" +
							"Se você não pediu para trocar sua senha por favor ignore esta mensagem." + 
							"<br/>" + 
							"<br/>" + 
							"Atenciosamente," + 
							"<br/>" + 
							"<br/>" + 
							"A Equipe Giroferta." +
							"<br/>" + 
							"<br/>" + 
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
