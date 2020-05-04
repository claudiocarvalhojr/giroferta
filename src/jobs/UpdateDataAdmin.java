package jobs;

import java.util.TimerTask;

import connection.Connection;
import entity.Admin;
import entity.AdvertisementStores;
import entity.Stores;
import utils.Utilities;

public class UpdateDataAdmin extends TimerTask {
	
	private Connection connection = null;
	
	public UpdateDataAdmin(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		int totalAdvActives = 0;
		int totalAdv = 0;
		int totalStores = 0;
		Admin admin = null;
		try {
			utilities.printDataHora(" - UPDATE DATA ADMIN => INICIO ...", isPrint);
			long initialTime = System.currentTimeMillis();
			connection.beginTransaction();
			totalAdvActives = connection.count(AdvertisementStores.QUERY_COUNT_BY_ADV_ACTIVES);
			totalAdv = connection.count(AdvertisementStores.QUERY_COUNT_TOTAL);
			totalStores = connection.count(Stores.QUERY_COUNT_TOTAL);
			admin = (Admin) connection.find(Admin.class, 1);
			admin.setTotal_adv_actives(totalAdvActives);
			admin.setTotal_adv(totalAdv);
			admin.setTotal_stores(totalStores);
			connection.update(admin);
			connection.commit();
			utilities.printDataHora(" - UPDATE DATA ADMIN => ... FIM | TOTAL ADV ACTIVES: " + totalAdvActives + " | TOTAL ADV: " + totalAdv + " | TOTAL STORES: " + totalStores + " | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} finally {
			connection.closeTransaction();
			utilities = null;
			admin = null;
		}
		
	}

}
