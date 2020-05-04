package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import dao.ContactsDAO;
import interfaces.IActions;

public final class ContactsActions implements IActions {

	private ContactsDAO contactDAO = null;
	
	public ContactsActions(Connection connection) {
		this.contactDAO = new ContactsDAO(connection);
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public void resource(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("resource")) {
			if (request.getParameter("op").equals("send"))
				contactDAO.send(request, response);
		}
	}

}
