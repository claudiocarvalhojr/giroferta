package jobs;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.hibernate.jdbc.ReturningWork;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import connection.Connection;
import entity.Advertisements;
import entity.Users;
import utils.Utilities;
import writerXML.Sitemap;
import writerXML.SitemapIndex;
import writerXML.Url;
import writerXML.UrlSet;

public class UpdateSitemap extends TimerTask {
	
	private Connection connection = null;
	
	private String pathSitemap = null;
	
	public String getPathSitemap() {
		return pathSitemap;
	}

	public void setPathSitemap(String pathSitemap) {
		this.pathSitemap = pathSitemap;
	}

	public UpdateSitemap(Connection connection, String pathSitemap) {
		this.connection = connection;
		this.setPathSitemap(pathSitemap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		long initialTime = System.currentTimeMillis();
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		Serializer serializer = null;
		UrlSet urlSet = null;
		SitemapIndex sitemapIndex = null;
		List<Url> listUrl = null;
		List<String> listAuxUrl = null;
		List<String> listFile = null;
		List<Sitemap> listSitemap = null;
		File result = null;
		File pathXML = null;
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			utilities.printDataHora(" - UPDATE SITEMAP => INICIO ...", isPrint);
			connection.beginTransaction();
			serializer = new Persister(new Format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
			listFile = new ArrayList<String>();
			pathXML = new File(getPathSitemap());			
			if (!pathXML.exists())
				pathXML.mkdirs();
			pathXML.setWritable(true, true);
			
			
			
			/* STATIC ****************************************************** */
			urlSet = new UrlSet("http://www.sitemaps.org/schemas/sitemap/0.9");
			listUrl = new ArrayList<Url>();
			listUrl.add(new Url("https://www.giroferta.com/", LocalDate.now().format(formato), "daily", 1.0));
			listUrl.add(new Url("https://www.giroferta.com/home/", LocalDate.now().format(formato), "weekly", 0.6));
			listUrl.add(new Url("https://www.giroferta.com/register/", LocalDate.now().format(formato), "weekly", 0.6));
			listUrl.add(new Url("https://www.giroferta.com/login/", LocalDate.now().format(formato), "weekly", 0.6));
			listUrl.add(new Url("https://www.giroferta.com/terms/", LocalDate.now().format(formato), "weekly", 0.6));
			listUrl.add(new Url("https://www.giroferta.com/fique-por-dentro", LocalDate.now().format(formato), "weekly", 0.6));
			urlSet.setListUrl(listUrl);
			result = new File(getPathSitemap() + "static.xml");
			listFile.add("static.xml");
			serializer.write(urlSet, result);
			urlSet = null;
			listUrl = null;
			
			
			
			/* STORES ****************************************************** */
			urlSet = new UrlSet("http://www.sitemaps.org/schemas/sitemap/0.9");
			listUrl = new ArrayList<Url>();
			listAuxUrl = (List<String>) connection.list(Users.QUERY_LIST_ALL_URL_ACTIVE);
			for (String url: listAuxUrl)
				listUrl.add(new Url("https://www.giroferta.com/" + url, LocalDate.now().format(formato), "daily", 0.9));
			urlSet.setListUrl(listUrl);
			result = new File(getPathSitemap() + "stores.xml");
			listFile.add("stores.xml");
			serializer.write(urlSet, result);
			urlSet = null;
			listUrl = null;
			
		
			
			/* ADVERTISEMENTS ********************************************** */
			urlSet = new UrlSet("http://www.sitemaps.org/schemas/sitemap/0.9");
			listUrl = new ArrayList<Url>();
//			int totalAdv = connection.count(AdvertisementStores.QUERY_COUNT_TOTAL);
			int totalAdv = connection.count(Advertisements.QUERY_COUNT_TOTAL_PUBLISHED);
			int limitAdv = 35000;
			int qtdaAtualAdv = 0;
			int count = 0;
			int countURL = 0;
			int cutOff = 20;
			totalAdv /= limitAdv;
			for (int i=0; i <= totalAdv; i++) {
				urlSet = new UrlSet("http://www.sitemaps.org/schemas/sitemap/0.9");
				listUrl = new ArrayList<Url>();
//				listAuxUrl = (List<String>) connection.list(AdvertisementStores.QUERY_LIST_ALL_URL_ACTIVE, qtdaATualAdv, limitAdv);
//				for (String url: listAuxUrl) {
				for (String url: getListUrlAds(qtdaAtualAdv, limitAdv)) {
					listUrl.add(new Url("https://www.giroferta.com/" + url, LocalDate.now().format(formato), "weekly", 0.9));
					countURL++;
					if (countURL % cutOff  == 0) {
						connection.getSession().flush();
						connection.getSession().clear();
					}
				}
				qtdaAtualAdv += limitAdv;
//				System.out.println("QTDA: " + qtdaATualAdv);
				count++;
				urlSet.setListUrl(listUrl);
				result = new File(getPathSitemap() + "advertisements" + count + ".xml");
				serializer.write(urlSet, result);
				listFile.add("advertisements" + count + ".xml");
				urlSet = null;
				listUrl = null;
			}
	
			
			
			/* SITEMAP ***************************************************** */
			sitemapIndex = new SitemapIndex("http://www.sitemaps.org/schemas/sitemap/0.9");
			listSitemap = new ArrayList<Sitemap>();
			for (String fileName: listFile)
				listSitemap.add(new Sitemap("https://www.giroferta.com/sitemap/" + fileName, LocalDate.now().format(formato)));
			sitemapIndex.setListSitemap(listSitemap);
			result = new File(getPathSitemap() + "sitemap.xml");
			serializer.write(sitemapIndex, result);
			
			
			utilities.printDataHora(" - UPDATE SITEMAP => ... FIM | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.closeTransaction();
			utilities = null;
			pathXML = null;
			serializer = null;
			urlSet = null;
			sitemapIndex = null;
			listUrl = null;
			listAuxUrl = null;
			listFile = null;
			listSitemap = null;
			result = null;
			formato = null;
		}
	}

//	private List<String> getListUrl(int qtdaATualAdv, int limitAdv) {
//		return connection.getSession().doReturningWork(
//			new ReturningWork<List<String>>() {
//				@Override
//				public List<String> execute(java.sql.Connection con) throws SQLException {
//					List<String> listAux = new ArrayList<String>();
//					PreparedStatement pst = null;
//					ResultSet rst = null; 
//					try {
//						pst = con.prepareStatement(
//								  " SELECT url FROM advertisement_stores "
//								+ " WHERE "
//								+ " status = 1 "
//								+ " LIMIT ?, ? "
//							);
//						pst.setInt(1, qtdaATualAdv);
//						pst.setInt(2, limitAdv);
//						rst = pst.executeQuery();
//						while(rst.next())
//							listAux.add(rst.getString(1));
//					} finally {
//						pst.close();
//						rst.close();
//					}
//					return listAux;
//				}
//			}
//		);
//	}
	
	private List<String> getListUrlAds(int qtdaATualAdv, int limitAdv) {
		return connection.getSession().doReturningWork(
			new ReturningWork<List<String>>() {
				@Override
				public List<String> execute(java.sql.Connection con) throws SQLException {
					List<String> listAux = new ArrayList<String>();
					PreparedStatement pst = null;
					ResultSet rst = null; 
					try {
						pst = con.prepareStatement(
								  " SELECT url FROM advertisements "
								+ " WHERE "
								+ " status = 2 "
								+ " LIMIT ?, ? "
							);
						pst.setInt(1, qtdaATualAdv);
						pst.setInt(2, limitAdv);
						rst = pst.executeQuery();
						while(rst.next())
							listAux.add(rst.getString(1));
					} finally {
						pst.close();
						rst.close();
					}
					return listAux;
				}
			}
		);
	}
	
}
