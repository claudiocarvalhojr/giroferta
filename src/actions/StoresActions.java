package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import dao.StoresDAO;
import interfaces.IActions;

public final class StoresActions implements IActions {
	
	private StoresDAO storesDAO = null;
	
	public StoresActions(Connection connection) {
		this.storesDAO = new StoresDAO(connection);
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("form"))
			return storesDAO.form(request, response);
		else if (request.getParameter("action").equals("save"))
			return storesDAO.save(request, response);
		else if (request.getParameter("action").equals("delete"))
			return storesDAO.delete(request, response);
		else if (request.getParameter("action").equals("list"))
			return storesDAO.list(request, response);
		return (String) request.getSession().getAttribute("ERROR");
	}

	@Override
	public void resource(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("resource")) {
			if (request.getParameter("op").equals("cities"))
				storesDAO.cities(request, response);
			else if (request.getParameter("op").equals("json"))
				storesDAO.json(request, response);
			else if (request.getParameter("op").equals("uploadImageLogo"))
				storesDAO.uploadImageLogo(request, response);
			else if (request.getParameter("op").equals("deleteImageLogo"))
				storesDAO.deleteImageLogo(request, response);
			else if (request.getParameter("op").equals("uploadImage"))
				storesDAO.uploadImage(request, response);
			else if (request.getParameter("op").equals("viewImage"))
				storesDAO.viewImage(request, response);
			else if (request.getParameter("op").equals("deleteImage"))
				storesDAO.deleteImage(request, response);
		}
	}

}
