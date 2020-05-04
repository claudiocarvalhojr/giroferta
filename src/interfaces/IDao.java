package interfaces;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IDao {
	
	public String form(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException ;
	
	public String save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException ;

	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException ;

	public String list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException ;

	public boolean check(HttpServletRequest request) throws ServletException, IOException;

}
