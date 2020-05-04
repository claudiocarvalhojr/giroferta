package config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import interfaces.IActions;

public final class Home implements IActions {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String HOME = "home.jsp";
		Config config = new Config();
		config.setConfig(request, response);
		return HOME;
	}

	@Override
	public void resource(HttpServletRequest request, HttpServletResponse response) {}

}
