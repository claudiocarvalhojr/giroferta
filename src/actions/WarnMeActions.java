package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import dao.WarnMeDAO;
import interfaces.IActions;

public final class WarnMeActions implements IActions {

	private WarnMeDAO warnMeDAO = null;
	
	public WarnMeActions(Connection connection) {
		this.warnMeDAO = new WarnMeDAO(connection);
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public void resource(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("resource")) {
			if (request.getParameter("op").equals("send"))
				warnMeDAO.send(request, response);
		}
	}

}
