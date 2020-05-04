package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import dao.ChatDAO;
import interfaces.IActions;

public final class ChatActions implements IActions {

	private ChatDAO chatDAO = null;
	
	public ChatActions(Connection connection) {
		this.chatDAO = new ChatDAO(connection);
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("form"))
			return chatDAO.form(request, response);
		else if (request.getParameter("action").equals("save"))
			return chatDAO.save(request, response);
		else if (request.getParameter("action").equals("list"))
			return chatDAO.list(request, response);
		return (String) request.getSession().getAttribute("ERROR");
	}

	@Override
	public void resource(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("resource")) {
			if (request.getParameter("op").equals("create"))
				chatDAO.create(request, response);
		}
	}

}
