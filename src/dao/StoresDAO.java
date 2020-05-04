package dao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import connection.Connection;
import entity.AdvertisementImages;
import entity.AdvertisementStores;
import entity.Advertisements;
import entity.Cities;
import entity.States;
import entity.StoreImages;
import entity.Stores;
import entity.Users;
import interfaces.IDao;
import mail.MailAlert;
import spinwork.Manager;
import utils.Utilities;

public final class StoresDAO implements IDao {
	
//	private static final String SUCCESS = "stores/success.jsp";
	private static final String FORM = "stores/form.jsp";
	private static final String LIST = "stores/list.jsp";
	private static final String INCOMPLETE = "users/incomplete.jsp";
	private Connection connection = null;

	public StoresDAO(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public String form(HttpServletRequest request, HttpServletResponse response) {
		Stores store = null;
		Map<String, Object> param = null;
		try {
			connection.beginTransaction();
			if (check(request)) {
				store = (Stores) connection.find(Stores.class, Integer.parseInt(request.getParameter("store.id")));
				request.setAttribute("store", store);
				request.setAttribute("stateId", store.getCity_id().getState_id().getId());
				request.setAttribute("cityId", store.getCity_id().getId());
				param = new HashMap<String, Object>();
				param.put("state_id", store.getCity_id().getState_id().getId());
				request.setAttribute("listCities", connection.list(Cities.QUERY_FILTERED_BY_STATE, param));
				param.clear();
				param.put("store_id", store.getId());
				request.setAttribute("listStoreImages", connection.list(StoreImages.QUERY_BY_STORE, param));
				param.clear();
			}
			request.setAttribute("current", request.getParameter("current"));
			request.setAttribute("maximum", request.getParameter("maximum"));
			request.setAttribute("valueSearch", request.getParameter("valueSearch"));
			request.setAttribute("listStates", connection.list(States.QUERY_LIST_ALL_WITH_ORDINATION));
		} finally {
			connection.closeTransaction();
			store = null;
			param = null;
		}
		return FORM;
	}

	@Override
	public String save(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		Boolean isNew = false;
		Stores store = null;
		Users user = null;
		int stateId = 0;
		Map<String, Object> param = null;
		Date dataAtual = null;
		ArrayList<String> messages = null;
		MailAlert mailAlert = null;
		Thread threadMailAlert = null;
		
		try {
			connection.beginTransaction();
			if (check(request))
				store = (Stores) connection.find(Stores.class, Integer.parseInt(request.getParameter("store.id")));
			else {
				store = new Stores();
				isNew = true;
			}
			store = (Stores) Manager.setObject(store, request, connection);
			user = (Users) request.getSession().getAttribute("currentUser");
			stateId = Integer.parseInt(request.getParameter("ddStoreState"));
			param = new HashMap<String, Object>();
			param.put("state_id", stateId);
			request.setAttribute("listCities", connection.list(Cities.QUERY_FILTERED_BY_STATE, param));
			param.clear();
			request.setAttribute("stateId", stateId);
			request.setAttribute("cityId", Integer.parseInt(request.getParameter("store.city_id")));
			dataAtual = new Date();
			if (isNew) {
				store.setCnpj("");
				store.setState_registration("");
				store.setCompany_name("");
				store.setCompany_fantasy_name("");
				store.setCompany_slogan("");
				store.setUser_id(user);
				store.setQtda_images(0);
				store.setMax_images(3);
				store.setImage_file_name("");
				store.setImage_file_type("");
				store.setStatus(1);
				store.setCreated_at(dataAtual);
			}
			store.setUpdated_at(dataAtual);
			if ((boolean) request.getAttribute("isValid")) {
				if (isNew) {
					connection.save(store);
					user.setCount_stores(user.getCount_stores()+1);
					connection.update(user);
					request.getSession().setAttribute("currentUser", user);
					messages = new ArrayList<String>();
					messages.add("store.success");
					request.setAttribute("messages", messages);
					request.setAttribute("typeMessage", "list-group-item-success");
					mailAlert = new MailAlert(
							"Giroferta - Nova loja", 
							"\n\nID: " + store.getId() + 
							"\nLoja: " + store.getName() + 
							"\nFilial: " + store.getFilial() + 
							"\nLatitude: " + store.getLatitude() + 
							"\nLongitude: " + store.getLongitude() + 
							"\nE-mail: " + store.getContact_email() + 
							"\n\n",
							new String[] {"golive@giroferta.com"},
							(String) request.getSession().getAttribute("giroUrl")
						);
					threadMailAlert = new Thread(mailAlert);
					threadMailAlert.start();
				}
				else {
					connection.update(store);
					messages = new ArrayList<String>();
					messages.add("store.updated");
					request.setAttribute("messages", messages);
					request.setAttribute("typeMessage", "list-group-item-success");
				}
				connection.commit();
			}
			else
				request.setAttribute("typeMessage", "list-group-item-danger");
		} finally {
			connection.closeTransaction();
			isNew = false;
			store = null;
			user = null;
			stateId = 0;
			param = null;
			dataAtual = null;
			messages = null;
			mailAlert = null;
			threadMailAlert = null;
		}
		return form(request, response);	
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		Stores store = null;
		Users user = null;
		
		try {
			connection.beginTransaction();
			if (check(request)) {
				store = (Stores) connection.find(Stores.class, Integer.parseInt(request.getParameter("store.id")));
				store.setStatus(0);
				connection.update(store);
				user = (Users) request.getSession().getAttribute("currentUser");
				if (user.getCount_stores() > 0) {
					user.setCount_stores(user.getCount_stores()-1);
					connection.update(user);
				}
				connection.commit();
				request.getSession().setAttribute("currentUser", user);
			}
		} finally {
			connection.closeTransaction();
			store = null;
			user = null;
		}
		return list(request, response);
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getSession().getAttribute("currentUser") == null)
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
		List<?> listStores = null;
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
				param.put("name", "%" + valueSearch + "%");			
				param.put("filial", "%" + valueSearch + "%");			
				totalResults = connection.count(Stores.QUERY_COUNT_BY_NAME_OR_FILIAL, param);
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
			param.put("name", "%" + valueSearch + "%");			
			param.put("filial", "%" + valueSearch + "%");			
			listStores = connection.list(Stores.QUERY_BY_NAME_OR_FILIAL, param, current, maximum);
			param.clear();
			request.setAttribute("valueSearch", valueSearch);
			request.getSession().setAttribute("current", current);
			request.getSession().setAttribute("maximum", maximum);
			request.setAttribute("navActionFirst", request.getSession().getAttribute("linkStoresListFirst"));
			request.setAttribute("navActionPrevious", request.getSession().getAttribute("linkStoresListPrevious"));
			request.setAttribute("navActionNext", request.getSession().getAttribute("linkStoresListNext"));
			request.setAttribute("navActionLast", request.getSession().getAttribute("linkStoresListLast"));
			request.setAttribute("searchAction", request.getSession().getAttribute("linkStoresListSearch"));
			request.setAttribute("list", listStores);
		} finally {
			connection.closeTransaction();
//			utilities = null;
			valueSearch = null;
			param = null;
			listStores = null;
			user = null;
			nav = null;
		}
		return LIST;
	}

	@Override
	public boolean check(HttpServletRequest request) {
		boolean isCheck;
		if (request.getParameter("store.id") != null && !request.getParameter("store.id").equals("") && !request.getParameter("store.id").equals("0"))
			isCheck = true;
		else 
			isCheck = false;
		return isCheck;
	}

	@SuppressWarnings("unchecked")
	public void cities(HttpServletRequest request, HttpServletResponse response){
		StringBuffer retorno = new StringBuffer("");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		int ddStoreState = 0;
		Map<String, Object> param = null;
		List<Cities> listCities = null;
		try {
			connection.beginTransaction();
			try {
				ddStoreState = Integer.parseInt(request.getParameter("ddStoreState"));
				param = new HashMap<String, Object>();
				param.put("state_id", ddStoreState);
				listCities = (List<Cities>) connection.list(Cities.QUERY_FILTERED_BY_STATE, param);
				param.clear();
				out = response.getWriter();
				out.println("<select class=\"form-control\" id=\"ddStoreCity\" name=\"store.city_id\">");
				retorno.append("<option value=\"0\">Cidade</option>");
				for (Cities cities : listCities) {
					retorno.append("<option value=\"" + cities.getId()  + "\">" + cities.getName() + "</option>");
				}
		        out.println(retorno);
		        out.println("</select>");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			connection.closeTransaction();
			retorno = null;
			out = null;
			ddStoreState = 0;
			param = null;
			listCities = null;
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void json(HttpServletRequest request, HttpServletResponse response) {
//		Util utilities = new Util();
//		utilities.printNameValue(request.getParameterMap(), true);
		JsonArrayBuilder  jsonArrayBuilderStore = Json.createArrayBuilder();
		JsonArrayBuilder  jsonArrayBuilderStoreImages = Json.createArrayBuilder();
		JsonArrayBuilder  jsonArrayBuilderAdvertisements = Json.createArrayBuilder();
		JsonArrayBuilder  jsonArrayBuilderAdvertisementImages = Json.createArrayBuilder();
		JsonObjectBuilder jsonObjectBuilderStore = Json.createObjectBuilder();
		JsonObjectBuilder jsonObjectBuilderStoreImages = Json.createObjectBuilder();
		JsonObjectBuilder jsonObjectBuilderAdvertisements = Json.createObjectBuilder();
		JsonObjectBuilder jsonObjectBuilderAdvertisementImages = Json.createObjectBuilder();
		JsonArray jsonArrayStore = null;		
		JsonArray jsonArrayStoreImages = null;
		JsonArray jsonArrayAdvertisements = null;
		JsonArray jsonArrayAdvertisementImages = null;
		Map<String, Object> param = null;
		List<StoreImages> listStoreImages = null;
		List<AdvertisementImages> listAdvertisementsImages = null;
		List<AdvertisementStores> listAdvertisementStores = null;
		List<Stores> listStores = null;
		String search = null;
		String auxDescription = null;
		try {
			connection.beginTransaction();
			if (request.getParameter("currentStore") != null) {
				param = new HashMap<String, Object>();
				
				param.put("url", request.getParameter("currentStore"));
				listStores = (List<Stores>) connection.list(Stores.QUERY_BY_URL, param);
				param.clear();
				
				int qtyStores = listStores.size();
				param.put("url", request.getParameter("currentStore"));
				int qtyAdvertisements = connection.count(Advertisements.QUERY_COUNT_BY_PUBLISHED_BY_URL, param);
				param.clear();
				int total = qtyStores * qtyAdvertisements;
				
//				int limite = total;
//				if (limite > 500) {
//					limite /= 500;
//					if (limite > 500) {
//						limite /= 500;
//						if (limite > 500) {
//							limite /= 500;
//						}
//					}
//				}
//				
//				while (limite > 500) {
//					limite /= 500;
//				}
				
				
				int limite = 5;
				
//				if (total > 500) {
//					int aux = 0;
//					if (qtyStores > 500)
//						limite = 1;
//					else {
//						aux = limite * qtyStores;
//						if (aux < 500) {
//							limite++;
//							aux = limite * qtyStores;
//							if (aux < 500) {
//								limite++;
//								aux = limite * qtyStores;
//								if (aux < 500) {
//									limite++;
//									aux = limite * qtyStores;
//									if (aux < 500) {
//										limite++;
//										aux = limite * qtyStores;
//										if (aux < 500) {
//											limite++;
//											aux = limite * qtyStores;
//										}
//									}
//								}
//							}
//						}
//					}
//				}
				
				
//				System.out.println("LOJAS: " + qtyStores);
//				System.out.println("ANUNCIOS: " + qtyAdvertisements);
//				System.out.println("TOTAL: " + total);
//				System.out.println("LIMITE: " + limite);
				
				search = request.getParameter("search");
				auxDescription = "";
				if (search == null)
					search = "";
				for (Stores store : listStores) {
					if (store.getStatus() == 1) {
						for (StoreImages storeImages : store.getListStoreImages()) {
							jsonObjectBuilderStoreImages
								.add("id", storeImages.getId())
								.add("image_file_name", storeImages.getImage_file_name());
							jsonArrayBuilderStoreImages.add(jsonObjectBuilderStoreImages.build());
						}
						jsonArrayStoreImages = jsonArrayBuilderStoreImages.build();
						jsonObjectBuilderStore
							.add("id", store.getId())
							.add("name", store.getName())
							.add("filial", store.getFilial())
							.add("city", store.getCity_id().getName())
							.add("state", store.getCity_id().getState_id().getInitials())
							.add("neighborhood", store.getNeighborhood())
							.add("address", store.getAddress())
							.add("number", store.getNumber())
							.add("complement", store.getComplement())
							.add("postal_code", store.getPostal_code())
							.add("latitude", store.getLatitude())
							.add("longitude", store.getLongitude())
//							.add("company_name", store.getCompany_name())
//							.add("company_fantasy_name", store.getCompany_fantasy_name())
							.add("contact_phone", store.getContact_phone())
							.add("contact_email", store.getContact_email())
							.add("logo", store.getUser_id().getImage_file_name())
//							.add("slogan", store.getCompany_slogan())
							.add("company_description", store.getCompany_description())
							.add("userUrl", store.getUser_id().getUrl())
							.add("userType_person", store.getUser_id().getType_person())
							.add("userLink", store.getUser_id().getLink())
							.add("userPartner", store.getUser_id().getPartner())
							.add("userName", store.getUser_id().getName())
							.add("userLastName", store.getUser_id().getLast_name())
							.add("userCompany_name", store.getUser_id().getCompany_name())
							.add("imagesStore", jsonArrayStoreImages);
						
//						if (!store.getListAdvertisementStores().isEmpty()) {
							param.put("store_id", store.getId());
							if (total > 500)
								listAdvertisementStores = (List<AdvertisementStores>) connection.list(AdvertisementStores.QUERY_BY_STORE, param, 0 , limite);
							else
								listAdvertisementStores = (List<AdvertisementStores>) connection.list(AdvertisementStores.QUERY_BY_STORE, param);
							param.clear();
							for (AdvertisementStores advertisementStores : listAdvertisementStores) {
								if (advertisementStores.getAdvertisement_id().getStatus() == 2) {
									auxDescription = advertisementStores.getAdvertisement_id().getDescription();
									if (auxDescription.length() > 40)
										auxDescription = auxDescription.substring(0, 39);
									jsonObjectBuilderAdvertisements
										.add("id", advertisementStores.getAdvertisement_id().getId())
										.add("title", advertisementStores.getAdvertisement_id().getTitle())
										.add("description", auxDescription)
										.add("price", advertisementStores.getAdvertisement_id().getPrice())
										.add("item_state", advertisementStores.getAdvertisement_id().getItem_state())
										.add("availability", advertisementStores.getAdvertisement_id().getAvailability())
										.add("brand", advertisementStores.getAdvertisement_id().getBrand())
										.add("link", advertisementStores.getAdvertisement_id().getLink());
									for (AdvertisementImages advertisementImages : advertisementStores.getAdvertisement_id().getListAdvertisementImages()) {
										jsonObjectBuilderAdvertisementImages
											.add("id", advertisementImages.getId())
											.add("image_file_name", advertisementImages.getImage_file_name());
										jsonArrayBuilderAdvertisementImages.add(jsonObjectBuilderAdvertisementImages.build());
									}
									jsonArrayAdvertisementImages = jsonArrayBuilderAdvertisementImages.build();
									jsonObjectBuilderAdvertisements.add("imagesAdvertisement", jsonArrayAdvertisementImages);
									jsonArrayBuilderAdvertisements.add(jsonObjectBuilderAdvertisements.build());
								}
							}
							jsonArrayAdvertisements = jsonArrayBuilderAdvertisements.build();								
							jsonObjectBuilderStore.add("advertisements", jsonArrayAdvertisements);
//						}
						jsonArrayBuilderStore.add(jsonObjectBuilderStore.build());
					}
				}
				jsonArrayStore = jsonArrayBuilderStore.build();			
//				System.out.println(jsonArrayStore.toString());
				try {
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonArrayStore.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			connection.closeTransaction();
//			utilities = null;
			jsonArrayBuilderStore = null;
			jsonArrayBuilderStoreImages = null;
			jsonArrayBuilderAdvertisements = null;
			jsonArrayBuilderAdvertisementImages = null;
			jsonObjectBuilderStore = null;
			jsonObjectBuilderStoreImages = null;
			jsonObjectBuilderAdvertisements = null;
			jsonObjectBuilderAdvertisementImages = null;
			jsonArrayStore = null;		
			jsonArrayStoreImages = null;
			jsonArrayAdvertisements = null;
			jsonArrayAdvertisementImages = null;
			param = null;
			listStoreImages = null;
			listAdvertisementsImages = null;
			listAdvertisementStores = null;
			listStores = null;
			search = null;
			auxDescription = null;
		}
	}
	
	public void uploadImageLogo(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") != null) {
//			Utilities utilities = new Utilities();
			Stores store = null;
			ArrayList<String> messages = null;
			String pathImages = null;
			String nameImage = null;
			String ext = null;
			Date dataAtual = null;
			boolean isValid = false;
			boolean isMultipart = false;
			FileItemFactory factory = null;
			ServletFileUpload upload = null;
			List<FileItem> items = null;
			String[] tokens = null;
			int i = 0;
			BufferedImage original = null;
//			BufferedImage alterada = null;
			File uploadPath = null;
			File uploadFile = null;
			try {
				connection.beginTransaction();
				store = (Stores) connection.find(Stores.class, Integer.parseInt(request.getParameter("store.id")));
				messages = new ArrayList<String>();
				pathImages = (String) request.getSession().getAttribute("pathImages");
				nameImage = String.valueOf(store.getId());
				ext = "";
				dataAtual = new Date();
				isValid = false;
				isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {
					try {
						factory = new DiskFileItemFactory();
						upload = new ServletFileUpload(factory);
						items = (List<FileItem>) upload.parseRequest(request);
						for(FileItem item : items) {
							if(!item.isFormField()) {
								tokens = item.getName().split("\\.");
								i = tokens.length;
								if (i > 1)
									ext = tokens[i-1].toLowerCase();
								if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
									if (item.getSize() > 0 && item.getSize() <= 2097152) { // 524288 = 512k
										original = ImageIO.read(item.getInputStream());
										if (original.getWidth() <= 1000) {
//											alterada = utilities.redimensionar(original, 640, 480);
											pathImages = pathImages + "logos/" + nameImage + "/";
											uploadPath = new File(pathImages);
											if (!uploadPath.exists())
												uploadPath.mkdirs();
											uploadPath.setWritable(true, true);
											nameImage = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(dataAtual) + "_" + nameImage + "." + ext;
											uploadFile = new File(pathImages + nameImage);
											uploadFile.setWritable(true, true);
											ImageIO.write(original, ext, uploadFile);	
											isValid = true;
										}
										else 
											messages.add("image.scaleImageLogo");
									}
									else
										messages.add("image.sizeImage");
								}
								else
									messages.add("image.typeImage");
							}
						}
					} catch (FileUploadException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (isValid) {
					store.setImage_file_name("stores/logos/" + store.getId() + "/" + nameImage);
					store.setImage_file_type(ext);
					store.setUpdated_at(dataAtual);
					connection.beginTransaction();
					connection.update(store);
					connection.commit();
				}
				request.setAttribute("messages", messages);
				request.setAttribute("store", store);
				request.setAttribute("current", request.getParameter("current"));
				request.setAttribute("maximum", request.getParameter("maximum"));
				request.setAttribute("valueSearch", request.getParameter("valueSearch"));
				connection.closeTransaction();
				try {
					request.getRequestDispatcher("./Controller?form=actions.StoresActions&action=form&panel=true").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} finally {
				connection.closeTransaction();
//				utilities = new Utilities();
				store = null;
				messages = null;
				pathImages = null;
				nameImage = null;
				ext = null;
				dataAtual = null;
				isValid = false;
				factory = null;
				upload = null;
				items = null;
				tokens = null;
				original = null;
//				alterada = null;
				uploadPath = null;
				uploadFile = null;
			}
		}
	}
		
	public void deleteImageLogo(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") != null) {
			Stores store = null;
			String pathImages = null;
			File file = null;
			try {
				connection.beginTransaction();
				store = (Stores) connection.find(Stores.class, Integer.parseInt(request.getParameter("store.id")));
				pathImages = (String) request.getSession().getAttribute("pathImages");
				file = new File(pathImages + store.getImage_file_name());
				if (file.exists()) {
					file.setWritable(true, true);
					file.delete();
					store.setImage_file_name("");
					store.setImage_file_type("");
					store.setUpdated_at(new Date());
					connection.update(store);
					connection.commit();
					file = null;
				}
				request.setAttribute("store", store);
				request.setAttribute("current", request.getParameter("current"));
				request.setAttribute("maximum", request.getParameter("maximum"));
				request.setAttribute("valueSearch", request.getParameter("valueSearch"));
				connection.closeTransaction();
				try {
					request.getRequestDispatcher("./Controller?form=actions.StoresActions&action=form&panel=true").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					connection.closeTransaction();
				}
			} finally {
				connection.closeTransaction();
				store = null;
				pathImages = null;
				file = null;
			}
		}
	}
	
	public void uploadImage(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") != null) {
			Utilities utilities = null;
			Stores store = null;
			ArrayList<String> messages = null;
			String pathImages = null;
			String nameImage = null;
			boolean isMultipart = false;
			FileItemFactory factory = null;
			ServletFileUpload upload = null;
			List<FileItem> items = null;
			String ext = null;
			String[] tokens = null;
			int i = 0;
			BufferedImage original = null;
			BufferedImage alterada = null;
			File uploadPath = null;
			File uploadFile = null;
			Date dataAtual = null;
			try {
				connection.beginTransaction();
				store = (Stores) connection.find(Stores.class, Integer.parseInt(request.getParameter("store.id")));
				messages = new ArrayList<String>();
				if (store.getQtda_images() < store.getMax_images()) {
					store.setQtda_images(store.getQtda_images() + 1);
					pathImages = (String) request.getSession().getAttribute("pathImages");
					nameImage = String.valueOf(store.getQtda_images());
					isMultipart = ServletFileUpload.isMultipartContent(request);
					if(isMultipart) {
						try {
							factory = new DiskFileItemFactory();
							upload = new ServletFileUpload(factory);
							items = (List<FileItem>) upload.parseRequest(request);
							for(FileItem item : items) {
								if(!item.isFormField()) {
									ext = "";
									tokens = item.getName().split("\\.");
									i = tokens.length;
									if (i > 1)
										ext = tokens[i-1].toLowerCase();
									if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
										if (item.getSize() > 0 && item.getSize() <= 2097152) { // 524288 = 512k
											utilities = new Utilities();
											original = ImageIO.read(item.getInputStream());
											if (original.getWidth() > original.getHeight() && original.getWidth() >= 640) {
												alterada = utilities.resizeImage(original, 640, 480);
												pathImages = pathImages + "stores/" + store.getId() + "/";
												uploadPath = new File(pathImages);
												if (!uploadPath.exists())
													uploadPath.mkdirs();
												uploadPath.setWritable(true, true);
												dataAtual = new Date();
												nameImage =  (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(dataAtual) + "_" + nameImage + "." + ext;
												uploadFile = new File(pathImages + nameImage);
												uploadFile.setWritable(true, true);
												ImageIO.write(alterada, ext, uploadFile);									
												StoreImages storeImages = new StoreImages();
												storeImages.setStore_id(store);
												storeImages.setImage_file_name("stores/" + store.getId() + "/" + nameImage);
												storeImages.setImage_file_type(ext);
												storeImages.setStatus(1);
												storeImages.setCreated_at(dataAtual);
												storeImages.setUpdated_at(dataAtual);
												connection.save(storeImages);
												connection.update(store);
												connection.commit();
											}
											else 
												messages.add("image.scaleImage");
										}
										else
											messages.add("image.sizeImage");
									}
									else
										messages.add("image.typeImage");
								}
							}
						} catch (FileUploadException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else
					messages.add("image.maximumAmount");
				request.setAttribute("messages", messages);
				request.setAttribute("store", store);
				request.setAttribute("current", request.getParameter("current"));
				request.setAttribute("maximum", request.getParameter("maximum"));
				request.setAttribute("valueSearch", request.getParameter("valueSearch"));
				connection.closeTransaction();
				try {
					request.getRequestDispatcher("./Controller?form=actions.StoresActions&action=form&panel=true").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} finally {
				connection.closeTransaction();
				utilities = null;
				store = null;
				messages = null;
				pathImages = null;
				nameImage = null;
				factory = null;
				upload = null;
				items = null;
				ext = null;
				tokens = null;
				original = null;
				alterada = null;
				uploadPath = null;
				uploadFile = null;
				dataAtual = null;
			}
		}
	}
	
	public void viewImage(HttpServletRequest request, HttpServletResponse response) {
		Utilities utilities = null;
		String image = null;
		String pathImages = null;
		String serverImages = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		URL urlImage = null;
		HttpURLConnection httpCode = null;
		try {
			utilities = new Utilities();
			image = request.getParameter("image");
			pathImages = (String) request.getSession().getAttribute("pathImages");
			image = pathImages + image;
			if (new File(image).exists()) {
				outputStream = response.getOutputStream();
				outputStream.write(utilities.toBytes(new FileInputStream(image)));
				outputStream.flush();
				outputStream.close();
			} else {
				serverImages = (String) request.getSession().getAttribute("giroUrlServerImages");
				image = serverImages + "images/defaultImage.png";
				urlImage = new URL(image);
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
			Stores store = null;
			String pathImages = null;
			StoreImages storeImages = null;
			File file = null;
			try {
				connection.beginTransaction();
				store = (Stores) connection.find(Stores.class, Integer.parseInt(request.getParameter("store.id")));
				if (request.getParameter("store_image_id") != null) {
					pathImages = (String) request.getSession().getAttribute("pathImages");
					storeImages = (StoreImages) connection.find(StoreImages.class, Integer.parseInt(request.getParameter("store_image_id")));
					file = new File(pathImages + storeImages.getImage_file_name());
					if (file.exists()) {
						file.setWritable(true, true);
						file.delete();
						store.setQtda_images(store.getQtda_images() - 1);
						connection.delete(storeImages);
						connection.update(store);
						connection.commit();
					}
				}
				request.setAttribute("store", store);
				request.setAttribute("current", request.getParameter("current"));
				request.setAttribute("maximum", request.getParameter("maximum"));
				request.setAttribute("valueSearch", request.getParameter("valueSearch"));
				connection.closeTransaction();
				try {
					request.getRequestDispatcher("./Controller?form=actions.StoresActions&action=form&panel=true").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} finally {
				connection.closeTransaction();
				store = null;
				pathImages = null;
				storeImages = null;
				file = null;
			}
		}
	}
	
}
