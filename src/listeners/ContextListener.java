package listeners;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import connection.Connection;
import entity.Addresses;
import entity.Admin;
import entity.AdsStoresMostAccessed;
import entity.AdsStoresRecentlyAdded;
import entity.AdvertisementImages;
import entity.AdvertisementStores;
import entity.Advertisements;
import entity.Categories;
import entity.Chat;
import entity.ChatMessages;
import entity.Cities;
import entity.ClickValues;
import entity.Contacts;
import entity.Countries;
import entity.GoogleCategories;
import entity.HistoryAdsUrl;
import entity.HistoryCountClick;
import entity.HistoryCourtesy;
import entity.HistoryGrossClick;
import entity.HistoryMostAccessed;
import entity.HistoryPurchases;
import entity.HistoryRecentlyAdded;
import entity.HistorySearch;
import entity.Imobex;
import entity.NewsletterLists;
import entity.Orders;
import entity.States;
import entity.Stopwords;
import entity.StoreImages;
import entity.Stores;
import entity.Users;
import entity.WarnMe;
import jobs.CheckImobex;
import jobs.CheckPaymentsPagSeguro;
import jobs.FixesImages;
import jobs.ImportImages;
import jobs.ImportXML;
import jobs.Jobs;
import jobs.ReplyAdsInStores;
import jobs.UpdateAddresses;
import jobs.UpdateDataAdmin;
import jobs.UpdateHighLights;
import jobs.UpdateSitemap;
import utils.Utilities;

@WebListener
public final class ContextListener implements ServletContextListener {
	
	private boolean addDay = true;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		SessionFactory sessionFactory = (SessionFactory) arg0.getServletContext().getAttribute("SessionFactory");
		if (sessionFactory != null)
			sessionFactory.close();
		sessionFactory = null;
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
//		System.out.println("ContextPath.......: " + arg0.getServletContext().getContextPath());
//		System.out.println("ServletContextName: " + arg0.getServletContext().getServletContextName());
//		System.out.println("VirtualServerName.: " + arg0.getServletContext().getVirtualServerName());
//		System.out.println("ServerInfo........: " + arg0.getServletContext().getServerInfo());
		Configuration configuration = new Configuration().configure();
		configuration.addResource("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry)
       		.addAnnotatedClass(Addresses.class)
       		.addAnnotatedClass(Admin.class)
    		.addAnnotatedClass(AdsStoresMostAccessed.class)
    		.addAnnotatedClass(AdsStoresRecentlyAdded.class)
    		.addAnnotatedClass(AdvertisementImages.class)
    		.addAnnotatedClass(Advertisements.class)
    		.addAnnotatedClass(AdvertisementStores.class)
    		.addAnnotatedClass(Categories.class)
    		.addAnnotatedClass(Chat.class)
    		.addAnnotatedClass(ChatMessages.class)
    		.addAnnotatedClass(Cities.class)
    		.addAnnotatedClass(ClickValues.class)
    		.addAnnotatedClass(Contacts.class)
    		.addAnnotatedClass(Countries.class)
    		.addAnnotatedClass(GoogleCategories.class)
    		.addAnnotatedClass(HistoryCountClick.class)
    		.addAnnotatedClass(HistoryCourtesy.class)
    		.addAnnotatedClass(HistoryGrossClick.class)
    		.addAnnotatedClass(HistoryMostAccessed.class)
    		.addAnnotatedClass(HistoryPurchases.class)
    		.addAnnotatedClass(HistoryRecentlyAdded.class)
    		.addAnnotatedClass(HistorySearch.class)
    		.addAnnotatedClass(HistoryAdsUrl.class)
    		.addAnnotatedClass(Imobex.class)
    		.addAnnotatedClass(NewsletterLists.class)
    		.addAnnotatedClass(Orders.class)
    		.addAnnotatedClass(States.class)
    		.addAnnotatedClass(Stopwords.class)
    		.addAnnotatedClass(StoreImages.class)
    		.addAnnotatedClass(Stores.class)
    		.addAnnotatedClass(Users.class)
    		.addAnnotatedClass(WarnMe.class);
        SessionFactory sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
        arg0.getServletContext().setAttribute("sessionFactory", sessionFactory);
        Connection connection = null;
        Admin admin = null;
        ClickValues clickValues = null;
        try {
		    connection = new Connection(sessionFactory);
		    connection.beginTransaction();
		    admin = (Admin) connection.find(Admin.class, 1);
		    arg0.getServletContext().setAttribute("maxCourtesy", admin.getMax_courtesy());
		    arg0.getServletContext().setAttribute("homeTotalAdv", 0);
		    arg0.getServletContext().setAttribute("homeTotalStores", 0);
		    arg0.getServletContext().setAttribute("homeAditionalRadius", 0);
		    arg0.getServletContext().setAttribute("adminRadius", admin.getRadius());
		    arg0.getServletContext().setAttribute("adminTotalAdvActives", admin.getTotal_adv_actives());
		    arg0.getServletContext().setAttribute("adminTotalAdv", admin.getTotal_adv());
		    arg0.getServletContext().setAttribute("adminTotalStores", admin.getTotal_stores());
	        if (arg0.getServletContext().getContextPath().equals("/GirofertaWebApp")) {
	        	arg0.getServletContext().setAttribute("pathImages", admin.getPath_images_local());
	        	arg0.getServletContext().setAttribute("pathSitemap", admin.getPath_sitemap_local());
	        }
	        else {
	        	arg0.getServletContext().setAttribute("pathImages", admin.getPath_images_server());
	        	arg0.getServletContext().setAttribute("pathSitemap", admin.getPath_sitemap_server());
	        }
	        clickValues = (ClickValues) connection.find(ClickValues.class, 1); 
	        arg0.getServletContext().setAttribute("basicClickValue", clickValues.getValue());
        } finally {
            connection.closeTransaction();
        	configuration = null;
        	serviceRegistry = null;
        	metadataSources = null; 
            admin = null;
            clickValues = null;
        }
        
        // JOBs agendados
        System.out.println("JOB's **************************************************************************");
        jobs(new Connection(sessionFactory), 0, false, (String) arg0.getServletContext().getAttribute("pathImages"), (String) arg0.getServletContext().getAttribute("pathSitemap"));
//        jobSendEmailAccountPending(arg0, sessionFactory);
//        jobImportXML(sessionFactory); // 1X POR DIA (6:05)
//        jobImportImages(sessionFactory, (String) arg0.getServletContext().getAttribute("pathImages")); // 1X POR DIA (6:20)
//        jobReplyAdsInStores(sessionFactory); // 1X POR DIA (6:40)
//        jobFixesImages(sessionFactory, (String) arg0.getServletContext().getAttribute("pathImages")); // 3X POR DIA (10:20/18:20/02:20)
//        jobUpdateHighlights(sessionFactory); // 1x POR DIA (8:20)
//        jobUpdateSitemap(sessionFactory, (String) arg0.getServletContext().getAttribute("pathSitemap")); // 1X POR DIA (8:30)
        jobUpdateAddresses(sessionFactory); // 2X POR DIA (01:05/13:05)
        jobCheckImobex(sessionFactory, arg0.getServletContext().getContextPath()); // 1X POR DIA (11:05)
        jobUpdateDataAdmin(sessionFactory); // 2X POR DIA (02:05/14:05)
        jobCheckPaymentsPagSeguro(sessionFactory); // DE HORA EM HORA
        System.out.println("********************************************************************************");
	}
	
