package dao;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import connection.Connection;
import entity.NewsletterLists;
import interfaces.IDao;
import utils.Utilities;

public final class NewsletterListsDAO implements IDao {
	
	private Connection connection = null;
	
	public NewsletterListsDAO(Connection connection) {
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
	
	public void json(HttpServletRequest request, HttpServletResponse response) {
		Utilities utilities = null;
		Map<String, Object> param = null;
		NewsletterLists newsletterList = null; 
		String gson = null;
		String name = null;
		String email = null;
		Date dataAtual = null;
		try {
			connection.beginTransaction();
			gson = "";
			name = request.getParameter("nameNewsletter");
			email = request.getParameter("mailNewsletter");
			utilities = new Utilities();
			if (utilities.isEmail(email)) {
				param = new HashMap<String, Object>();
				param.put("email", email);
				newsletterList = (NewsletterLists) connection.find(NewsletterLists.QUERY_FIND_BY_EMAIL, param);
				param.clear();
				if (newsletterList == null) {
					dataAtual = new Date();
					newsletterList = new NewsletterLists();
					newsletterList.setName(name);
					newsletterList.setEmail(email);
					newsletterList.setStatus(1);
					newsletterList.setCreated_at(dataAtual);
					newsletterList.setUpdated_at(dataAtual);
					connection.save(newsletterList);
					connection.commit();
					gson = new Gson().toJson(true);
				}
				else
					gson = new Gson().toJson(false);
				try {
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(gson);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
				gson = new Gson().toJson(false);
		} finally {
			connection.closeTransaction();
			utilities = null;
			param = null;
			newsletterList = null; 
			gson = null;
			name = null;
			email = null;
			dataAtual = null;
		}
	}

}
