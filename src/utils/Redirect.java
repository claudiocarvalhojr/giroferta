package utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Redirect {

	private static final String pathPagePanel = "/jsp/panel/";
	private static final String pathPageDefault = "/jsp/";
	private static final String pathPageTemplatePanel = "/templates/panel/layout.jsp";	
	private static final String pathPageTemplateDefault = "/templates/layout.jsp";
	private static final String attributeNameShowPages = "yield";

	public void toPage(String pageDisplayed, boolean isPanel, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathPageDisplayed = ""; 
		String pageContainer = "";
		if (isPanel) {
			pathPageDisplayed = pathPagePanel + pageDisplayed;
			pageContainer = pathPageTemplatePanel;					
		}
		else {
			pathPageDisplayed = pathPageDefault + pageDisplayed;
			pageContainer = pathPageTemplateDefault;
		}
		request.getSession().setAttribute(attributeNameShowPages, pathPageDisplayed);
		if (!response.isCommitted()){
			request.getRequestDispatcher(pageContainer).forward(request, response);
		}
	}

}
