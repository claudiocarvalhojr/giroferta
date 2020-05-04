package dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import entity.NewsletterLists;
import entity.Users;
import entity.WarnMe;
import interfaces.IDao;

public final class WarnMeDAO implements IDao {

//	private static final String LIST = "contacts/list.jsp";
	private Connection connection = null;
	
	public WarnMeDAO(Connection connection) {
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
			WarnMe warnMe = new WarnMe();
			warnMe.setName(request.getParameter("warnMe.name"));
			warnMe.setEmail(request.getParameter("warnMe.email"));
			warnMe.setTerm(request.getParameter("warnMe.term"));
			warnMe.setLatitude(request.getParameter("warnMe.latitude"));
			warnMe.setLongitude(request.getParameter("warnMe.longitude"));
			warnMe.setCreated_at(dataAtual);
			warnMe.setUpdated_at(dataAtual);		
			connection.save(warnMe);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("email", warnMe.getEmail());
			NewsletterLists newsletterLists = (NewsletterLists) connection.find(NewsletterLists.QUERY_FIND_BY_EMAIL, param);
			param.clear();
			if (newsletterLists != null && newsletterLists.getStatus() == 0) {
				newsletterLists.setStatus(1);
				newsletterLists.setUpdated_at(dataAtual);
				connection.update(newsletterLists);
			}
			else {
				newsletterLists = new NewsletterLists();
				newsletterLists.setName(warnMe.getName());
				newsletterLists.setEmail(warnMe.getEmail());
				newsletterLists.setStatus(1);
				newsletterLists.setCreated_at(dataAtual);
				newsletterLists.setUpdated_at(dataAtual);
				connection.save(newsletterLists);
			}
			param.put("email", warnMe.getEmail());
			Users user = (Users) connection.find(Users.QUERY_BY_EMAIL, param);
			param.clear();
			if (user != null && user.getEmail_receive() == 0) {
				user.setEmail_receive(1);
				user.setUpdated_at(dataAtual);
				connection.update(user);
			}
			connection.commit();
		} finally {
			connection.closeTransaction();
		}
	}
	
}
