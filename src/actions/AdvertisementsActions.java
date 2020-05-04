package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import dao.AdvertisementsDAO;
import interfaces.IActions;

public final class AdvertisementsActions implements IActions {

	private AdvertisementsDAO advertisementsDAO = null;
	
	public AdvertisementsActions(Connection connection) {
		this.advertisementsDAO = new AdvertisementsDAO(connection);
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("form"))
			return advertisementsDAO.form(request, response);
		else if (request.getParameter("action").equals("save"))
			return advertisementsDAO.save(request, response);
		else if (request.getParameter("action").equals("publish"))
			return advertisementsDAO.publish(request, response);
		else if (request.getParameter("action").equals("delete"))
			return advertisementsDAO.delete(request, response);
		else if (request.getParameter("action").equals("list"))
			return advertisementsDAO.list(request, response);
		else if (request.getParameter("action").equals("importCsvTxt"))
			return advertisementsDAO.importCsvTxt(request, response);
		else if (request.getParameter("action").equals("importXml"))
			return advertisementsDAO.importXml(request, response);
		else if (request.getParameter("action").equals("importRunCsvTxt"))
			return advertisementsDAO.importRunCsvTxt(request, response);
		else if (request.getParameter("action").equals("importRunXml"))
			return advertisementsDAO.importRunXml(request, response);
		else if (request.getParameter("action").equals("reportByAdv"))
			return advertisementsDAO.reportByAdv(request, response);
		else if (request.getParameter("action").equals("reportByDate"))
			return advertisementsDAO.reportByDate(request, response);
		return (String) request.getSession().getAttribute("ERROR");
	}

	@Override
	public void resource(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("resource")) {
			if (request.getParameter("op").equals("count"))
				advertisementsDAO.count(request, response);		
			else if (request.getParameter("op").equals("purchases"))
				advertisementsDAO.purchases(request, response);		
			else if (request.getParameter("op").equals("countGrossClick"))
				advertisementsDAO.countGrossClick(request, response);		
			else if (request.getParameter("op").equals("countMostAccessed"))
				advertisementsDAO.countMostAccessed(request, response);		
			else if (request.getParameter("op").equals("countRecentlyAdded"))
				advertisementsDAO.countRecentlyAdded(request, response);		
			else if (request.getParameter("op").equals("countAdsUrl"))
				advertisementsDAO.countAdsUrl(request, response);		
			else if (request.getParameter("op").equals("json"))
				advertisementsDAO.json(request, response);
			else if (request.getParameter("op").equals("jsonHome"))
				advertisementsDAO.jsonHome(request, response);
			else if (request.getParameter("op").equals("jsonAdvertisement"))
				advertisementsDAO.jsonAdvertisement(request, response);
			else if (request.getParameter("op").equals("jsonAdvStores"))
				advertisementsDAO.jsonAdvStores(request, response);
//			else if (request.getParameter("op").equals("jsonCoord"))
//				advertisementsDAO.jsonCoord(request, response);
			else if (request.getParameter("op").equals("uploadImage"))
				advertisementsDAO.uploadImage(request, response);
			else if (request.getParameter("op").equals("viewImage"))
				advertisementsDAO.viewImage(request, response);
			else if (request.getParameter("op").equals("deleteImage"))
				advertisementsDAO.deleteImage(request, response);
			else if (request.getParameter("op").equals("categories"))
				advertisementsDAO.categories(request, response);
			else if (request.getParameter("op").equals("radius"))
				advertisementsDAO.getAdditionalRadius(request, response);
		}
	}

}
