package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import dao.UsersDAO;
import interfaces.IActions;

public final class UsersActions implements IActions {
	
	private UsersDAO userDAO = null;
	
	public UsersActions(Connection connection) {
		this.userDAO = new UsersDAO(connection);
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("form"))
			return userDAO.form(request, response);
		else if (request.getParameter("action").equals("save"))
			return userDAO.save(request, response);
		else if (request.getParameter("action").equals("delete"))
			return userDAO.delete(request, response);
		else if (request.getParameter("action").equals("list"))
			return userDAO.list(request, response);
		else if (request.getParameter("action").equals("formSendInstructionsResetPassword"))
			return userDAO.formSendInstructionsResetPassword(request, response);
		else if (request.getParameter("action").equals("sendInstructionsResetPassword"))
			return userDAO.sendInstructionsResetPassword(request, response);
		else if (request.getParameter("action").equals("formResetPassword"))
			return userDAO.formResetPassword(request, response);
		else if (request.getParameter("action").equals("confirmResetPassword"))
			return userDAO.confirmResetPassword(request, response);
		else if (request.getParameter("action").equals("formUpdatePassword"))
			return userDAO.formUpdatePassword(request, response);
		else if (request.getParameter("action").equals("confirmUpdatePassword"))
			return userDAO.confirmUpdatePassword(request, response);
		else if (request.getParameter("action").equals("activation"))
			return userDAO.activation(request, response);
		else if (request.getParameter("action").equals("login"))
			return userDAO.login(request, response);
		else if (request.getParameter("action").equals("check"))
			return userDAO.checkLogin(request, response);
		else if (request.getParameter("action").equals("logout"))
			return userDAO.logout(request, response);
		else if (request.getParameter("action").equals("purchases"))
			return userDAO.purchases(request, response);
		else if (request.getParameter("action").equals("terms"))
			return userDAO.terms(request, response);
		else 
			return (String) request.getSession().getAttribute("ERROR");
	}

	@Override
	public void resource(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("resource")) {
			if (request.getParameter("op").equals("uploadImage"))
				userDAO.uploadImage(request, response);
			else if (request.getParameter("op").equals("viewImage"))
				userDAO.viewImage(request, response);
			else if (request.getParameter("op").equals("deleteImage"))
				userDAO.deleteImage(request, response);
			else if (request.getParameter("op").equals("loginSocial"))
				userDAO.loginSocial(request, response);
/*			else if (request.getParameter("op").equals("cities"))
				userDAO.cities(request, response);
*/		}
	}

}
