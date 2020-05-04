package jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import connection.Connection;
import entity.Users;

public class CheckAccountPending extends TimerTask {
	
	private Connection connection = null;
	
	public CheckAccountPending(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		try {
			connection.beginTransaction();
			Calendar calendarInitial = Calendar.getInstance();
			calendarInitial.add(Calendar.DATE, -365);
			Date initialDate = calendarInitial.getTime();
			Date currentDate = new Date();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("initialDate", initialDate);
			param.put("currentDate", currentDate);
			@SuppressWarnings("unchecked")
			List<Users> listUsers = (List<Users>) connection.list(Users.QUERY_BY_DATE_UNCONFIRMED, param);
			param.clear();
			for (Users user : listUsers) {
				System.out.println("E-mail: " + user.getEmail());
			}
			
		} finally {
			connection.closeTransaction();
		}
		
	}

}
