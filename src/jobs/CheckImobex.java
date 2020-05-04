package jobs;

import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import connection.Connection;
import entity.Imobex;
import mail.MailAlert;
import utils.Utilities;

public class CheckImobex extends TimerTask {
	
	private Connection connection = null;
	
	private String appPath;
	
	public CheckImobex(Connection connection, String appPath) {
		this.connection = connection;
		this.appPath = appPath;
	}

	@Override
	public void run() {
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		utilities.printDataHora(" - CHECK IMOBEX => INICIO ...", isPrint);
		long initialTime = System.currentTimeMillis();
		String url = "http://repo.lenova.com.br/giroferta/";
		Document doc = null;
		Elements links = null;
		Imobex imobex = null;
		MailAlert mailAlert = null;
		Thread threadMailAlert = null;
		int countNewImobex = 0;
		try {
			connection.beginTransaction();
			doc = Jsoup.connect(url).get();
			links = doc.select("a[href]");
				for (Element link : links) {
					if (utilities.isDigit(link.text().substring(0, link.text().length() - 1))) {
						if (connection.find(Imobex.class, Integer.parseInt(link.text().substring(0, link.text().length() - 1))) == null) {
							countNewImobex++;
							imobex = new Imobex();
							imobex.setId(Integer.parseInt(link.text().substring(0, link.text().length() - 1)));
							imobex.setStatus(0);
							imobex.setCreated_at(new Date());
							connection.save(imobex);
							if (!appPath.equals("/GirofertaWebApp")) {
								mailAlert = new MailAlert(
										"Giroferta - Novo usuário IMOBEX", 
										"\n\nURL: " + url + link.text().substring(0, link.text().length() - 1) + "/" +
										"\n\n",
										new String[] {"golive@giroferta.com"},
										"https://www.giroferta.com/"
									);
								threadMailAlert = new Thread(mailAlert);
								threadMailAlert.start();
							}
						}
					}
				}
			connection.commit();
			utilities.printDataHora(" - CHECK IMOBEX => ... FIM | TOTAL: " + countNewImobex + " | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.closeTransaction();
			utilities = null;
			url = null;
			doc = null;
			links = null;
			imobex = null;
			mailAlert = null;
			threadMailAlert = null;
		}
		
	}

}
