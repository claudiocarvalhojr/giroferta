package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import interfaces.IActions;
import pagseguro.PagSeguroUtils;

public final class PagSeguroActions implements IActions {
	
	private PagSeguroUtils pagSeguroUtils = null;
	
	public PagSeguroActions(Connection connection) {
		this.pagSeguroUtils = new PagSeguroUtils(connection);
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("form"))
			return pagSeguroUtils.form(request, response);
		else if (request.getParameter("action").equals("confirmation"))
			return pagSeguroUtils.confirmation(request, response);
		return (String) request.getSession().getAttribute("ERROR");
	}

	@Override
	public void resource(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("resource")) {
			if (request.getParameter("op").equals("checkoutCode"))
				pagSeguroUtils.checkoutCode(request, response);
		}
	}

}
