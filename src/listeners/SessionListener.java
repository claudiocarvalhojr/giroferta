package listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import utils.Utilities;

@WebListener
public final class SessionListener implements HttpSessionListener {
	
    public SessionListener() {}

    public void sessionCreated(HttpSessionEvent arg0)  {
    	Utilities utilities = new Utilities();
    	utilities.printDataHora(" - SESSION: " + arg0.getSession().getId() + " - INICIADA", false);
    	utilities = null;
    }

    public void sessionDestroyed(HttpSessionEvent arg0)  {
    	Utilities utilities = new Utilities();
    	utilities.printDataHora(" - SESSION: " + arg0.getSession().getId() + " - FINALIZADA", false);
    	utilities = null;
    }
	
}
