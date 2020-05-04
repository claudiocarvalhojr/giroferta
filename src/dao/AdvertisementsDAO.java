package dao;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.hibernate.jdbc.ReturningWork;

import com.google.gson.Gson;

import connection.Connection;
import entity.Admin;
import entity.AdvertisementImages;
import entity.AdvertisementStores;
import entity.Advertisements;
import entity.Categories;
import entity.GoogleCategories;
import entity.HistoryCountClick;
import entity.HistoryGrossClick;
import entity.HistoryMostAccessed;
import entity.HistoryPurchases;
import entity.HistoryRecentlyAdded;
import entity.HistorySearch;
import entity.Stopwords;
import entity.Stores;
import entity.Users;
import interfaces.IDao;
import mail.MailAlert;
import spinwork.Manager;
import spinwork.util.Utils;
import utils.Utilities;

@MultipartConfig
public final class AdvertisementsDAO implements IDao {
	
	private static final String FORM = "advertisements/form.jsp";
	private static final String LIST = "advertisements/list.jsp";
	private static final String INCOMPLETE = "users/incomplete.jsp";
	private static final String IMPORT_CSV_TXT = "advertisements/importCsvTxt.jsp";
	private static final String IMPORT_XML = "advertisements/importXml.jsp";
	private static final String REPORT_BY_ADV = "advertisements/report_by_adv.jsp";
	private static final String REPORT_BY_DATE = "advertisements/report_by_date.jsp";
	private Connection connection = null;
	