    // VERIFICA CONTAS NAO ATIVAS (AINDA EM TESTE)
/*	private void jobSendEmailAccountPending(ServletContextEvent arg0, SessionFactory sessionFactory) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(30 * 1000); // 30 segundos
			calendar = Calendar.getInstance();
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - ACCOUNT PENDING => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime()), true);
			Connection connection = ;
			new Timer().schedule(new CheckAccountPending(new Connection(sessionFactory)), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
*/	
	
	// IMPORTA XML DOS CLIENTES 1X POR DIA (6:05)
	private void jobs(Connection connection, int idUser, boolean isReImport, String pathImages, String pathSitemap) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(((60 * 60) * 24) * 1000); // 1x por dia
			calendar = Calendar.getInstance();
			if (addDay)
				if (Calendar.HOUR_OF_DAY >= 6)
					calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 6);
		    calendar.set(Calendar.MINUTE, 5);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - JOBS => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime()), true);
			new Timer().schedule(new Jobs(connection, idUser, isReImport, pathImages, pathSitemap), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
    // ATUALIZA ADDRESSES 2X POR DIA (04:05/12:05/20:05) 
	private void jobUpdateAddresses(SessionFactory sessionFactory) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(((60 * 60) * 8) * 1000); // 2x por dia
			calendar = Calendar.getInstance();
			if (addDay)
				if (Calendar.HOUR_OF_DAY >= 1)
					calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 1);
		    calendar.set(Calendar.MINUTE, 5);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - UPDATE ADDRESSES => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime()), true);
			new Timer().schedule(new UpdateAddresses(new Connection(sessionFactory)), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
	// ATUALIZA DADOS DO ADMIN 2X POR DIA (02:05/14:05)
	private void jobUpdateDataAdmin(SessionFactory sessionFactory) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(((60 * 60) * 12) * 1000); // 2x por dia
			calendar = Calendar.getInstance();
			if (addDay)
				if (Calendar.HOUR_OF_DAY >= 6)
					calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 2);
		    calendar.set(Calendar.MINUTE, 5);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - UPDATE DATA ADMIN => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime()), true);
			new Timer().schedule(new UpdateDataAdmin(new Connection(sessionFactory)), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
	// VERIFICA FTP IMOBEX 1X POR DIA (11:05)
	private void jobCheckImobex(SessionFactory sessionFactory, String appPath) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(((60 * 60) * 24) * 1000); // 1x por dia
			calendar = Calendar.getInstance();
			if (addDay)
				if (Calendar.HOUR_OF_DAY >= 6)
					calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 11);
		    calendar.set(Calendar.MINUTE, 5);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - CHECK IMOBEX => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime()), true);
			new Timer().schedule(new CheckImobex(new Connection(sessionFactory), appPath), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
    // VERIFICA PAGAMENTOS NO PAGSEGURO DE HORA EM HORA
	private void jobCheckPaymentsPagSeguro(SessionFactory sessionFactory) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long((60 * 60) * 1000); // 1 hora = 60 * 60 * 1000 (1000 = 1s)
			calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, 1);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - PAGSEGURO => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime()), true);
			new Timer().schedule(new CheckPaymentsPagSeguro(new Connection(sessionFactory)), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
	// IMPORTA XML DOS CLIENTES 1X POR DIA (6:05)
	private void jobImportXML(SessionFactory sessionFactory) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(((60 * 60) * 24) * 1000); // 1x por dia
			calendar = Calendar.getInstance();
			if (addDay)
				if (Calendar.HOUR_OF_DAY >= 6)
					calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 6);
		    calendar.set(Calendar.MINUTE, 5);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - IMPORT XML => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime()), true);
			new Timer().schedule(new ImportXML(new Connection(sessionFactory), 0), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
	// IMPORTA IMAGENS DOS ANUNCIOS 1X POR DIA (6:20)
	private void jobImportImages(SessionFactory sessionFactory, String pathImages) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(((60 * 60) * 24) * 1000); // 1x por dia
			calendar = Calendar.getInstance();
			if (addDay)
				if (Calendar.HOUR_OF_DAY >= 6)
					calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 6);
		    calendar.set(Calendar.MINUTE, 20);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - IMPORT IMAGES => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime())/* + " | PATH IMAGES: " + pathImages*/, true);
			new Timer().schedule(new ImportImages(new Connection(sessionFactory), pathImages, 0, false), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
	// REPLICA ANUNCIOS PARA AS LOJAS 1X POR DIA (6:40)
	private void jobReplyAdsInStores(SessionFactory sessionFactory) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(((60 * 60) * 24) * 1000); // 1x por dia
			calendar = Calendar.getInstance();
			if (addDay)
				if (Calendar.HOUR_OF_DAY >= 6)
					calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 6);
		    calendar.set(Calendar.MINUTE, 40);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - REPLY ADS IN STORES => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime()), true);
			new Timer().schedule(new ReplyAdsInStores(new Connection(sessionFactory), 0), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
	// CORRIGE IMAGENS DOS ANUNCIOS 4X POR DIA (10:20/18:20/02:20)
	private void jobFixesImages(SessionFactory sessionFactory, String pathImages) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(((60 * 60) * 8) * 1000); // 3x por dia
			calendar = Calendar.getInstance();
			if (addDay)
				if (Calendar.HOUR_OF_DAY >= 6)
					calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 2);
		    calendar.set(Calendar.MINUTE, 20);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - FIXES IMAGES => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime())/* + " | PATH IMAGES: " + pathImages*/, true);
			new Timer().schedule(new FixesImages(new Connection(sessionFactory), pathImages), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
	// ATUALIZA OS MAIS ACESSADOS E OS ADD RECENTEMENTE 1x POR DIA (8:20)
	private void jobUpdateHighlights(SessionFactory sessionFactory) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(((60 * 60) * 24) * 1000); // 1x por dia
			calendar = Calendar.getInstance();
			if (addDay)
				if (Calendar.HOUR_OF_DAY >= 6)
					calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 8);
		    calendar.set(Calendar.MINUTE, 20);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - UPDATE HIGHLIGHTS => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime()), true);
			new Timer().schedule(new UpdateHighLights(new Connection(sessionFactory)), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
	// GERA SITEMAP 1X POR DIA (8:30)
	private void jobUpdateSitemap(SessionFactory sessionFactory, String pathSitemap) {
		Utilities utilities = null;
		Long interval = null;
		Calendar calendar = null;
		Date initialDate = null;
		try {
			utilities = new Utilities();
			interval = new Long(((60 * 60) * 24) * 1000); // 1x por dia
			calendar = Calendar.getInstance();
			if (addDay)
				if (Calendar.HOUR_OF_DAY >= 6)
					calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 8);
		    calendar.set(Calendar.MINUTE, 30);
		    calendar.set(Calendar.SECOND, 0);
		    initialDate = calendar.getTime();
		    utilities.printDataHora(" - UPDATE SITEMAP => FIRST RUN: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime())/* + " | PATH SITEMAP: " + pathSitemap*/, true);
			new Timer().schedule(new UpdateSitemap(new Connection(sessionFactory), pathSitemap), initialDate, interval.longValue());
		} finally {
			utilities = null;
			interval = null;
			calendar = null;
			initialDate = null;
		}
	}
	
}
