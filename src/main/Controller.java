package main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import interfaces.IActions;
import utils.Redirect;
import utils.Utilities;

@WebServlet("/Controller")
@MultipartConfig
public final class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
    public Controller() {
        super();
    }

	public void init(ServletConfig config, HttpServletRequest request) throws ServletException {}

	public void destroy() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isPrint = false;
		request.getSession(true).setMaxInactiveInterval(1800);
		IActions command = null;
		Connection connection = null; 
		String page = "";
		Utilities utilities = new Utilities();
		try {
			connection = (Connection) request.getSession().getAttribute("connection");
			if (request.getParameter("form").startsWith("actions"))
				command = (IActions) Class.forName(request.getParameter("form")).getConstructor(Connection.class).newInstance(connection);
			else
				command = (IActions) Class.forName(request.getParameter("form")).newInstance();
			if (request.getParameter("action") == null || !request.getParameter("action").equals("resource")) {
				utilities.printDataHora(" - FORM: " + request.getParameter("form") + " - ACTION: " + request.getParameter("action"), isPrint);
				page = command.execute(request, response);
			}
			boolean panel = false;
			if (page.startsWith("home"))
				panel = false;
			else {
				if (request.getParameter("panel") != null) {
					if (request.getParameter("panel").equals("false"))
						panel = false;
					else if (request.getParameter("panel").equals("true"))
						panel = true;
				}
			}
			if (request.getParameter("action") == null || !request.getParameter("action").equals("resource")) {
				utilities.printDataHora(" - PANEL: " + panel + " - PAGE: " + page, isPrint);
				Redirect redirect = new Redirect();
				redirect.toPage(page, panel, request, response);
			}
			else { // resource
				utilities.printDataHora(" - FORM: " + request.getParameter("form") + " - ACTION: " + request.getParameter("action") + " - OP: " + request.getParameter("op"), isPrint);
				utilities.printDataHora(" - PANEL: " + panel, isPrint);
				command.resource(request, response);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