	public AdvertisementsDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public String form(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = new HashMap<String, Object>();
		Advertisements advertisement = null;
		Users user = null;
		try {
			connection.beginTransaction();
			if (check(request)) {
				advertisement = (Advertisements) connection.find(Advertisements.class, Integer.parseInt(request.getParameter("advertisement.id")));
				request.setAttribute("advertisement", advertisement);
//				request.setAttribute("storeId", advertisement.getStore_id().getId());
				param.put("advertisement_id", advertisement.getId());
				request.setAttribute("listAdvertisementImages", connection.list(AdvertisementImages.QUERY_BY_ADVERTISEMENT, param));
				param.clear();
			}
			user = (Users) request.getSession().getAttribute("currentUser");
			param.put("user_id", user.getId());
			request.setAttribute("listStores", connection.list(Stores.QUERY_BY_USER, param));
			request.setAttribute("listCategories", connection.list(Categories.QUERY_ALL));
			param.clear();
			request.setAttribute("current", request.getParameter("current"));
			request.setAttribute("maximum", request.getParameter("maximum"));
			request.setAttribute("valueSearch", request.getParameter("valueSearch"));
		} finally {
			connection.closeTransaction();
			param = new HashMap<String, Object>();
			advertisement = null;
			user = null;
		}
		return FORM;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String save(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);		
		boolean isNew = false;
		Advertisements advertisement = null;
		Users user = null;
		GoogleCategories googleCategories = null;
		Date dataAtual = null;
		Map<String, Object> param = null;
		ArrayList<String> messages = null;
		AdvertisementStores advertisementStores = null;
		List<Stores> listStores = null;
		MailAlert mailAlert = null;
		Thread threadMailAlert = null;
		List<AdvertisementStores> listAdvertisementStores = null;
		
		try {
			connection.beginTransaction();
			advertisement = null;
			if (check(request))
				advertisement = (Advertisements) connection.find(Advertisements.class, Integer.parseInt(request.getParameter("advertisement.id")));
			else {
				advertisement = new Advertisements();
				isNew = true;
			}
			advertisement = (Advertisements) Manager.setObject(advertisement, request, connection);
			user = (Users) request.getSession().getAttribute("currentUser");
			googleCategories = (GoogleCategories) connection.find(GoogleCategories.class, 3690);
			dataAtual = new Date();
			if (isNew) {
				advertisement.setUser_id(user);
				advertisement.setGoogle_category_id(googleCategories);
				advertisement.setReference("");
				advertisement.setBalance(new BigDecimal(-1));
				advertisement.setQtda_images(0);
				if (user.getType_person().equals("F"))
					advertisement.setMax_images(4);
				else
					advertisement.setMax_images(8);
				advertisement.setUrl("");
				advertisement.setGross_click(0);
				advertisement.setCount_click(0);
				advertisement.setStatus(1);
				advertisement.setCreated_at(dataAtual);
			}
			messages = new ArrayList<String>();
			advertisement.setCategory_id((Categories) connection.find(Categories.class, Integer.parseInt(request.getParameter("advertisement.category_id"))));
			advertisement.setUpdated_at(dataAtual);
			if ((boolean) request.getAttribute("isValid")) {
				if (advertisement.getPrice() == null)
					advertisement.setPrice(new BigDecimal(0.00));
				if (!advertisement.getLink().equals("") && (!advertisement.getLink().startsWith("http://") && !advertisement.getLink().startsWith("https://")))
					advertisement.setLink("http://" + advertisement.getLink());
				int cbStoreId = Integer.parseInt(request.getParameter("advertisement.store_id"));
				int countAdvertisements = 0;
				param = new HashMap<String, Object>();
				if (isNew) {
					param.put("user_id", user.getId());
					listStores = (List<Stores>) connection.list(Stores.QUERY_BY_USER, param);
					param.clear();
					if (cbStoreId == 0) {
						advertisement.setAll_stores(1);
						connection.save(advertisement);
						advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
						connection.update(advertisement);
						countAdvertisements++;
						for (Stores store : listStores) {
//							countAdvertisements++;
							advertisementStores = new AdvertisementStores();
							advertisementStores.setAdvertisement_id(advertisement);
							advertisementStores.setStore_id(store);
							advertisementStores.setGross_click(0);
							advertisementStores.setCount_click(0);
							advertisementStores.setStatus(1);
							advertisementStores.setCreated_at(dataAtual);
							advertisementStores.setUpdated_at(dataAtual);
							advertisementStores.setUrl("advertisement/" + advertisement.getId() + "/" + store.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
							connection.save(advertisementStores);
						}
					}
					else {
						advertisement.setAll_stores(0);
						connection.save(advertisement);
						advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
						connection.update(advertisement);
						countAdvertisements++;
						for (Stores store : listStores) {
//							countAdvertisements++;
							advertisementStores = new AdvertisementStores();
							advertisementStores.setAdvertisement_id(advertisement);
							advertisementStores.setStore_id(store);
							advertisementStores.setGross_click(0);
							advertisementStores.setCount_click(0);
							if (cbStoreId == store.getId())
								advertisementStores.setStatus(1);
							else
								advertisementStores.setStatus(0);
							advertisementStores.setCreated_at(dataAtual);
							advertisementStores.setUpdated_at(dataAtual);
							advertisementStores.setUrl("advertisement/" + advertisement.getId() + "/" + store.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
							connection.save(advertisementStores);
						}
					}
					messages.add("advertisement.success");
					mailAlert = new MailAlert(
							"Giroferta - Novo anúncio", 
							"\n\nID: " + advertisement.getId() + 
							"\nAnúncio: " + advertisement.getTitle() + 
							"\nDescrição: " + advertisement.getDescription() + 
							"\nMarca: " + advertisement.getBrand() + 
							"\nPreço: " + advertisement.getPrice() + 
							"\n\n",
							new String[] {"golive@giroferta.com"},
							(String) request.getSession().getAttribute("giroUrl")
						);
					threadMailAlert = new Thread(mailAlert);
					threadMailAlert.start();
				}
				else {
					param.put("advertisement_id", advertisement.getId());
					listAdvertisementStores = (List<AdvertisementStores>) connection.list(AdvertisementStores.QUERY_BY_ADVERTISEMENT, param);
					param.clear();
					if (cbStoreId == 0) {
						advertisement.setAll_stores(1);
						advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
						connection.update(advertisement);
						for (AdvertisementStores advStores : listAdvertisementStores) {
							advStores.setStatus(1);
							advStores.setUpdated_at(dataAtual);
							advStores.setUrl("advertisement/" + advStores.getAdvertisement_id().getId() + "/" + advStores.getStore_id().getId() + "/" + utilities.configUrl(advertisement.getTitle()));
							connection.update(advStores);
						}
					}
					else {
						advertisement.setAll_stores(0);
						advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
						connection.update(advertisement);
						for (AdvertisementStores advStores : listAdvertisementStores) {
							if (cbStoreId == advStores.getStore_id().getId())
								advStores.setStatus(1);
							else
								advStores.setStatus(0);
							advStores.setUpdated_at(dataAtual);
							advStores.setUrl("advertisement/" + advStores.getAdvertisement_id().getId() + "/" + advStores.getStore_id().getId() + "/" + utilities.configUrl(advertisement.getTitle()));
							connection.update(advStores);
						}
					}
					messages.add("advertisement.updated");
				}
				request.setAttribute("messages", messages);
				request.setAttribute("typeMessage", "list-group-item-success");
				if (countAdvertisements > 0) {
					user.setCount_advertisements(user.getCount_advertisements() + countAdvertisements);
					connection.update(user);					
					request.getSession().setAttribute("currentUser", user);
				}
				connection.commit();
			}
			else
				request.setAttribute("typeMessage", "list-group-item-danger");
		} finally {
			connection.closeTransaction();
//			utilities = null;
			isNew = false;
			advertisement = null;
			user = null;
			googleCategories = null;
			dataAtual = null;
			param = null;
			messages = null;
			advertisementStores = null;
			listStores = null;
			mailAlert = null;
			threadMailAlert = null;
			listAdvertisementStores = null;
		}
		return form(request, response);
	}

	@SuppressWarnings("unchecked")
	public String publish(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		Advertisements advertisements = null;
		Map<String, Object> param = null;
		List<Advertisements> listAdvertisements = null;
		
		try {
			connection.beginTransaction();
			if (check(request)) {
				advertisements = (Advertisements) connection.find(Advertisements.class, Integer.parseInt(request.getParameter("advertisement.id")));
				if (advertisements.getStatus() == 1 || advertisements.getStatus() == 3) {
					if (advertisements.getUser_id().getBalance().compareTo(advertisements.getUser_id().getClick_values_id().getValue()) < 0) {
						param = new HashMap<String, Object>();
						param.put("user_id", advertisements.getUser_id().getId());
						listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_PUBLISHED_BY_USER, param);
						param.clear();
						for (Advertisements adv : listAdvertisements) {
							if (adv.getId().compareTo(advertisements.getId()) != 0) {
								adv.setStatus(1);
								connection.update(adv);
							}
						}
					}
					advertisements.setStatus(2);
				}
				else
					advertisements.setStatus(1);
				connection.update(advertisements);
				connection.commit();
			}
		} finally {
			connection.closeTransaction();
//			utilities = null;
			advertisements = null;
			param = null;
			listAdvertisements = null;
		}
		return list(request, response);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String list(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		Users user = (Users) request.getSession().getAttribute("currentUser");
		if (user.getType_account() == 11) {
			user = null;
			return INCOMPLETE;
		}
			
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		String valueSearch = null;
		Map<String, Object> param = null;
		List<?> listAdvertisements = null;
		int current = 0;		
		int maximum = 10;
		int totalResults = 0;			
		int totalPages = 0;
		String nav = null;
		try {
			connection.beginTransaction();
			param = new HashMap<String, Object>();
			valueSearch = request.getParameter("valueSearch");
			if (valueSearch == null)
				valueSearch = "";
			else
				valueSearch = valueSearch.replaceAll("%", "");
			if (request.getParameter("nav") == null) {
				param.put("user_id", user.getId());
				param.put("title", "%" + valueSearch + "%");
				totalResults = connection.count(Advertisements.QUERY_COUNT_BY_TITLE, param);
				param.clear();
				if (totalResults > 0) {
					totalPages = totalResults / maximum;
				}
				else {
					totalResults = 0;			
					totalPages = 0;
				}
				request.getSession().setAttribute("totalResults", totalResults);
				request.getSession().setAttribute("totalPages", totalPages);
			}
			else {
				nav = request.getParameter("nav");
				current = (int) request.getSession().getAttribute("current");
				maximum = (int) request.getSession().getAttribute("maximum");
				totalResults = (int) request.getSession().getAttribute("totalResults");
				totalPages = (int) request.getSession().getAttribute("totalPages");
				if (nav.equals("first"))
					current = 0;
				else if (nav.equals("previous"))
					current -= maximum;
				else if (nav.equals("next"))
					current += maximum;
				else if (nav.equals("last")) {
					if((totalResults % maximum) == 0)
						current = (totalPages - 1) * maximum;
					else
						current = totalPages * maximum;
				}
				if (current > totalResults)
					current =0;
			}
			param.put("user_id", user.getId());
			param.put("title", "%" + valueSearch + "%");
			listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_TITLE, param, current, maximum);
			param.clear();
			request.setAttribute("valueSearch", valueSearch);
			request.getSession().setAttribute("current", current);
			request.getSession().setAttribute("maximum", maximum);
			request.setAttribute("navActionFirst", request.getSession().getAttribute("linkAdvertisementsListFirst"));
			request.setAttribute("navActionPrevious", request.getSession().getAttribute("linkAdvertisementsListPrevious"));
			request.setAttribute("navActionNext", request.getSession().getAttribute("linkAdvertisementsListNext"));
			request.setAttribute("navActionLast", request.getSession().getAttribute("linkAdvertisementsListLast"));
			request.setAttribute("searchAction", request.getSession().getAttribute("linkAdvertisementsListSearch"));
			request.setAttribute("list", listAdvertisements);
		} finally {
			connection.closeTransaction();
//			utilities = null;
			valueSearch = null;
			param = null;
			listAdvertisements = null;
			user = null;
			nav = null;
		}
		return LIST;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		Advertisements advertisement = null;
		Map<String, Object> param = null;
		Date dataAtual = null;
		Users user = null;
		try {
			connection.beginTransaction();
			if (check(request)) {
				advertisement = (Advertisements) connection.find(Advertisements.class, Integer.parseInt(request.getParameter("advertisement.id")));
				if (advertisement != null) {
					param = new HashMap<String, Object>();
					dataAtual = new Date();
					advertisement.setStatus(0);
					advertisement.setUpdated_at(dataAtual);
					connection.update(advertisement);
					param.put("advertisement_id", advertisement.getId());
					List<AdvertisementStores> listAdvertisementStores = (List<AdvertisementStores>) connection.list(AdvertisementStores.QUERY_BY_ADVERTISEMENT, param);
					param.clear();
					for (AdvertisementStores advertisementStores : listAdvertisementStores) {
						advertisementStores.setStatus(0);
						advertisementStores.setUpdated_at(dataAtual);
						connection.update(advertisementStores);
					}
					param.put("advertisement_id", advertisement.getId());
					List<AdvertisementImages> listAdvertisementImages = (List<AdvertisementImages>) connection.list(AdvertisementImages.QUERY_BY_ADVERTISEMENT, param);
					param.clear();
					for (AdvertisementImages advertisementImages : listAdvertisementImages) {
						advertisementImages.setStatus(0);
						advertisementImages.setUpdated_at(dataAtual);
						connection.update(advertisementImages);
					}
					user = (Users) request.getSession().getAttribute("currentUser");
					if (user.getCount_advertisements() > 0) {
						user.setCount_advertisements(user.getCount_advertisements()-1);
						connection.update(user);
					}
					connection.commit();
					request.getSession().setAttribute("currentUser", user);
				}
			}
		} finally {
			connection.closeTransaction();
			advertisement = null;
			param = null;
			dataAtual = null;
			user = null;
		}
		return list(request, response);
	}

	public String importCsvTxt(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		Users user = null;
		Map<String, Object> param = null;
		
		try {
			connection.beginTransaction();
			user = (Users) request.getSession().getAttribute("currentUser");
			param = new HashMap<String, Object>();
			param.put("user_id", user.getId());
			request.setAttribute("listStores", connection.list(Stores.QUERY_BY_USER, param));
			request.setAttribute("listCategories", connection.list(Categories.QUERY_ALL));
			param.clear();
		} finally {
			connection.closeTransaction();
			user = null;
			param = null;
		}
		
		return IMPORT_CSV_TXT;
	}

	@SuppressWarnings("unchecked")
	public String importRunCsvTxt(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		Utils utils = new Utils();
		Users user = (Users) request.getSession().getAttribute("currentUser");
		Part part = null;
	    InputStream is = null;
	    InputStreamReader isr = null;
		BufferedReader br = null;
		ArrayList<String> messages = new ArrayList<String>();
		String splitBy = ";";
		String line = "";
		String[] tokens = null;
		int cbStoreId = Integer.parseInt(request.getParameter("cbStoreId"));
		Advertisements advertisement = null;
		AdvertisementStores advertisementStores = null;
		AdvertisementImages advertisementImages = null;
		Map<String, Object> param = null;
		List<Stores> listStores = null;
		GoogleCategories googleCategories = null;
		Date dataAtual = new Date();
		boolean isValid = true;
		boolean isNew = false;
		boolean isNewImage = false;
		URL urlImage = null;
		HttpURLConnection httpCode = null;
		InputStream imputStream = null;
		String pathImages = "";
		String nameImage = "";
		String ext = "";
		String[] tokensUrl = null;
		int i = 0;
		int count = 0;
		int countAdvertisements = 0;
		int countLines = 0;
		MailAlert mailAlert = null;
		Thread threadMailAlert = null;
		BufferedImage image = null;
		BufferedImage finalImage = null;
		File imageFile = null;
		File imagePath = null;
		String pathImage = "";		
		int posX = 0;
		int posY = 0;
		try {
			connection.beginTransaction();
			part = request.getPart("btFile");
			if (part.getSubmittedFileName().endsWith(".csv") || part.getSubmittedFileName().endsWith(".txt") && part.getSize() > 0) {
				param = new HashMap<String, Object>();
				param.put("user_id", user.getId());
				listStores = (List<Stores>) connection.list(Stores.QUERY_BY_USER, param);
				param.clear();
				googleCategories = (GoogleCategories) connection.find(GoogleCategories.class, 3690);
				is = part.getInputStream();
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				while ((line = br.readLine()) != null) {
					countLines++;
					tokens = line.split(splitBy);
					if (tokens.length < 8) {
						isValid = false;
					}
				}
				if (countLines == 0)
					isValid = false;
				countLines = 0;
				if (isValid) {
					is = part.getInputStream();
					isr = new InputStreamReader(is);
					br = new BufferedReader(isr);
					while ((line = br.readLine()) != null) {
						countLines++;
						if (countLines > 0 && countLines <= 100) {
							tokens = line.split(splitBy);
							advertisement = null;
							isNew = false;
							count = 0;
							param.put("user_id", user.getId());
							param.put("reference", tokens[0]);
							advertisement = (Advertisements) connection.find(Advertisements.QUERY_BY_REFERENCE, param);					
							param.clear();
							if (advertisement == null) {
								advertisement = new Advertisements();
								isNew = true;
							}
							if (tokens[0].equals("0"))
								advertisement.setReference("");
							else
								advertisement.setReference(tokens[0]);
							advertisement.setTitle(tokens[1]);
							advertisement.setDescription(tokens[2]);
							if (tokens[3] == null || tokens[3].equals("") || tokens[3].equals("0"))
								advertisement.setPrice(new BigDecimal(0.00));
							else
								advertisement.setPrice(utils.converteBigDecimal(tokens[3]));
							advertisement.setBrand(tokens[4]);
							if (tokens[5].equals("1"))
								advertisement.setItem_state("NOVO");
							else if (tokens[5].equals("2"))
								advertisement.setItem_state("USADO");
							else if (tokens[5].equals("3"))
								advertisement.setItem_state("RECONDICIONADO	");
							else if (tokens[5].equals("4"))
								advertisement.setItem_state("SERVIÇO");
							else
								advertisement.setItem_state("0");
							
							if (request.getParameter("ckPublicar") != null)
								advertisement.setStatus(2);
							else
								advertisement.setStatus(1);
							
							if (tokens[6].equals("1")) {
								advertisement.setAvailability("EM ESTOQUE");
								if (request.getParameter("ckPublicar") != null)
									advertisement.setStatus(2);
							}
							else if (tokens[6].equals("2"))
								advertisement.setAvailability("PRÉ-VENDA");
							else if (tokens[6].equals("3")) {
								advertisement.setAvailability("SEM ESTOQUE");
								if (request.getParameter("ckPublicar") != null)
									advertisement.setStatus(1);
							}
							else if (tokens[6].equals("4"))
								advertisement.setAvailability("SERVIÇO");
							else
								advertisement.setAvailability("0");
							if (tokens[7] != null && !tokens[7].equals("") && !tokens[7].equals("null") && tokens[7].length() > 5) {
								if(!tokens[7].startsWith("http://") && !tokens[7].startsWith("https://")) 
									advertisement.setLink("http://" + tokens[7]);
								else
									advertisement.setLink(tokens[7]);
							}
							else
								advertisement.setLink("");
							advertisement.setUser_id(user);
							advertisement.setCategory_id((Categories) connection.find(Categories.class, Integer.parseInt(request.getParameter("cbCategoryId"))));
							advertisement.setGoogle_category_id(googleCategories);
							advertisement.setAll_stores(1);
							advertisement.setBalance(new BigDecimal(-1));
							advertisement.setGross_click(0);
							advertisement.setCount_click(0);
							advertisement.setUpdated_at(dataAtual);
							if (user.getType_person().equals("F"))
								advertisement.setMax_images(4);
							else
								advertisement.setMax_images(8);
							if (isNew) {
								advertisement.setQtda_images(0);
								advertisement.setCreated_at(dataAtual);
								connection.save(advertisement);
								advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
								connection.update(advertisement);
								countAdvertisements++;
								if (cbStoreId == 0) {
									for (Stores store : listStores) {
//										countAdvertisements++;
										advertisementStores = new AdvertisementStores();								
										advertisementStores.setAdvertisement_id(advertisement);
										advertisementStores.setStore_id(store);
										advertisementStores.setGross_click(0);
										advertisementStores.setCount_click(0);
										advertisementStores.setStatus(1);
										advertisementStores.setCreated_at(dataAtual);
										advertisementStores.setUpdated_at(dataAtual);
										advertisementStores.setUrl("advertisement/" + advertisement.getId() + "/" + store.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
										connection.save(advertisementStores);
									}
								}
								else {
									for (Stores store : listStores) {
//										countAdvertisements++;
										advertisementStores = new AdvertisementStores();								
										advertisementStores.setAdvertisement_id(advertisement);
										advertisementStores.setStore_id(store);
										advertisementStores.setGross_click(0);
										advertisementStores.setCount_click(0);
										if (cbStoreId == store.getId())
											advertisementStores.setStatus(1);
										else
											advertisementStores.setStatus(0);
										advertisementStores.setCreated_at(dataAtual);
										advertisementStores.setUpdated_at(dataAtual);
										advertisementStores.setUrl("advertisement/" + advertisement.getId() + "/" + store.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
										connection.save(advertisementStores);
									}
								}
							}
							else {
								connection.update(advertisement);
								param.put("advertisement_id", advertisement.getId());
								List<AdvertisementStores> listAdvertisementStores = (List<AdvertisementStores>) connection.list(AdvertisementStores.QUERY_BY_ADVERTISEMENT, param);
								param.clear();
								if (cbStoreId == 0) {
									advertisement.setAll_stores(1);
									for (AdvertisementStores advStores : listAdvertisementStores) {
										advStores.setStatus(1);
										advStores.setUpdated_at(dataAtual);
										advStores.setUrl("advertisement/" + advStores.getAdvertisement_id().getId() + "/" + advStores.getStore_id().getId() + "/" + utilities.configUrl(advertisement.getTitle()));
										connection.update(advStores);
									}
								}
								else {
									advertisement.setAll_stores(0);
									for (AdvertisementStores advStores : listAdvertisementStores) {
										if (cbStoreId == advStores.getStore_id().getId())
											advStores.setStatus(1);
										else
											advStores.setStatus(0);
										advStores.setUpdated_at(dataAtual);
										advStores.setUrl("advertisement/" + advStores.getAdvertisement_id().getId() + "/" + advStores.getStore_id().getId() + "/" + utilities.configUrl(advertisement.getTitle()));
										connection.update(advStores);
									}
								}
							}
							if (tokens.length > 8) {
								 if (tokens[8] != null && !tokens[8].equals("") && !tokens[8].equals("null") && !tokens[8].equals("0") && tokens[8].length() > 10 && (tokens[8].startsWith("http://") || tokens[8].startsWith("https://"))) {
									urlImage = new URL(tokens[8]);
									httpCode = (HttpURLConnection) urlImage.openConnection();
									if (httpCode.getResponseCode() == 200) {
										imputStream = new BufferedInputStream(urlImage.openStream());
										if (advertisement.getQtda_images() < advertisement.getMax_images()) {
											pathImages = (String) request.getSession().getAttribute("pathImages");
											nameImage = String.valueOf(advertisement.getQtda_images());
											ext = "";
											tokensUrl = tokens[8].split("\\.");
											i = tokensUrl.length;
											if (i > 1)
												ext = tokensUrl[i-1].toLowerCase();
											if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
												pathImage = pathImages + "advertisements/" + advertisement.getId() + "/";
												imagePath = new File(pathImage);
												if (!imagePath.exists())
													imagePath.mkdirs();
												imagePath.setWritable(true, true);
												imageFile = new File(pathImage + nameImage);
												count = 0;
												do {
													count++;
													nameImage = (imagePath.list().length + count) + "." + ext;
													imageFile = new File(pathImage + nameImage);
												} while (imageFile.exists());
												image = ImageIO.read(imputStream);
												// PAISAGEM (640x480)
//												if (image.getWidth() > image.getHeight()) {
												if (image.getWidth() > image.getHeight() && image.getWidth() > 640) {
													image = utilities.resizeImage(image, 640, 480);
													posX = 0;
													posY = 0;
												} else if (image.getWidth() > image.getHeight() && image.getWidth() < 640) {
													posX = (640 - image.getWidth()) / 2;
													posY = (480 - image.getHeight()) / 2;
												}
												// RETRATO (360x480)
//												else if (image.getHeight() > image.getWidth()) {
												else if (image.getHeight() > image.getWidth() && image.getHeight() > 480) {
													image = utilities.resizeImage(image, 360, 480);
													posX = 140;
													posY = 0;
												} else if (image.getHeight() > image.getWidth() && image.getHeight() < 480) {
													posX = (640 - image.getWidth()) / 2;
													posY = (480 - image.getHeight()) / 2;
												}
												// QUADRADA (480x480)
//												else if (image.getWidth() == image.getHeight()) {
												else if (image.getWidth() == image.getHeight() && image.getWidth() > 480) {
													image = utilities.resizeImage(image, 480, 480);
													posX = 80;
													posY = 0;
												} else if (image.getWidth() == image.getHeight() && image.getWidth() < 480) {
													posX = (640 - image.getWidth()) / 2;
													posY = (480 - image.getHeight()) / 2;
												}
												finalImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
												Graphics graphics = finalImage.createGraphics();
												graphics.setColor(Color.WHITE);
												graphics.fillRect(0, 0, 640, 480);
												graphics.drawImage(image, posX, posY, null);
										        graphics.dispose();		
												imageFile.setWritable(true, true);
												ImageIO.write(finalImage, ext, imageFile);
												isNewImage = false;
												param.put("link", tokens[8]);
												advertisementImages = (AdvertisementImages) connection.find(AdvertisementImages.QUERY_BY_LINK, param);
												param.clear();
												if (advertisementImages == null) {
													advertisementImages = new AdvertisementImages();
													isNewImage = true;
												}
												advertisementImages.setAdvertisement_id(advertisement);
												advertisementImages.setImage_file_name("advertisements/" + advertisement.getId() + "/" + nameImage);
												advertisementImages.setImage_file_type(ext);
												advertisementImages.setLink(tokens[8]);
												advertisementImages.setStatus(1);
												advertisementImages.setUpdated_at(dataAtual);
												if (!advertisementImages.getLink().startsWith("http://") && !advertisementImages.getLink().startsWith("https://"))
													advertisementImages.setLink("http://" + advertisementImages.getLink());
												if (isNewImage) {
													advertisementImages.setCreated_at(dataAtual);
													connection.save(advertisementImages);
													advertisement.setQtda_images(advertisement.getQtda_images() + 1);
													connection.update(advertisement);
												}
												else
													connection.update(advertisementImages);
											}
										}
									}
								}
							}
						}
					}
					br.close();
				}
				else {
					messages.add("advertisement.importInvalidFormat");
					isValid = false;
					connection.rollback();
				}
			}
			else {
				messages.add("advertisement.importInvalidFormat");
				isValid = false;
				connection.rollback();
			}
			if (isValid) {
				if (countAdvertisements > 0) {
					user.setCount_advertisements(user.getCount_advertisements() + countAdvertisements);
					connection.update(user);					
					request.getSession().setAttribute("currentUser", user);
				}
				connection.commit();
				mailAlert = new MailAlert(
						"Giroferta - Importação de Anúncios", 
						"\n\nID: " + user.getId() + 
						"\n\nID: " + user.getId() + 
						"\nCliente: " + user.getName() + " " + user.getLast_name() + 
						"\nE-mail: " + user.getEmail() + 
						"\nTelefone: " + user.getPhone() + 
						"\nURL: " + user.getUrl() + 
						"\n\n",
						new String[] {"golive@giroferta.com"},
						(String) request.getSession().getAttribute("giroUrl")
					);
				threadMailAlert = new Thread(mailAlert);
				threadMailAlert.start();
			}
			if (isValid) {
				messages.add("advertisement.importWithSuccess");
				request.setAttribute("typeMessage", "list-group-item-success");
			}
			else {
				messages.add("advertisement.importFail");
				request.setAttribute("typeMessage", "list-group-item-danger");
			}
			request.setAttribute("listStores", listStores);
			request.setAttribute("messages", messages);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			connection.rollback();
		} catch (IOException e) {
			e.printStackTrace();
			connection.rollback();
		} catch (ServletException e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			connection.closeTransaction();
//			utilities = null;
			utils = null;
			user = null;
			part = null;
		    is = null;
		    isr = null;
			br = null;
			messages = null;
			splitBy = null;
			line = null;
			tokens = null;
			advertisement = null;
			advertisementStores = null;
			advertisementImages = null;
			param = null;
			listStores = null;
			googleCategories = null;
			dataAtual = null;
			urlImage = null;
			httpCode = null;
			imputStream = null;
			pathImages = null;
			nameImage = null;
			ext = null;
			tokensUrl = null;
			mailAlert = null;
			threadMailAlert = null;
			image = null;
			finalImage = null;
			imageFile = null;
			imagePath = null;
			pathImage = "";		
		}
		return IMPORT_CSV_TXT;
	}

	public String importXml(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
//		try {
//			connection.beginTransaction();
//			Users user = (Users) request.getSession().getAttribute("currentUser");
//			Map<String, Object> param = new HashMap<String, Object>();
//			param.put("user_id", user.getId());
//			request.setAttribute("listStores", connection.list(Stores.QUERY_LIST_ALL_BY_USER, param));
//			param.clear();
//		} finally {
//			connection.closeTransaction();
//		}
		
		return IMPORT_XML;
	}
	
	public String importRunXml(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
//		ParserXMLGoogleShopping parserXMLGoogleShopping = new ParserXMLGoogleShopping(request.getParameter("tfUrlXml"), (Users) request.getSession().getAttribute("currentUser"), connection, (String) request.getSession().getAttribute("pathImages"), (String) request.getSession().getAttribute("giroUrl"));
//		new Thread(parserXMLGoogleShopping).start();
		
//		Utilities util = new Utilities();
//		util.printNameValue(request.getParameterMap(), true);
//		
//		ArrayList<String> messages = new ArrayList<String>();
//		if (parserXMLGoogleShopping.isValid()) {
//			messages.add("advertisement.importWithSuccess");
//			request.setAttribute("typeMessage", "list-group-item-success");
//		}
//		else {
//			messages.add("advertisement.importFail");
//			request.setAttribute("typeMessage", "list-group-item-danger");
//		}
//		request.setAttribute("messages", messages);
	
		return IMPORT_XML;
	}

	@Override
	public boolean check(HttpServletRequest request) {
		boolean isCheck;
		if (request.getParameter("advertisement.id") != null && !request.getParameter("advertisement.id").equals("") && !request.getParameter("advertisement.id").equals("0"))
			isCheck = true;
		else 
			isCheck = false;
		return isCheck;
	}

	public void categories(HttpServletRequest request, HttpServletResponse response) {
		List<?> listGoogleCategories = null;
		try {
			connection.beginTransaction();
			listGoogleCategories = connection.list(GoogleCategories.QUERY_LIST_ALL_WITH_ORDINATION);
			request.setAttribute("listGoogleCategories", listGoogleCategories);
			listGoogleCategories = null;
		} finally {
			connection.closeTransaction();
			listGoogleCategories = null;
		}
	}

	public void json(HttpServletRequest request, HttpServletResponse response) {
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		JsonArrayBuilder  jsonArrayBuilderAdvertisements = Json.createArrayBuilder();
		JsonArrayBuilder  jsonArrayBuilderAdvertisementStore = Json.createArrayBuilder();
		JsonObjectBuilder jsonObjectBuilderAdvertisement = Json.createObjectBuilder();
		JsonObjectBuilder jsonObjectBuilderAdvertisementStore = Json.createObjectBuilder();
		JsonArray jsonArrayAdvertisements = null;		
		JsonArray jsonArrayAdvertisementStore = null;
		String valueSearch = request.getParameter("advertisement").toLowerCase();
		boolean isStopword = false;
		boolean isFound = false;
		int qtdaFounds = 0;
		Map<String, Object> param = null;
		List<AdvertisementStores> listAdvertisementStores = null;
		HistorySearch historySearch = null;
		String image_file_name = null;
//		int cont = 0;
//		String[] arrayAux = valueSearch.split(" ");
		try {
			connection.beginTransaction();
//			for (int i = 0; i < arrayAux.length; i++) {
//				cont++;
//				System.out.println(cont + "º) termo: " + arrayAux[i]);
//				isStopword = false;
//				param.put("stopword", arrayAux[i]);
//				if (connection.find(Stopwords.QUERY_STOPWORDS, param) != null)
//					isStopword = true;
//				param.clear();
//			}
			param = new HashMap<String, Object>();
			param.put("stopword", valueSearch);
			if (connection.find(Stopwords.QUERY_STOPWORDS, param) != null) {
				isStopword = true;
			}
			param.clear();
			valueSearch = valueSearch.replaceAll("%", "");
			valueSearch = valueSearch.replaceAll("insert", "");
			valueSearch = valueSearch.replaceAll("update", "");
			valueSearch = valueSearch.replaceAll("delete", "");
			valueSearch = valueSearch.replaceAll("create", "");
			valueSearch = valueSearch.replaceAll("alter", "");
			valueSearch = valueSearch.replaceAll("drop", "");
			if (valueSearch != null && !valueSearch.equals("") && valueSearch.length() > 1 && !isStopword) {
				listAdvertisementStores = getAdvertisementsHome(valueSearch, request.getParameter("lat"), request.getParameter("lng"), 25, request);
				if (!listAdvertisementStores.isEmpty()) { 
					isFound = true;
					qtdaFounds += listAdvertisementStores.size();
				}
				for (AdvertisementStores advertisementStores : listAdvertisementStores) {
					if (advertisementStores.getAdvertisement_id().getStatus() == 2) {
						jsonObjectBuilderAdvertisementStore
							.add("id", advertisementStores.getStore_id().getId())
							.add("name", advertisementStores.getStore_id().getName())
							.add("filial", advertisementStores.getStore_id().getFilial())
							.add("city", advertisementStores.getStore_id().getCity_id().getName())
							.add("state", advertisementStores.getStore_id().getCity_id().getState_id().getInitials())
							.add("neighborhood", advertisementStores.getStore_id().getNeighborhood())
							.add("address", advertisementStores.getStore_id().getAddress())
							.add("number", advertisementStores.getStore_id().getNumber())
							.add("complement", advertisementStores.getStore_id().getComplement())
							.add("postal_code", advertisementStores.getStore_id().getPostal_code())
							.add("latitude", advertisementStores.getStore_id().getLatitude())
							.add("longitude", advertisementStores.getStore_id().getLongitude())
							.add("contact_phone", advertisementStores.getStore_id().getContact_phone())
							.add("contact_email", advertisementStores.getStore_id().getContact_email())
							.add("logo", advertisementStores.getStore_id().getImage_file_name());
						jsonArrayBuilderAdvertisementStore.add(jsonObjectBuilderAdvertisementStore.build());
						jsonArrayAdvertisementStore = jsonArrayBuilderAdvertisementStore.build();
						if (advertisementStores.getAdvertisement_id().getListAdvertisementImages().size() > 0)
							image_file_name = advertisementStores.getAdvertisement_id().getListAdvertisementImages().get(0).getImage_file_name();
						else
							image_file_name = "";
						jsonObjectBuilderAdvertisement
							.add("advertisementStoresId", advertisementStores.getAdvertisement_id().getId()+"-"+advertisementStores.getStore_id().getId())
							.add("id", advertisementStores.getAdvertisement_id().getId())
							.add("title", advertisementStores.getAdvertisement_id().getTitle())
							.add("price", advertisementStores.getAdvertisement_id().getPrice())
							.add("item_state", advertisementStores.getAdvertisement_id().getItem_state())
							.add("availability", advertisementStores.getAdvertisement_id().getAvailability())
							.add("brand", advertisementStores.getAdvertisement_id().getBrand())
//							.add("link", advertisementStores.getAdvertisement_id().getLink())
							.add("url", advertisementStores.getAdvertisement_id().getUrl())
							.add("image_file_name", image_file_name)
							.add("store", jsonArrayAdvertisementStore);
						jsonArrayBuilderAdvertisements.add(jsonObjectBuilderAdvertisement.build());
					}
				}
				jsonArrayAdvertisements = jsonArrayBuilderAdvertisements.build();
				try {
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonArrayAdvertisements.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			historySearch = new HistorySearch();
			if (request.getSession().getAttribute("currentUser") != null)
				historySearch.setLogin(1);
			else
				historySearch.setLogin(0);
			historySearch.setTerm(valueSearch);
			historySearch.setAmount(qtdaFounds);
			if (isFound)
				historySearch.setFound(1);
			else
				historySearch.setFound(0);
			historySearch.setSource("H"); // H = home
			historySearch.setLatitude(request.getParameter("lat"));
			historySearch.setLongitude(request.getParameter("lng"));
			historySearch.setCreated_at(new Date());
			connection.save(historySearch);
			connection.commit();
		} finally {
			connection.closeTransaction();
//			utilities = null;
			jsonArrayBuilderAdvertisements = null;
			jsonArrayBuilderAdvertisementStore = null;
			jsonObjectBuilderAdvertisement = null;
			jsonObjectBuilderAdvertisementStore = null;
			jsonArrayAdvertisements = null;		
			jsonArrayAdvertisementStore = null;
			valueSearch = null;
			param = null;
			listAdvertisementStores = null;
			historySearch = null;
//			cont = null;
//			arrayAux = null;
		}
	}

	public void jsonHome(HttpServletRequest request, HttpServletResponse response) {
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		JsonArrayBuilder  jsonArrayBuilderAdvertisements = Json.createArrayBuilder();
		JsonArrayBuilder  jsonArrayBuilderAdvertisementStore = Json.createArrayBuilder();
		JsonObjectBuilder jsonObjectBuilderAdvertisement = Json.createObjectBuilder();
		JsonObjectBuilder jsonObjectBuilderAdvertisementStore = Json.createObjectBuilder();
		JsonArray jsonArrayAdvertisements = null;		
		JsonArray jsonArrayAdvertisementStore = null;
		List<AdvertisementStores> listAdvertisementStores = null;
		String image_file_name = null;
		try {
			connection.beginTransaction();
			listAdvertisementStores = getAdvertisementsHome("%%", request.getParameter("lat"), request.getParameter("lng"), 25, request);
			for (AdvertisementStores advertisementStores : listAdvertisementStores) {
				if (advertisementStores.getAdvertisement_id().getStatus() == 2) {
					jsonObjectBuilderAdvertisementStore
						.add("id", advertisementStores.getStore_id().getId())
						.add("name", advertisementStores.getStore_id().getName())
						.add("filial", advertisementStores.getStore_id().getFilial())
						.add("city", advertisementStores.getStore_id().getCity_id().getName())
						.add("state", advertisementStores.getStore_id().getCity_id().getState_id().getInitials())
						.add("neighborhood", advertisementStores.getStore_id().getNeighborhood())
						.add("address", advertisementStores.getStore_id().getAddress())
						.add("number", advertisementStores.getStore_id().getNumber())
						.add("complement", advertisementStores.getStore_id().getComplement())
						.add("postal_code", advertisementStores.getStore_id().getPostal_code())
						.add("latitude", advertisementStores.getStore_id().getLatitude())
						.add("longitude", advertisementStores.getStore_id().getLongitude())
						.add("contact_phone", advertisementStores.getStore_id().getContact_phone())
						.add("contact_email", advertisementStores.getStore_id().getContact_email())
						.add("logo", advertisementStores.getStore_id().getUser_id().getImage_file_name());
					jsonArrayBuilderAdvertisementStore.add(jsonObjectBuilderAdvertisementStore.build());
					jsonArrayAdvertisementStore = jsonArrayBuilderAdvertisementStore.build();
					if (advertisementStores.getAdvertisement_id().getListAdvertisementImages().size() > 0)
						image_file_name = advertisementStores.getAdvertisement_id().getListAdvertisementImages().get(0).getImage_file_name();
					else
						image_file_name = "";
					jsonObjectBuilderAdvertisement
						.add("advertisementStoresId", advertisementStores.getAdvertisement_id().getId()+"-"+advertisementStores.getStore_id().getId())
						.add("id", advertisementStores.getAdvertisement_id().getId())
						.add("title", advertisementStores.getAdvertisement_id().getTitle())
						.add("price", advertisementStores.getAdvertisement_id().getPrice())
						.add("item_state", advertisementStores.getAdvertisement_id().getItem_state())
						.add("availability", advertisementStores.getAdvertisement_id().getAvailability())
						.add("brand", advertisementStores.getAdvertisement_id().getBrand())
//						.add("link", advertisementStores.getAdvertisement_id().getLink())
						.add("url", advertisementStores.getAdvertisement_id().getUrl())
						.add("image_file_name", image_file_name)
						.add("store", jsonArrayAdvertisementStore);
					jsonArrayBuilderAdvertisements.add(jsonObjectBuilderAdvertisement.build());
				}
			}
			jsonArrayAdvertisements = jsonArrayBuilderAdvertisements.build();
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonArrayAdvertisements.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			connection.closeTransaction();
//			utilities = null;
			jsonArrayBuilderAdvertisements = null;
			jsonArrayBuilderAdvertisementStore = null;
			jsonObjectBuilderAdvertisement = null;
			jsonObjectBuilderAdvertisementStore = null;
			jsonArrayAdvertisements = null;		
			jsonArrayAdvertisementStore = null;
			listAdvertisementStores = null;
		}
	}

	public void jsonAdvertisement(HttpServletRequest request, HttpServletResponse response) {
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		JsonArrayBuilder  jsonArrayBuilderAdvertisements = Json.createArrayBuilder();
		JsonArrayBuilder  jsonArrayBuilderAdvertisementStore = Json.createArrayBuilder();
		JsonObjectBuilder jsonObjectBuilderAdvertisement = Json.createObjectBuilder();
		JsonObjectBuilder jsonObjectBuilderAdvertisementStore = Json.createObjectBuilder();
		JsonArray jsonArrayAdvertisements = null;		
		JsonArray jsonArrayAdvertisementStore = null;
		Map<String, Object> param = null;
		String[] advStores = null;
		AdvertisementStores advertisementStores = null;
		String image_file_name = null;
		try {
			connection.beginTransaction();
			param = new HashMap<String, Object>();
			advStores = request.getParameter("currentAdvertisement").split("-");
			param.put("advertisement_id", Integer.parseInt(advStores[0]));
			param.put("store_id", Integer.parseInt(advStores[1]));
			advertisementStores = (AdvertisementStores) connection.find(AdvertisementStores.QUERY_BY_ADVERTISEMENTSTORES, param);
			param.clear();
			if (advertisementStores.getAdvertisement_id().getStatus() == 2) {
				jsonObjectBuilderAdvertisementStore
					.add("id", advertisementStores.getStore_id().getId())
					.add("name", advertisementStores.getStore_id().getName())
					.add("filial", advertisementStores.getStore_id().getFilial())
					.add("city", advertisementStores.getStore_id().getCity_id().getName())
					.add("state", advertisementStores.getStore_id().getCity_id().getState_id().getInitials())
					.add("neighborhood", advertisementStores.getStore_id().getNeighborhood())
					.add("address", advertisementStores.getStore_id().getAddress())
					.add("number", advertisementStores.getStore_id().getNumber())
					.add("complement", advertisementStores.getStore_id().getComplement())
					.add("postal_code", advertisementStores.getStore_id().getPostal_code())
					.add("latitude", advertisementStores.getStore_id().getLatitude())
					.add("longitude", advertisementStores.getStore_id().getLongitude())
					.add("contact_phone", advertisementStores.getStore_id().getContact_phone())
					.add("contact_email", advertisementStores.getStore_id().getContact_email())
					.add("logo", advertisementStores.getStore_id().getUser_id().getImage_file_name());
				jsonArrayBuilderAdvertisementStore.add(jsonObjectBuilderAdvertisementStore.build());
				jsonArrayAdvertisementStore = jsonArrayBuilderAdvertisementStore.build();
				if (advertisementStores.getAdvertisement_id().getListAdvertisementImages().size() > 0)
					image_file_name = advertisementStores.getAdvertisement_id().getListAdvertisementImages().get(0).getImage_file_name();
				else
					image_file_name = "";
				jsonObjectBuilderAdvertisement
					.add("advertisementStoresId", advertisementStores.getAdvertisement_id().getId()+"-"+advertisementStores.getStore_id().getId())
					.add("id", advertisementStores.getAdvertisement_id().getId())
					.add("title", advertisementStores.getAdvertisement_id().getTitle())
					.add("price", advertisementStores.getAdvertisement_id().getPrice())
					.add("item_state", advertisementStores.getAdvertisement_id().getItem_state())
					.add("availability", advertisementStores.getAdvertisement_id().getAvailability())
					.add("brand", advertisementStores.getAdvertisement_id().getBrand())
//					.add("link", advertisementStores.getAdvertisement_id().getLink())
					.add("url", advertisementStores.getAdvertisement_id().getUrl())
					.add("image_file_name", image_file_name)
					.add("store", jsonArrayAdvertisementStore);
				jsonArrayBuilderAdvertisements.add(jsonObjectBuilderAdvertisement.build());
			}
			jsonArrayAdvertisements = jsonArrayBuilderAdvertisements.build();
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonArrayAdvertisements.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			connection.closeTransaction();
//			utilities = null;
			jsonArrayBuilderAdvertisements = null;
			jsonArrayBuilderAdvertisementStore = null;
			jsonObjectBuilderAdvertisement = null;
			jsonObjectBuilderAdvertisementStore = null;
			jsonArrayAdvertisements = null;		
			jsonArrayAdvertisementStore = null;
			advertisementStores = null;
			advStores = null;
			param = null;
		}
	}

	@SuppressWarnings("unchecked")
	public void jsonAdvStores(HttpServletRequest request, HttpServletResponse response) {
		JsonArrayBuilder  jsonArrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		JsonArray jsonArray = null;
		List<AdvertisementStores> listAdvertisementStores = null;
		Map<String, Object> param = null; 
		try {
			connection.beginTransaction();
			param = new HashMap<String, Object>();
			param.put("advertisement_id", Integer.parseInt(request.getParameter("advertisement_id")));
			listAdvertisementStores = (List<AdvertisementStores>) connection.list(AdvertisementStores.QUERY_BY_ADVERTISEMENT, param);
			param.clear();
			for (AdvertisementStores advertisementStores : listAdvertisementStores) {
				if (advertisementStores.getAdvertisement_id().getStatus() == 2) {
					jsonObjectBuilder
						.add("id", advertisementStores.getStore_id().getId())
						.add("name", advertisementStores.getStore_id().getName())
						.add("filial", advertisementStores.getStore_id().getFilial())
						.add("city", advertisementStores.getStore_id().getCity_id().getName())
						.add("state", advertisementStores.getStore_id().getCity_id().getState_id().getInitials())
						.add("neighborhood", advertisementStores.getStore_id().getNeighborhood())
						.add("address", advertisementStores.getStore_id().getAddress())
						.add("number", advertisementStores.getStore_id().getNumber())
						.add("complement", advertisementStores.getStore_id().getComplement())
						.add("postal_code", advertisementStores.getStore_id().getPostal_code())
						.add("latitude", advertisementStores.getStore_id().getLatitude())
						.add("longitude", advertisementStores.getStore_id().getLongitude())
						.add("contact_phone", advertisementStores.getStore_id().getContact_phone())
						.add("contact_email", advertisementStores.getStore_id().getContact_email())
						.add("logo", advertisementStores.getStore_id().getUser_id().getImage_file_name());
					jsonArrayBuilder.add(jsonObjectBuilder.build());
				}
			}
			jsonArray = jsonArrayBuilder.build();
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonArray.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			connection.closeTransaction();
			jsonArrayBuilder = null;
			jsonObjectBuilder = null;
			jsonArray = null;
			listAdvertisementStores = null;
			param = null;
		}
	}

	public void count(HttpServletRequest request, HttpServletResponse response) {
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Date dataAtual = null;
		Advertisements advertisement = null;
		AdvertisementStores advertisementStores = null;
		Map<String, Object> param = null;
		HistoryCountClick historyCountClick = null;
		String[] advertisementsIds = null;
		String[] advStores = null;
		try {
			connection.beginTransaction();
			if (request.getParameter("advertisement_id") != null) {
				advertisementsIds = request.getParameter("advertisement_id").split(",");
				dataAtual = new Date();
				param = new HashMap<String, Object>();
				for (int i=0; i<advertisementsIds.length; i++) {
					advStores = advertisementsIds[i].split("-");
					if (advStores.length > 1) {
						param.put("advertisement_id", Integer.parseInt(advStores[0]));
						param.put("store_id", Integer.parseInt(advStores[1]));
						advertisementStores = (AdvertisementStores) connection.find(AdvertisementStores.QUERY_BY_ADVERTISEMENTSTORES, param);
						param.clear();
						advertisement = advertisementStores.getAdvertisement_id();
					}
					else {
						param.put("advertisement_id", Integer.parseInt(advStores[0]));
						advertisement = (Advertisements) connection.find(Advertisements.class, Integer.parseInt(advStores[0]));
						param.clear();
					}
					if (request.getSession().getAttribute("click_adv_id_" + advertisement.getId()) == null) {
						historyCountClick = new HistoryCountClick();
						historyCountClick.setAdvertisement_id(advertisement);
						if (advStores.length > 1 && advertisementStores != null)
							historyCountClick.setStore_id(advertisementStores.getStore_id());
						else
							historyCountClick.setStore_id(null);
						if (request.getSession().getAttribute("currentUser") != null)
							historyCountClick.setLogin(1);
						else
							historyCountClick.setLogin(0);
						historyCountClick.setLatitude(request.getParameter("lat"));
						historyCountClick.setLongitude(request.getParameter("lng"));
						historyCountClick.setCreated_at(dataAtual);
						connection.update(historyCountClick);
					}
					discountClick(advertisementStores, advertisement, advStores.length, "C", dataAtual, request);
				}
				connection.commit();
			}
		} finally {
			connection.closeTransaction();
			utilities = null;
			dataAtual = null;
			advertisement = null;
			advertisementStores = null;
			param = null;
			historyCountClick = null;
			advertisementsIds = null;
			advStores = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void discountClick(AdvertisementStores advertisementStores, Advertisements advertisement, int advStores, String source, Date dataAtual, HttpServletRequest request) {
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		HistoryGrossClick historyGrossClick = null;
		Map<String, Object> param = null;
		List<Advertisements> listAdvertisements = null;
		Users user = (Users) request.getSession().getAttribute("currentUser");
		try {
			if (advertisement != null && request.getSession().getAttribute("click_adv_id_" + advertisement.getId()) == null) {
				// Se saldo for maior do que o valor do clique e se for PJ 
				// OU 
				// se saldo for maior do que o valor do clique, se PF e se possuir mais do que 1 anúncio ativo  
				if ((advertisement.getUser_id().getBalance().compareTo(advertisement.getUser_id().getClick_values_id().getValue()) >= 0 && 
						advertisement.getUser_id().getType_person().equals("J")) || 
						(advertisement.getUser_id().getBalance().compareTo(advertisement.getUser_id().getClick_values_id().getValue()) >= 0 && 
								advertisement.getUser_id().getType_person().equals("F") && 
								advertisement.getUser_id().getCount_advertisements() > 1)) {
					
					if (user == null || (user != null && user.getId() != advertisement.getUser_id().getId())) {
						// debita o valor do clique do saldo
						advertisement.getUser_id().setBalance(advertisement.getUser_id().getBalance().subtract(advertisement.getUser_id().getClick_values_id().getValue()));
						connection.update(advertisement.getUser_id());
//						util.printDataHora(" | " + (i+1) + ") SALDO: " + advertisement.getUser_id().getBalance() + " | ANÚNCIO: " + advertisement.getId() + " | USUARIO: " + advertisement.getUser_id().getId() + " | EMAIL: " + advertisement.getUser_id().getEmail() + " | TITULO: " + advertisement.getTitle(), isPrint);
						// Se saldo for menor do que o valor do clique, congela todos os anuncios do usuario
						if (advertisement.getUser_id().getBalance().compareTo(advertisement.getUser_id().getClick_values_id().getValue()) < 0) {
//							util.printDataHora(" | SEM SALDO: " + advertisement.getUser_id().getBalance() + " | ANÚNCIO: " + advertisement.getId() + " | USUARIO: " + advertisement.getUser_id().getId() + " | EMAIL: " + advertisement.getUser_id().getEmail() + " | TITULO: " + advertisement.getTitle(), isPrint);
							param = new HashMap<String, Object>();
							param.put("user_id", advertisement.getUser_id().getId());
							listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_PUBLISHED_BY_USER, param);
							param.clear();
							for (Advertisements aux : listAdvertisements) {
								aux.setStatus(3);
								connection.update(aux);
							}
						}
						
						// contabilza apenas os cliques que debitam do saldo (count_click)
						advertisement.setCount_click(advertisement.getCount_click() + 1);
						connection.update(advertisement);
						if (advStores > 1)
							request.getSession().setAttribute("click_adv_id_" + advertisement.getId(), advertisement.getId());
						else
							request.getSession().setAttribute("adv_id_" + advertisement.getId(), advertisement.getId());
						if (advStores > 1) { // para qdo store for != null no par adv-sto
							advertisementStores.setCount_click(advertisementStores.getCount_click() + 1);
							connection.update(advertisementStores);
						}
					}
				}
			}
			// contabiliza todos os cliques (bruto) (gross_click)
			if (advertisement != null) {
				advertisement.setGross_click(advertisement.getGross_click() + 1);
				connection.update(advertisement);
				historyGrossClick = new HistoryGrossClick();
				historyGrossClick.setAdvertisement_id(advertisement);
				if (advStores > 1)
					historyGrossClick.setStore_id(advertisementStores.getStore_id());
				else
					historyGrossClick.setStore_id(null);
				if (user != null)
					historyGrossClick.setLogin(1);
				else
					historyGrossClick.setLogin(0);
				historyGrossClick.setLatitude(request.getParameter("lat"));
				historyGrossClick.setLongitude(request.getParameter("lng"));
				historyGrossClick.setSource(source);
				historyGrossClick.setCreated_at(dataAtual);
				connection.update(historyGrossClick);
				request.getSession().setAttribute("advertisement_id_" + advertisement.getId(), advertisement.getId());
				if (advStores > 1) { // para qdo store for != null no par adv-sto
					advertisementStores.setGross_click(advertisementStores.getGross_click() + 1);
					connection.update(advertisementStores);
				}
			}
		} finally {
			utilities = null;
			historyGrossClick = null;
			param = null;
			listAdvertisements = null;
			user = null;
		}
	}
	
	public void purchases(HttpServletRequest request, HttpServletResponse response) {
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		AdvertisementStores advertisementStores = null;
		HistoryPurchases historyPurchases = null;
		HistoryGrossClick historyGrossClick = null;
		Map<String, Object> param = null;
		Date dataAtual = null;
		String[] advStores = null;
		try {
			connection.beginTransaction();
			dataAtual = new Date();
			advStores = request.getParameter("advertisement_id").split("-");
			param = new HashMap<String, Object>();
			param.put("advertisement_id", Integer.parseInt(advStores[0]));
			param.put("store_id", Integer.parseInt(advStores[1]));
			advertisementStores = (AdvertisementStores) connection.find(AdvertisementStores.QUERY_BY_ADVERTISEMENTSTORES, param);
			param.clear();
			historyPurchases = new HistoryPurchases();
			historyPurchases.setAdvertisement_id(advertisementStores.getAdvertisement_id());
			if (advStores.length > 1 && advertisementStores != null)
				historyPurchases.setStore_id(advertisementStores.getStore_id());
			else
				historyPurchases.setStore_id(null);
			if (request.getSession().getAttribute("currentUser") != null)
				historyPurchases.setLogin(1);
			else
				historyPurchases.setLogin(0);
			historyPurchases.setLatitude(request.getParameter("lat"));
			historyPurchases.setLongitude(request.getParameter("lng"));
			historyPurchases.setCreated_at(dataAtual);
			connection.update(historyPurchases);
			
			historyGrossClick = new HistoryGrossClick();
			historyGrossClick.setAdvertisement_id(advertisementStores.getAdvertisement_id());
			if (advStores.length > 1 && advertisementStores != null)
				historyGrossClick.setStore_id(advertisementStores.getStore_id());
			else
				historyGrossClick.setStore_id(null);
			if (request.getSession().getAttribute("currentUser") != null)
				historyGrossClick.setLogin(1);
			else
				historyGrossClick.setLogin(0);
			historyGrossClick.setLatitude(request.getParameter("lat"));
			historyGrossClick.setLongitude(request.getParameter("lng"));
			historyGrossClick.setSource("P");
			historyGrossClick.setCreated_at(dataAtual);
			connection.update(historyGrossClick);
			
			connection.commit();
		} finally {
			connection.closeTransaction();
			utilities = null;
			advertisementStores = null;
			historyPurchases = null;
			historyGrossClick = null;
			param = null;
			dataAtual = null;
			advStores = null;
		}
	}
	
	public void countMostAccessed(HttpServletRequest request, HttpServletResponse response) {
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		AdvertisementStores advertisementStores = null;
		Advertisements advertisement = null;
		HistoryMostAccessed historyMostAccessed;
		Map<String, Object> param = null;
		Date dataAtual = null;
		String[] advStores = null;
		try {
			connection.beginTransaction();
			dataAtual = new Date();
			advStores = request.getParameter("advertisement_id").split("-");
			param = new HashMap<String, Object>();
			param.put("advertisement_id", Integer.parseInt(advStores[0]));
			param.put("store_id", Integer.parseInt(advStores[1]));
			advertisementStores = (AdvertisementStores) connection.find(AdvertisementStores.QUERY_BY_ADVERTISEMENTSTORES, param);
			param.clear();
			historyMostAccessed = new HistoryMostAccessed();
			if (advStores.length > 1 && advertisementStores != null) {
				historyMostAccessed.setAdvertisement_id(advertisementStores.getAdvertisement_id());
				historyMostAccessed.setStore_id(advertisementStores.getStore_id());
			}
			else {
				param.put("advertisement_id", Integer.parseInt(advStores[0]));
				advertisement = (Advertisements) connection.find(Advertisements.QUERY_BY_ID, param);
				param.clear();
				historyMostAccessed.setAdvertisement_id(advertisement);
				historyMostAccessed.setStore_id(null);
			}
			if (request.getSession().getAttribute("currentUser") != null)
				historyMostAccessed.setLogin(1);
			else
				historyMostAccessed.setLogin(0);
			historyMostAccessed.setLatitude(request.getParameter("lat"));
			historyMostAccessed.setLongitude(request.getParameter("lng"));
			historyMostAccessed.setCreated_at(dataAtual);
			connection.update(historyMostAccessed);
			if (advertisementStores != null)
				discountClick(advertisementStores, advertisementStores.getAdvertisement_id(), advStores.length, "R", dataAtual, request);
			else
				discountClick(null, advertisement, 0, "R", dataAtual, request);

/*			historyMostAccessed.setAdvertisement_id(advertisementStores.getAdvertisement_id());
			if (advStores.length > 1 && advertisementStores != null)
				historyMostAccessed.setStore_id(advertisementStores.getStore_id());
			else
				historyMostAccessed.setStore_id(null);
			if (request.getSession().getAttribute("currentUser") != null)
				historyMostAccessed.setLogin(1);
			else
				historyMostAccessed.setLogin(0);
			historyMostAccessed.setLatitude(request.getParameter("lat"));
			historyMostAccessed.setLongitude(request.getParameter("lng"));
			historyMostAccessed.setCreated_at(dataAtual);
			connection.update(historyMostAccessed);
			discountClick(advertisementStores, advertisementStores.getAdvertisement_id(), advStores.length, "M", dataAtual, request);
*/			connection.commit();
		} finally {
			connection.closeTransaction();
			utilities = null;
			advertisementStores = null;
			advertisement = null;
			historyMostAccessed = null;
			param = null;
			dataAtual = null;
			advStores = null;
		}
	}

	public void countRecentlyAdded(HttpServletRequest request, HttpServletResponse response) {
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		AdvertisementStores advertisementStores = null;
		Advertisements advertisement = null;
		HistoryRecentlyAdded historyRecentlyAdded = null;
		Map<String, Object> param = null;
		Date dataAtual = null;
		String[] advStores = null;
		try {
			connection.beginTransaction();
			dataAtual = new Date();
			advStores = request.getParameter("advertisement_id").split("-");
			param = new HashMap<String, Object>();
			param.put("advertisement_id", Integer.parseInt(advStores[0]));
			param.put("store_id", Integer.parseInt(advStores[1]));
			advertisementStores = (AdvertisementStores) connection.find(AdvertisementStores.QUERY_BY_ADVERTISEMENTSTORES, param);
			param.clear();
			historyRecentlyAdded = new HistoryRecentlyAdded();
			if (advStores.length > 1 && advertisementStores != null) {
				historyRecentlyAdded.setAdvertisement_id(advertisementStores.getAdvertisement_id());
				historyRecentlyAdded.setStore_id(advertisementStores.getStore_id());
			}
			else {
				param.put("advertisement_id", Integer.parseInt(advStores[0]));
				advertisement = (Advertisements) connection.find(Advertisements.QUERY_BY_ID, param);
				param.clear();
				historyRecentlyAdded.setAdvertisement_id(advertisement);
				historyRecentlyAdded.setStore_id(null);
			}
			if (request.getSession().getAttribute("currentUser") != null)
				historyRecentlyAdded.setLogin(1);
			else
				historyRecentlyAdded.setLogin(0);
			historyRecentlyAdded.setLatitude(request.getParameter("lat"));
			historyRecentlyAdded.setLongitude(request.getParameter("lng"));
			historyRecentlyAdded.setCreated_at(dataAtual);
			connection.update(historyRecentlyAdded);
			if (advertisementStores != null)
				discountClick(advertisementStores, advertisementStores.getAdvertisement_id(), advStores.length, "R", dataAtual, request);
			else
				discountClick(null, advertisement, 0, "R", dataAtual, request);
			connection.commit();
		} finally {
			connection.closeTransaction();
			utilities = null;
			advertisementStores = null;
			advertisement = null;
			historyRecentlyAdded = null;
			param = null;
			dataAtual = null;
			advStores = null;
		}
	}

	public void countAdsUrl(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("acesso por ads url...");
	}

	public void countGrossClick(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("acesso repetido...");
	}

	@SuppressWarnings("unchecked")
	public String reportByAdv(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users user = null;
		Map<String, Object> param = null;
		String valueSearch = null;
		List<Advertisements> listAdvertisements = null;
		List<Advertisements> listCopy = null;
		int current = 0;		
		int maximum = 10;
		int totalResults = 0;			
		int totalPages = 0;
		String nav = null;
		String type_report = null;
		try {
			connection.beginTransaction();
			user = (Users) request.getSession().getAttribute("currentUser");
			param = new HashMap<String, Object>();
			valueSearch = request.getParameter("valueSearch");
			if (valueSearch == null)
				valueSearch = "";
			else
				valueSearch = valueSearch.replaceAll("%", "");
			if (request.getParameter("nav") == null) {
				param.put("user_id", user.getId());
				totalResults = connection.count(Advertisements.QUERY_COUNT_REPORT_AND_USER, param);
				param.clear();
				if (totalResults > 0) {
					totalPages = totalResults / maximum;
				}
				else {
					totalResults = 0;			
					totalPages = 0;
				}
				request.getSession().setAttribute("totalResults", totalResults);
				request.getSession().setAttribute("totalPages", totalPages);
			}
			else {
				nav = request.getParameter("nav");
				current = (int) request.getSession().getAttribute("current");
				maximum = (int) request.getSession().getAttribute("maximum");
				totalResults = (int) request.getSession().getAttribute("totalResults");
				totalPages = (int) request.getSession().getAttribute("totalPages");
				if (nav.equals("first"))
					current = 0;
				else if (nav.equals("previous"))
					current -= maximum;
				else if (nav.equals("next"))
					current += maximum;
				else if (nav.equals("last")) {
					if((totalResults % maximum) == 0)
						current = (totalPages - 1) * maximum;
					else
						current = totalPages * maximum;
				}
				if (current > totalResults)
					current =0;
			}
			param.put("user_id", user.getId());
			type_report = request.getParameter("type_report");
			if (type_report == null)
				type_report = (String) request.getSession().getAttribute("type_report");
			if (type_report != null) {
				request.getSession().setAttribute("type_report", type_report);
				if (type_report.equals("gross")) {
					request.setAttribute("subtitleReport", "Total de Cliques");
					listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_GROSS_CLICK_AND_USER, param, current, maximum);
					listCopy = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_GROSS_CLICK_AND_USER, param, current, maximum);
				}
				else if (type_report.equals("count")) {
					request.setAttribute("subtitleReport", "Contabilizados");
					listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_COUNT_CLICK_AND_USER, param, current, maximum);
					listCopy = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_COUNT_CLICK_AND_USER, param, current, maximum);
				}
				else if (type_report.equals("purchase")) {
					request.setAttribute("subtitleReport", "Botão Comprar");
					listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_PURCHASE_AND_USER, param, current, maximum);
					listCopy = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_PURCHASE_AND_USER, param, current, maximum);
				}
			}
			else {
				request.setAttribute("subtitleReport", "Contabilizados");
				listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_COUNT_CLICK_AND_USER, param, current, maximum);
				listCopy = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_COUNT_CLICK_AND_USER, param, current, maximum);
			}
			Collections.reverse(listAdvertisements);			
			param.clear();
			request.setAttribute("valueSearch", valueSearch);
			request.getSession().setAttribute("current", current);
			request.getSession().setAttribute("maximum", maximum);
			request.setAttribute("navActionFirst", request.getSession().getAttribute("linkAdvertisementsReportByAdvFirst"));
			request.setAttribute("navActionPrevious", request.getSession().getAttribute("linkAdvertisementsReportByAdvPrevious"));
			request.setAttribute("navActionNext", request.getSession().getAttribute("linkAdvertisementsReportByAdvNext"));
			request.setAttribute("navActionLast", request.getSession().getAttribute("linkAdvertisementsReportByAdvLast"));
			request.setAttribute("searchAction", request.getSession().getAttribute("linkAdvertisementsReportByAdvSearch"));
			request.setAttribute("list", listAdvertisements);
			request.setAttribute("listCopy", listCopy);
		} finally {
			connection.closeTransaction();
//			utilities = null;
			valueSearch = null;
			param = null;
			listAdvertisements = null;
			listCopy = null;
			user = null;
			nav = null;
			type_report = null; 
		}
		return REPORT_BY_ADV;
	}
	
	@SuppressWarnings("unchecked")
	public String reportByDate(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users user = null;
		Map<String, Object> param = null;
		String valueSearch = null;
		List<HistoryCountClick> listHistoryCountClick = null;
		List<HistoryGrossClick> listHistoryGrossClick = null;
		List<HistoryPurchases> listHistoryPurchases = null;
		List<HistoryMostAccessed> listHistoryMostAccessed = null;
		List<HistoryRecentlyAdded> listHistoryRecentlyAdded = null;
		int current = 0;		
		int maximum = 10;
		int totalResults = 0;			
		int totalPages = 0;
		String nav = null;
		Calendar calendarInitial = Calendar.getInstance();
		Calendar calendarFinal = Calendar.getInstance();
		int ano = calendarInitial.get(Calendar.YEAR);
		int mes = calendarInitial.get(Calendar.MONTH);
		String report_type = null;
		if (request.getParameter("selYear") != null) {
			ano = Integer.parseInt(request.getParameter("selYear"));
			request.getSession().setAttribute("year_report", ano);
		}
		else {
			if (request.getSession().getAttribute("year_report") == null)
				request.getSession().setAttribute("year_report", ano);
			else
				ano = (int) request.getSession().getAttribute("year_report");
		}
			
		if (request.getParameter("selMonth") != null) {
			mes = Integer.parseInt(request.getParameter("selMonth"));
			request.getSession().setAttribute("month_report", mes);
		}
		else {
			if (request.getSession().getAttribute("month_report") == null)
				request.getSession().setAttribute("month_report", mes);
			else
				mes = (int) request.getSession().getAttribute("month_report");
		}
		
		calendarInitial.set(Calendar.YEAR, ano);
		calendarInitial.set(Calendar.MONTH, mes);
		calendarInitial.set(Calendar.DAY_OF_MONTH, 1);
		calendarInitial.set(Calendar.HOUR_OF_DAY, 0);
		calendarInitial.set(Calendar.MINUTE, 0);
		calendarInitial.set(Calendar.SECOND, 0);
		Date initialDate = calendarInitial.getTime();
		
		calendarFinal.set(Calendar.YEAR, ano);
		calendarFinal.set(Calendar.MONTH, mes);
		calendarFinal.set(Calendar.DAY_OF_MONTH, calendarFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
		calendarFinal.set(Calendar.MINUTE, 59);
		calendarFinal.set(Calendar.SECOND, 59);
		Date finalDate = calendarFinal.getTime();
		
		try {
			connection.beginTransaction();
			user = (Users) request.getSession().getAttribute("currentUser");
			param = new HashMap<String, Object>();
			valueSearch = request.getParameter("valueSearch");
			if (valueSearch == null)
				valueSearch = "";
			else
				valueSearch = valueSearch.replaceAll("%", "");
			if (request.getParameter("nav") == null) {
				param.put("user_id", user.getId());
				param.put("initialDate", initialDate);
				param.put("currentDate",finalDate);
				report_type = request.getParameter("selReportType");
				if (report_type == null)
					report_type = (String) request.getSession().getAttribute("report_type");
				if (report_type != null) {
					if (report_type.equals("CC")) {
						request.getSession().setAttribute("report_type", "CC");
						totalResults = connection.count(HistoryCountClick.QUERY_COUNT_HISTORY_CLICK_BY_USER_AND_DATE, param);
					}
					else if (report_type.equals("GC")) {
						request.getSession().setAttribute("report_type", "GC");
						totalResults = connection.count(HistoryGrossClick.QUERY_COUNT_HISTORY_GROSS_CLICK_BY_USER_AND_DATE, param);
					}
					else if (report_type.equals("PU")) {
						request.getSession().setAttribute("report_type", "PU");
						totalResults = connection.count(HistoryPurchases.QUERY_COUNT_HISTORY_PURCHASES_BY_USER_AND_DATE, param);
					}
					else if (report_type.equals("MA")) {
						request.getSession().setAttribute("report_type", "MA");
						totalResults = connection.count(HistoryMostAccessed.QUERY_COUNT_HISTORY_MOST_ACCESSED_BY_USER_AND_DATE, param);
					}
					else if (report_type.equals("RA")) {
						request.getSession().setAttribute("report_type", "RA");
						totalResults = connection.count(HistoryRecentlyAdded.QUERY_COUNT_HISTORY_RECENTLY_ADDED_BY_USER_AND_DATE, param);
					}
				}
				else {
					request.getSession().setAttribute("report_type", "CC");
					totalResults = connection.count(HistoryCountClick.QUERY_COUNT_HISTORY_CLICK_BY_USER_AND_DATE, param);
				}
				param.clear();
				if (totalResults > 0) {
					totalPages = totalResults / maximum;
				}
				else {
					totalResults = 0;			
					totalPages = 0;
				}
				request.getSession().setAttribute("totalResults", totalResults);
				request.getSession().setAttribute("totalPages", totalPages);
			}
			else {
				nav = request.getParameter("nav");
				current = (int) request.getSession().getAttribute("current");
				maximum = (int) request.getSession().getAttribute("maximum");
				totalResults = (int) request.getSession().getAttribute("totalResults");
				totalPages = (int) request.getSession().getAttribute("totalPages");
				if (nav.equals("first"))
					current = 0;
				else if (nav.equals("previous"))
					current -= maximum;
				else if (nav.equals("next"))
					current += maximum;
				else if (nav.equals("last")) {
					if((totalResults % maximum) == 0)
						current = (totalPages - 1) * maximum;
					else
						current = totalPages * maximum;
				}
				if (current > totalResults)
					current =0;
			}
			param.put("user_id", user.getId());
			param.put("initialDate", initialDate);
			param.put("currentDate", finalDate);
			report_type = request.getParameter("selReportType");
			if (report_type == null)
				report_type = (String) request.getSession().getAttribute("report_type");
			if (report_type != null) {
				if (report_type.equals("CC")) {
					request.getSession().setAttribute("report_type", "CC");
					listHistoryCountClick = (List<HistoryCountClick>) connection.list(HistoryCountClick.QUERY_HISTORY_COUNT_CLICK_BY_USER_AND_DATE, param, current, maximum);
					request.setAttribute("list", listHistoryCountClick);
				}
				else if (report_type.equals("GC")) {
					request.getSession().setAttribute("report_type", "GC");
					listHistoryGrossClick = (List<HistoryGrossClick>) connection.list(HistoryGrossClick.QUERY_HISTORY_GROSS_CLICK_BY_USER_AND_DATE, param, current, maximum);
					request.setAttribute("list", listHistoryGrossClick);
				}
				else if (report_type.equals("PU")) {
					request.getSession().setAttribute("report_type", "PU");
					listHistoryPurchases = (List<HistoryPurchases>) connection.list(HistoryPurchases.QUERY_HISTORY_PURCHASES_BY_USER_AND_DATE, param, current, maximum);
					request.setAttribute("list", listHistoryPurchases);
				}
				else if (report_type.equals("MA")) {
					request.getSession().setAttribute("report_type", "MA");
					listHistoryMostAccessed = (List<HistoryMostAccessed>) connection.list(HistoryMostAccessed.QUERY_HISTORY_MOST_ACCESSED_BY_USER_AND_DATE, param, current, maximum);
					request.setAttribute("list", listHistoryMostAccessed);
				}
				else if (report_type.equals("RA")) {
					request.getSession().setAttribute("report_type", "RA");
					listHistoryRecentlyAdded = (List<HistoryRecentlyAdded>) connection.list(HistoryRecentlyAdded.QUERY_HISTORY_RECENTLY_ADDED_BY_USER_AND_DATE, param, current, maximum);
					request.setAttribute("list", listHistoryRecentlyAdded);
				}
			}
			else {
				request.getSession().setAttribute("report_type", "CC");
				listHistoryCountClick = (List<HistoryCountClick>) connection.list(HistoryCountClick.QUERY_HISTORY_COUNT_CLICK_BY_USER_AND_DATE, param, current, maximum);
				request.setAttribute("list", listHistoryCountClick);
			}
			param.clear();
			request.setAttribute("valueSearch", valueSearch);
			request.getSession().setAttribute("current", current);
			request.getSession().setAttribute("maximum", maximum);
			request.setAttribute("navActionFirst", request.getSession().getAttribute("linkAdvertisementsReportByDateFirst"));
			request.setAttribute("navActionPrevious", request.getSession().getAttribute("linkAdvertisementsReportByDatePrevious"));
			request.setAttribute("navActionNext", request.getSession().getAttribute("linkAdvertisementsReportByDateNext"));
			request.setAttribute("navActionLast", request.getSession().getAttribute("linkAdvertisementsReportByDateLast"));
			request.setAttribute("searchAction", request.getSession().getAttribute("linkAdvertisementsReportByDateSearch"));
		} finally {
			connection.closeTransaction();
			utilities = null;
			valueSearch = null;
			param = null;
			listHistoryCountClick = null;
			listHistoryGrossClick = null;
			listHistoryPurchases = null;
			listHistoryMostAccessed = null;
			listHistoryRecentlyAdded = null;
			user = null;
			nav = null;
		}
		return REPORT_BY_DATE;
	}
	
	public void uploadImage(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") != null) {
			boolean isPrint = false;
			Utilities utilities = new Utilities();
			utilities.printNameValue(request.getParameterMap(), isPrint);
			Advertisements advertisement = null;
			ArrayList<String> messages = null;
			String pathImages = null;
			String nameImage = null;
			String ext = null;
			String[] tokens = null;
			int pos = 0;
			BufferedImage image = null;
			BufferedImage finalImage = null;
			Date dataAtual = null;
			File imagePath = null;
			File imageFile = null;
			String pathImage = "";		
			AdvertisementImages advertisementImages = null;
			int posX = 0;
			int posY = 0;
			int count = 0;
			try {
				connection.beginTransaction();
				advertisement = (Advertisements) connection.find(Advertisements.class, Integer.parseInt(request.getParameter("advertisement.id")));
				messages = new ArrayList<String>();
				if (advertisement.getQtda_images() < advertisement.getMax_images()) {
					advertisement.setQtda_images(advertisement.getQtda_images() + 1);
					pathImages = (String) request.getSession().getAttribute("pathImages");
					nameImage = String.valueOf(advertisement.getQtda_images());
					for (Part part : request.getParts()) {
						ext = "";
						tokens = part.getSubmittedFileName().split("\\.");
						pos = tokens.length;
						if (pos > 1) {
							ext = tokens[pos-1].toLowerCase();
							if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
								pathImage = pathImages + "advertisements/" + advertisement.getId() + "/";
								imagePath = new File(pathImage);
								if (!imagePath.exists())
									imagePath.mkdirs();
								imagePath.setWritable(true, true);
								imageFile = new File(pathImage + nameImage);
								count = 0;
								do {
									count++;
									nameImage = (imagePath.list().length + count) + "." + ext;
									imageFile = new File(pathImage + nameImage);
								} while (imageFile.exists());
								image = ImageIO.read(part.getInputStream());
								// PAISAGEM (640x480)
//								if (image.getWidth() > image.getHeight()) {
								if (image.getWidth() > image.getHeight() && image.getWidth() > 640) {
									image = utilities.resizeImage(image, 640, 480);
									posX = 0;
									posY = 0;
								} else if (image.getWidth() > image.getHeight() && image.getWidth() < 640) {
									posX = (640 - image.getWidth()) / 2;
									posY = (480 - image.getHeight()) / 2;
								}
								// RETRATO (360x480)
//								else if (image.getHeight() > image.getWidth()) {
								else if (image.getHeight() > image.getWidth() && image.getHeight() > 480) {
									image = utilities.resizeImage(image, 360, 480);
									posX = 140;
									posY = 0;
								} else if (image.getHeight() > image.getWidth() && image.getHeight() < 480) {
									posX = (640 - image.getWidth()) / 2;
									posY = (480 - image.getHeight()) / 2;
								}
								// QUADRADA (480x480)
//								else if (image.getWidth() == image.getHeight()) {
								else if (image.getWidth() == image.getHeight() && image.getWidth() > 480) {
									image = utilities.resizeImage(image, 480, 480);
									posX = 80;
									posY = 0;
								} else if (image.getWidth() == image.getHeight() && image.getWidth() < 480) {
									posX = (640 - image.getWidth()) / 2;
									posY = (480 - image.getHeight()) / 2;
								}
								finalImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
								Graphics graphics = finalImage.createGraphics();
								graphics.setColor(Color.WHITE);
								graphics.fillRect(0, 0, 640, 480);
								graphics.drawImage(image, posX, posY, null);
						        graphics.dispose();		
								imageFile.setWritable(true, true);
								ImageIO.write(finalImage, ext, imageFile);
								dataAtual = new Date();
								advertisementImages = new AdvertisementImages();
								advertisementImages.setAdvertisement_id(advertisement);
								advertisementImages.setImage_file_name("advertisements/" + advertisement.getId() + "/" + nameImage);
								advertisementImages.setImage_file_type(ext);
								advertisementImages.setLink("");
								advertisementImages.setStatus(1);
								advertisementImages.setCreated_at(dataAtual);
								advertisementImages.setUpdated_at(dataAtual);
								connection.save(advertisementImages);
								connection.update(advertisement);
								connection.commit();
							}
						}
						else
							messages.add("image.typeImage");
					}
				}
				else
					messages.add("image.maximumAmount");
				request.setAttribute("messages", messages);
				request.setAttribute("advertisement", advertisement);
				request.setAttribute("current", request.getParameter("current"));
				request.setAttribute("maximum", request.getParameter("maximum"));
				request.setAttribute("valueSearch", request.getParameter("valueSearch"));
				connection.closeTransaction();
				try {
					request.getRequestDispatcher("./Controller?form=actions.AdvertisementsActions&action=form&panel=true").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			} finally {
				connection.closeTransaction();
				utilities = null;
				advertisement = null;
				messages = null;
				pathImages = null;
				nameImage = null;
				ext = null;
				tokens = null;
				image = null;
				finalImage = null;
				dataAtual = null;
				imagePath = null;
				imageFile = null;
				pathImage = null;
				advertisementImages = null;
			}
		}
	}
	
	public void viewImage(HttpServletRequest request, HttpServletResponse response) {
		Utilities utilities = null;
		String image = null;
		String pathImages = null;
		String pathFullImages = null;
		String serverImages = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		URL urlImage = null;
		HttpURLConnection httpCode = null;
		try {
			utilities = new Utilities();
			image = request.getParameter("image");
			pathImages = (String) request.getSession().getAttribute("pathImages");
			pathFullImages = pathImages + image;
			if (new File(pathFullImages).exists() && pathFullImages.compareTo(pathImages) != 0 && image.compareTo("giroDefaultImage") != 0) {				
//				System.out.println("FULL: " + pathFullImages + " - PATH: " + pathImages + " - IMAGE: " + image);
				outputStream = response.getOutputStream();
				outputStream.write(utilities.toBytes(new FileInputStream(pathFullImages)));
				outputStream.flush();
				outputStream.close();
			} else {
				serverImages = (String) request.getSession().getAttribute("giroUrlServerImages");
				pathFullImages = serverImages + "images/defaultImage.png";
//				System.out.println("FULL: " + pathFullImages + " - PATH: " + serverImages + " - IMAGE: " + image);
				urlImage = new URL(pathFullImages);
				httpCode = (HttpURLConnection) urlImage.openConnection();
				if (httpCode.getResponseCode() == 200) {
					inputStream = urlImage.openStream();
					outputStream = response.getOutputStream();
					outputStream.write(utilities.toBytes(inputStream));
					outputStream.flush();
					outputStream.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			utilities = null;
			image = null;
			pathImages = null;
			serverImages = null;
			inputStream = null;
			outputStream = null;
			urlImage = null;
			httpCode = null;
		}
	}
	
	public void deleteImage(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") != null) {
			Advertisements advertisement = null;
			String pathImages = null;
			AdvertisementImages advertisementImages = null;
			File file = null;
			try {
				connection.beginTransaction();
				advertisement = (Advertisements) connection.find(Advertisements.class, Integer.parseInt(request.getParameter("advertisement.id")));
				if (request.getParameter("advertisement_image_id") != null) {
					pathImages = (String) request.getSession().getAttribute("pathImages");
					advertisementImages = (AdvertisementImages) connection.find(AdvertisementImages.class, Integer.parseInt(request.getParameter("advertisement_image_id")));
					file = new File(pathImages + advertisementImages.getImage_file_name());
					if (file.exists()) {
						file.setWritable(true, true);
						file.delete();
						advertisement.setQtda_images(advertisement.getQtda_images() - 1);
						connection.delete(advertisementImages);
						connection.update(advertisement);
						connection.commit();
					}
				}
				request.setAttribute("advertisement", advertisement);
				request.setAttribute("current", request.getParameter("current"));
				request.setAttribute("maximum", request.getParameter("maximum"));
				request.setAttribute("valueSearch", request.getParameter("valueSearch"));
				connection.closeTransaction();
				try {
					request.getRequestDispatcher("./Controller?form=actions.AdvertisementsActions&action=form&panel=true").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} finally {
				connection.closeTransaction();
				advertisement = null;
				pathImages = null;
				advertisementImages = null;
				file = null;
			}
		}
	}
	
	public List<AdvertisementStores> getAdvertisementsHome(String value_search, String lat, String lng, int radius, HttpServletRequest request) {
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		Admin admin = null;
		List<AdvertisementStores> listAdvertisementsHome = new ArrayList<AdvertisementStores>();
		List<Object[]> listStoresHome = null;
		List<Integer> listIdsAdvertisements = null;
		Map<String, Object> param = new HashMap<String, Object>();
		String[] tokensAux;
		List<String> tokensValidos = new ArrayList<String>();
		String value_search_aux = "";
		long initialTime = 0;
		int typeSearch = 0;
		int qtdaStores = 0;
		int qtdaAdv = 0;
		int aditionalRadius = 0;
		int countRepeat = 0;
		int store_id = 0;
		int countTokens = 0;
//		boolean isStopword = false;
		try {
			admin = (Admin) connection.find(Admin.class, 1);
			if (lat == null || lat.equals("") || lat.equals("undefined")) {
				lat = "-29.901271";
				lng = "-53.039038";
			}
			if (value_search.equals("%%"))
				typeSearch = 0;
			else {
				typeSearch = 1;
				tokensAux = value_search.split(" ");
				if (tokensAux.length > 1) {
					for (int i=0; i<tokensAux.length; i++) {
						tokensAux[i] = tokensAux[i].replaceAll("%", "");
						param.put("stopword", tokensAux[i]);
						if (connection.find(Stopwords.QUERY_STOPWORDS, param) == null)
							tokensValidos.add(tokensAux[i]);
						param.clear();
					}
					if (tokensValidos.size() > 1) {
						for (String aux : tokensValidos) {
							countTokens++;
							value_search_aux += aux;
							if (countTokens < tokensValidos.size())
								value_search_aux += "|";
						}
					}
					else
						value_search_aux = tokensValidos.get(0);
				}
				else
					value_search_aux = value_search.replaceAll("%", "");
				listIdsAdvertisements =	getListAdvertisementsIds(value_search_aux.toLowerCase(), admin.getResearched_view());
			}
			
			do {
				initialTime = System.currentTimeMillis();
				listStoresHome = connection.listByCoords(lat, lng, admin.getRadius() + aditionalRadius);
				for (Object[] object: listStoresHome) {
					store_id = (int) object[0];
					if (typeSearch == 0) {
						for (AdvertisementStores advStores : getListAdvStoresHome(store_id, admin.getHome_view()))
							listAdvertisementsHome.add(advStores);
					}
					else {
						if (listIdsAdvertisements.size() > 0) {
							for (int advertisement_id : listIdsAdvertisements) {
								param.put("advertisement_id", advertisement_id);
								param.put("store_id", store_id);
								AdvertisementStores advertisementStores = (AdvertisementStores) connection.find(AdvertisementStores.QUERY_BY_ADVERTISEMENTSTORES, param);
								param.clear();
								if (advertisementStores != null)
									listAdvertisementsHome.add(advertisementStores);
							}
						}
					}
				}
				qtdaStores = listStoresHome.size();
				qtdaAdv = listAdvertisementsHome.size();
				if (typeSearch == 0)
					utilities.printDataHora(" - Time: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s. | qtdaStores: " + qtdaStores + " | qtdaAdv: " + qtdaAdv + " | radius: " + admin.getRadius() + "km", isPrint);
				else
					utilities.printDataHora(" - Time: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s. | termo: " + value_search_aux + " | qtdaStores: " + qtdaStores + " | qtdaAdv: " + qtdaAdv + " | countRepeat: " + countRepeat + " | radius: " + admin.getRadius() + "km | aditionalRadius: " + aditionalRadius + "km", isPrint);
				if (qtdaAdv == 0)
					aditionalRadius += admin.getAdd_radius();
				countRepeat++;
				request.getSession().setAttribute("homeAditionalRadius", aditionalRadius);
			} while(qtdaAdv == 0 && aditionalRadius <= admin.getLimit_radius());
			request.getSession().setAttribute("listAdvertisementsHome", listAdvertisementsHome);
			request.getSession().setAttribute("homeTotalAdv", qtdaAdv);
			request.getSession().setAttribute("homeTotalStores", qtdaStores);
			request.getSession().setAttribute("homeAditionalRadius", aditionalRadius);
		} finally {
			utilities = null;
			admin = null;
			listStoresHome = null;
			listIdsAdvertisements = null;
			param = null;
			tokensAux = null;
			value_search_aux = null;
		}
//		utilities.printDataHora(" - TEMPO DE BUSCA: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.\n", isPrint);
		return listAdvertisementsHome;
	}
	
	public void getAdditionalRadius(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(new Gson().toJson((int) request.getSession().getAttribute("homeAditionalRadius")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<AdvertisementStores> getListAdvStoresHome(int store_id, int limitHome) {
		return connection.getSession().doReturningWork( 
			new ReturningWork<List<AdvertisementStores>>() {
			@Override
			public List<AdvertisementStores> execute(java.sql.Connection con) throws SQLException {
				List<AdvertisementStores> listAux = new ArrayList<AdvertisementStores>();
				AdvertisementStores advSto = null;
				PreparedStatement pst = null;
				ResultSet rst = null; 
				String sql = 
						" SELECT advsto.* FROM "
						+ " 	advertisement_stores AS advsto "
						+ " INNER JOIN "
						+ " 	advertisements AS adv "
						+ " 		ON advsto.advertisement_id = adv.id "
						+ " INNER JOIN "
						+ " 	stores AS sto "
						+ " 		ON advsto.store_id = sto.id "
						+ " WHERE "
						+ " 	advsto.store_id = ? AND "
						+ " 	advsto.status = 1 AND "
						+ " 	adv.status = 2 AND "
						+ " 	sto.status = 1 "
//						+ " ORDER BY advsto.updated_at DESC "
						+ " LIMIT ? ";
				try {
					pst = con.prepareStatement(sql);
					pst.setInt(1, store_id);
					pst.setInt(2, limitHome);
					rst = pst.executeQuery();
					while(rst.next()) {
						advSto = new AdvertisementStores();
						advSto.setAdvertisement_id((Advertisements) connection.find(Advertisements.class, rst.getInt(1)));
						advSto.setStore_id((Stores) connection.find(Stores.class, rst.getInt(2)));
						advSto.setUrl(rst.getString(3));
						advSto.setGross_click(rst.getInt(4));
						advSto.setCount_click(rst.getInt(5));
						advSto.setStatus(rst.getInt(6));
						advSto.setCreated_at(rst.getTimestamp(7));
						advSto.setUpdated_at(rst.getTimestamp(8));
						listAux.add(advSto);
					}
				} finally {
					pst.close();
					rst.close();
					sql = null;
				}
				return listAux;
			}
		});
	}
	
	private List<Integer> getListAdvertisementsIds(String value_search, int limitSearch) {
		return connection.getSession().doReturningWork( 
			new ReturningWork<List<Integer>>() {
			@Override
			public List<Integer> execute(java.sql.Connection con) throws SQLException {
				List<Integer> listAux = new ArrayList<Integer>();
				PreparedStatement pst = null;
				ResultSet rst = null; 
				String sql = 
						" SELECT adv.id FROM advertisements AS adv "
						+ " INNER JOIN categories AS cat ON adv.category_id = cat.id "
						+ " WHERE "
						+ " (adv.title REGEXP ? OR adv.brand REGEXP ? OR cat.keywords REGEXP ?) AND "
						+ " adv.status = 2 "
						+ " LIMIT ? ";
				try {
					pst = con.prepareStatement(sql);
					pst.setString(1, value_search);
					pst.setString(2, value_search);
					pst.setString(3, value_search);
					pst.setInt(4, limitSearch);
					rst = pst.executeQuery();
					while(rst.next())
						listAux.add(rst.getInt(1));
				} finally {
					pst.close();
					rst.close();
					sql = null;
				}
				return listAux;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public void generateURLs() {
		Utilities utilities = new Utilities(); 
		List<Advertisements> listAdvertisements = null;
		long initialTime = System.currentTimeMillis();
		boolean isPrint = true;
		int limit = 1000;
		int current = 0;
		int total = 0;
		int count = 0;
		int cutOff = 20;
		try {
			connection.beginTransaction();
			utilities.printDataHora(" - UPDATE URLs => INICIO ...", isPrint);
			total = connection.count(Advertisements.QUERY_COUNT_TOTAL);
			if (total > limit) {					
				total /= limit;
				for (int i=0; i <= total; i++) {
					listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_ALL, current, limit);
					for (Advertisements ads : listAdvertisements) {
						count++;
						ads.setUrl("advertisement/" + ads.getId() + "/" + utilities.configUrl(ads.getTitle()));
						connection.update(ads);
						if (count % cutOff  == 0) {
							connection.getSession().flush();
							connection.getSession().clear();
						}
					}
					current += limit;
				}
			}
			connection.commit();
			utilities.printDataHora(" - UPDATE URLs => ... FIM | QTDA: " + count + " | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} finally {
			connection.closeTransaction();
			utilities = null;
			listAdvertisements = null;
		}
	}
	
}
