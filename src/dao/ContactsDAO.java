package dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import entity.Contacts;
import entity.NewsletterLists;
import entity.Users;
import interfaces.IDao;
import mail.MailAlert;

public final class ContactsDAO implements IDao {
	
//	private static final String LIST = "contacts/list.jsp";
	private Connection connection = null;
	
	public ContactsDAO(Connection connection) {
		this.connection = connection; 
	}

	@Override
	public String form(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public String save(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public boolean check(HttpServletRequest request) {
		return false;
	}
	
	public void send(HttpServletRequest request, HttpServletResponse response) {
		try {
			connection.beginTransaction();
			Date dataAtual = new Date();
			Contacts contact = new Contacts();
			contact.setName(request.getParameter("contact.name"));
			contact.setEmail(request.getParameter("contact.email"));
			contact.setSubject(request.getParameter("contact.subject"));
			contact.setMessage_text(request.getParameter("contact.message_text"));
			contact.setCreated_at(dataAtual);
			contact.setUpdated_at(dataAtual);
			connection.save(contact);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("email", contact.getEmail());
			NewsletterLists newsletterLists = (NewsletterLists) connection.find(NewsletterLists.QUERY_FIND_BY_EMAIL, param);
			param.clear();
			if (newsletterLists != null && newsletterLists.getStatus() == 0) {
				newsletterLists.setStatus(1);
				newsletterLists.setUpdated_at(dataAtual);
				connection.update(newsletterLists);
			}
			else {
				newsletterLists = new NewsletterLists();
				newsletterLists.setName(contact.getName());
				newsletterLists.setEmail(contact.getEmail());
				newsletterLists.setStatus(1);
				newsletterLists.setCreated_at(dataAtual);
				newsletterLists.setUpdated_at(dataAtual);
				connection.save(newsletterLists);
			}
			param.put("email", contact.getEmail());
			Users user = (Users) connection.find(Users.QUERY_BY_EMAIL, param);
			param.clear();
			if (user != null && user.getEmail_receive() == 0) {
				user.setEmail_receive(1);
				user.setUpdated_at(dataAtual);
				connection.update(user);
			}
			connection.commit();
			MailAlert mailAlert = new MailAlert(
					"Giroferta - Novo contato", 
					"\n\nID: " + contact.getId() + 
					"\nNome: " + contact.getName() + 
					"\nEmail: " + contact.getEmail() + 
					"\nAssunto: " + contact.getSubject() + 
					"\nMensagem: " + contact.getMessage_text() + 
					"\n\n",
					new String[] {"atendimento@giroferta.com"},
					(String) request.getSession().getAttribute("giroUrl")
				);
			Thread threadMailAlert = new Thread(mailAlert);
			threadMailAlert.start();
		} finally {
			connection.closeTransaction();
		}
	}

}
