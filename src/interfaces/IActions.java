package interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IActions {
	
	public String execute(HttpServletRequest request, HttpServletResponse response);
	
	public void resource(HttpServletRequest request, HttpServletResponse response);
	
}
