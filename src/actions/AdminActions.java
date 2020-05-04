package actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import dao.AdminDAO;
import interfaces.IActions;

public final class AdminActions implements IActions {

	private AdminDAO adminDAO = null;
	
	public AdminActions(Connection connection) {
		this.adminDAO = new AdminDAO(connection);
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("manageAccountsForm"))
			return adminDAO.manageAccountsForm(request, response);
		else if (request.getParameter("action").equals("manageAccountsSearch"))
			return adminDAO.manageAccountsSearch(request, response);
		else if (request.getParameter("action").equals("manageAccountsApplyActive"))
			return adminDAO.manageAccountsApplyActive(request, response);
		else if (request.getParameter("action").equals("manageAccountsApplyProfile"))
			return adminDAO.manageAccountsApplyProfile(request, response);
		else if (request.getParameter("action").equals("manageAccountsApplyPassword"))
			return adminDAO.manageAccountsApplyPassword(request, response);
		else if (request.getParameter("action").equals("manageAccountsApplyExtraCredit"))
			return adminDAO.manageAccountsApplyExtraCredit(request, response);
		else if (request.getParameter("action").equals("manageAccountsApplyCurrentClick"))
			return adminDAO.manageAccountsApplyCurrentClick(request, response);
		else if (request.getParameter("action").equals("manageAccountsApplyImportXml"))
			return adminDAO.manageAccountsApplyImportXml(request, response);
		else if (request.getParameter("action").equals("manageAccountsApplyTypeXml"))
			return adminDAO.manageAccountsApplyTypeXml(request, response);
		else if (request.getParameter("action").equals("manageAccountsApplyUrlXml"))
			return adminDAO.manageAccountsApplyUrlXml(request, response);
		else if (request.getParameter("action").equals("list"))
			return adminDAO.list(request, response);
		else if (request.getParameter("action").equals("courtesyForm"))
			return adminDAO.courtesyForm(request, response);
		else if (request.getParameter("action").equals("courtesySearch"))
			return adminDAO.courtesySearch(request, response);
		else if (request.getParameter("action").equals("courtesyApply"))
			return adminDAO.courtesyApply(request, response);
		else if (request.getParameter("action").equals("sales"))
			return adminDAO.sales(request, response);
		else if (request.getParameter("action").equals("setup"))
			return adminDAO.manager(request, response);
		else if (request.getParameter("action").equals("jobs"))
			return adminDAO.jobs(request, response);
		else if (request.getParameter("action").equals("reports"))
			return adminDAO.reports(request, response);
		else if (request.getParameter("action").equals("reportByAdv"))
			return adminDAO.reportByAdv(request, response);
		else if (request.getParameter("action").equals("reportByDate"))
			return adminDAO.reportByDate(request, response);
		return (String) request.getSession().getAttribute("ERROR");
	}

	@Override
	public void resource(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("resource")) {
			if (request.getParameter("op").equals("fullproccess"))
				adminDAO.fullProccess(request, response);		
			else if (request.getParameter("op").equals("xml"))
				adminDAO.importXML(request, response);		
			else if (request.getParameter("op").equals("reply"))
				adminDAO.replyAdsStores(request, response);		
			else if (request.getParameter("op").equals("sitemap"))
				adminDAO.updateSitemap(request, response);		
			else if (request.getParameter("op").equals("images"))
				adminDAO.importImages(request, response);		
			else if (request.getParameter("op").equals("fixes"))
				adminDAO.fixesImages(request, response);		
			else if (request.getParameter("op").equals("highlights"))
				adminDAO.updateHighLights(request, response);		
			else if (request.getParameter("op").equals("urls"))
				adminDAO.updateURLs(request, response);		
		}
	}

}
