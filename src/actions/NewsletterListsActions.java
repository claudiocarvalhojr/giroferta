package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import dao.NewsletterListsDAO;
import interfaces.IActions;

public final class NewsletterListsActions implements IActions {
	
	private NewsletterListsDAO newsletterListsDAO = null;
	
	public NewsletterListsActions(Connection connection) {
		this.newsletterListsDAO = new NewsletterListsDAO(connection);
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("save"))
			newsletterListsDAO.save(request, response);
		return (String) request.getSession().getAttribute("ERROR");
	}

	@Override
	public void resource(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("resource")) {
			if (request.getParameter("op").equals("json"))
				newsletterListsDAO.json(request, response);
		}
	}

}
