package jobs;

import java.util.TimerTask;

import connection.Connection;
import utils.Utilities;

public class Jobs extends TimerTask {
	
	private Connection connection = null;
	private int idUser = 0;
	private boolean isReImport = false;
	private String pathImages = null;
	private String pathSitemap = null;
	
	public Jobs(Connection connection, int idUser, boolean isReImport, String pathImages, String pathSitemap) {
		this.connection = connection;
		this.idUser = idUser;
		this.isReImport = isReImport;
		this.pathImages = pathImages;
		this.pathSitemap = pathSitemap;
	}
	
	public void run() {
		long initialTime = System.currentTimeMillis();
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		try {
			utilities.printDataHora(" - JOBS => INICIO... ", isPrint);
			new ImportXML(connection, idUser).run();
			new ImportImages(connection, (String) pathImages, idUser, isReImport).run();
			new ReplyAdsInStores(connection, idUser).run();
			new UpdateHighLights(connection).run();
			new UpdateSitemap(connection, (String) pathSitemap).run();
			utilities.printDataHora(" - JOBS => ... FIM  | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} finally {
			utilities = null;
		}
	}

}
