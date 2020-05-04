package filters;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;

import config.Config;
import connection.Connection;
import entity.Admin;
import entity.AdvertisementImages;
import entity.AdvertisementStores;
import entity.Advertisements;
import entity.ClickValues;
import entity.HistorySearch;
import entity.Stopwords;
import entity.Stores;
import entity.Users;
import utils.Redirect;
import utils.Utilities;

@WebFilter("/*")
public final class UrlFilter implements Filter {

    public UrlFilter() {}

	public void init(FilterConfig fConfig) throws ServletException {}

	public void destroy() {}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String giroUrlServer = "https://www.giroferta.com/";
		String giroUrlLocal = "http://localhost:8080/GirofertaWebApp/";
		String giroIP = "67.205.107.230";
		String giroUrl = "";
//		int ads_home = 100;
		HttpServletRequest httpServletRequest = null; 
		HttpServletResponse httpServletResponse = null;
		Connection connection = null;
		Redirect redirect = null;
		Config config = null;
		Utilities utilities = null;
		boolean isPrintAllParams = false;
		boolean isPrintPathImage = false;
		boolean isPrintConnection = false;
		boolean isPrintURL = true;
		boolean isPrintToken = false;
		String uri = null;
		String[] tokens = null;
		int sizeTokens = 0;
		String token = null;
		String context = null;
		String schema = null;
		String server = null;
		int port = 0;
		boolean secure = false;
		String pathImages = null;
		String pathSitemap = null;
		File uploadPath = null;
		String form = null;
		String action = null;
		Map<String, Object> param = null;
		Users user = null;
		Advertisements advertisement = null;
		List<Advertisements> listResultSearch = null;
		List<Advertisements> listAdvMostAccessed = null;
		List<Advertisements> listAdvRecentlyAdded = null;
		List<AdvertisementStores> listMostAccessed = null;
		List<AdvertisementStores> listRecentlyAdded = null;
		
		try {
			request.setCharacterEncoding("UTF-8");
			httpServletRequest = (HttpServletRequest) request; 
			httpServletResponse = (HttpServletResponse) response;
			redirect = new Redirect();
			config = new Config();
			utilities = new Utilities();
			if(httpServletRequest.getRequestURI().matches(".*(properties|css|js|jpg|png|gif|ico|map|eot|svg|ttf|woff|woff2|html|xml|txt)$")){
			    chain.doFilter(request, response);
			    return;
			}
			
			uri = httpServletRequest.getRequestURI();
			tokens = uri.split(Pattern.quote("/"));
			sizeTokens = tokens.length;
			token = "";
			context = httpServletRequest.getContextPath();
			schema = request.getScheme();
			server = request.getServerName();
			port = request.getServerPort();
			secure = request.isSecure();
			
			if (sizeTokens == 0)
				token = "/";
			else if (tokens.length > 1)
				token = tokens[sizeTokens - 1];
			else
				token = uri;
			
			if (server.equals("www.giroferta.com") || server.equals("giroferta.com") || server.equals(giroIP))
				giroUrl = giroUrlServer;
			else
				giroUrl = giroUrlLocal;
			
			utilities.printDataHora(" | PROTOCOL: " + schema + " | SERVER: " + server + " | PORT: " + port + " | CONTEXT: " + context + " | URI: " + uri + " | TOKEN: " + token + " | SECURE: " + secure + " | NEW: " + httpServletRequest.getSession().isNew() + " | TOKENS: " + sizeTokens, isPrintAllParams);
			
			if (httpServletRequest.getSession().getAttribute("pathImages") == null) {
				pathImages = (String) request.getServletContext().getAttribute("pathImages");
				httpServletRequest.getSession().setAttribute("pathImages", pathImages);
				utilities.printDataHora(" - PATH IMAGES: " + pathImages, isPrintPathImage);
				uploadPath = new File(pathImages);
				if (!uploadPath.exists()) {
					uploadPath.mkdirs();
					utilities.printDataHora(" - PATH CREATED!", isPrintPathImage);
				}
				else
					utilities.printDataHora(" - PATH ALREADY EXISTS!", isPrintPathImage);
				uploadPath.setWritable(true, true);
			}
			
			if (httpServletRequest.getSession().getAttribute("pathSitemap") == null) {
				pathSitemap = (String) request.getServletContext().getAttribute("pathSitemap");
				httpServletRequest.getSession().setAttribute("pathSitemap", pathSitemap);
			}

			if (httpServletRequest.getSession().getAttribute("connection") == null) {
				utilities.printDataHora(" - CRIADA UMA NOVA CONEXÃO", isPrintConnection);
				connection = new Connection((SessionFactory) request.getServletContext().getAttribute("sessionFactory"));
				httpServletRequest.getSession().setAttribute("connection", connection);
			}
			else {
				utilities.printDataHora(" - UTILIZANDO CONEXÃO JÁ EXISTENTE", isPrintConnection);
				connection = (Connection) httpServletRequest.getSession().getAttribute("connection");
			}
			
			if (httpServletRequest.getSession().isNew()) {
				config.setConfig(httpServletRequest, httpServletResponse);
				httpServletRequest.getSession().setAttribute("giroUrl", giroUrl);
				httpServletRequest.getSession().setAttribute("giroUrlServerImages", giroUrlServer);
				
			}
	
			
			// redireciona para o login se tentar acessar telas do painel sem estar logado
			if (httpServletRequest.getSession().getAttribute("currentUser") == null) {
				if (httpServletRequest.getParameter("panel") != null && httpServletRequest.getParameter("panel").equals("true")) {
					if (httpServletRequest.getParameter("form") != null && httpServletRequest.getParameter("action") != null) {
						form = httpServletRequest.getParameter("form");
						action = httpServletRequest.getParameter("action");
						if (!form.endsWith("UsersActions") && (!action.equals("form") || !action.equals("login"))) {
							httpServletRequest.getRequestDispatcher("/Controller?form=actions.UsersActions&action=login&panel=true").forward(httpServletRequest, httpServletResponse);
							return;
						}
					}
				}
			}
			
			if (httpServletRequest.getSession().isNew() || httpServletRequest.getSession().getAttribute("listMostAccessed") == null) {
				Admin admin = null;
				ClickValues clickValues = null;
				try {
					connection.beginTransaction();
					admin = (Admin) connection.find(Admin.class, 1);
					
//					listMostAccessed = getListAdsStores(connection, (admin.getLimit_most_accessed() * 6), 0);
//					listRecentlyAdded = getListAdsStores(connection,(admin.getLimit_new_ads() * 2), 1);
//					
//					Collections.shuffle(listMostAccessed);
//					Collections.shuffle(listRecentlyAdded);
//
//					httpServletRequest.getSession().setAttribute("listMostAccessed", listMostAccessed.subList(0, 8));
//					httpServletRequest.getSession().setAttribute("listNewAds", listRecentlyAdded.subList(0, 12));
					
					httpServletRequest.getSession().setAttribute("listMostAccessed", getListAdsStores(connection, admin.getLimit_most_accessed(), 0));
					httpServletRequest.getSession().setAttribute("listNewAds", getListAdsStores(connection, admin.getLimit_new_ads(), 1));
					
					httpServletRequest.getSession().setAttribute("listStoresHome", connection.list(Users.QUERY_STORES_HOME, 6));
//					advertisementsDAO = new AdvertisementsDAO(connection);
//					advertisementsDAO.getAdvertisementsHome("%%", httpServletRequest.getParameter("lat"), httpServletRequest.getParameter("lng"), 25, httpServletRequest);
					httpServletRequest.getSession().setAttribute("maxCourtesy", admin.getMax_courtesy());
					httpServletRequest.getSession().setAttribute("adminRadius", admin.getRadius());
					httpServletRequest.getSession().setAttribute("adminTotalAdvActives", admin.getTotal_adv_actives());
					httpServletRequest.getSession().setAttribute("adminTotalAdv", admin.getTotal_adv());
					httpServletRequest.getSession().setAttribute("adminTotalStores", admin.getTotal_stores());
					httpServletRequest.getSession().setAttribute("logUrl", admin.getLog_url());
			        clickValues = (ClickValues) connection.find(ClickValues.class, 1); 
			        httpServletRequest.getSession().setAttribute("basicClickValue", clickValues.getValue());
				} finally {
					connection.closeTransaction();
					admin = null;
					clickValues = null;
				}
			}
//			else {
//				List<AdvertisementStores> listMostAccessed = (List<AdvertisementStores>) httpServletRequest.getSession().getAttribute("listMostAccessed");
//				List<AdvertisementStores> listNewAds = (List<AdvertisementStores>) httpServletRequest.getSession().getAttribute("listNewAds");
//				Collections.shuffle(listMostAccessed);
//				Collections.shuffle(listNewAds);
//				httpServletRequest.getSession().setAttribute("listMostAccessed", listMostAccessed.subList(0, 8));
//				httpServletRequest.getSession().setAttribute("listNewAds", listNewAds.subList(0, 12));
//			}
			
			if (httpServletRequest.getSession().getAttribute("logUrl") != null && (Integer) httpServletRequest.getSession().getAttribute("logUrl") == 1)
				isPrintURL = true;
			else
				isPrintURL = false;
			
			utilities.printDataHora(" | URL: " + httpServletRequest.getRequestURL(), isPrintURL);
//			utilities.printDataHora(" | URI: " + httpServletRequest.getRequestURI(), isPrintURL);
			
			if (((server.equals("www.giroferta.com") || server.equals("giroferta.com") || server.equals(giroIP)) && token.equals("/") && sizeTokens == 0) 
					 || (server.equals("localhost") && token.equals("GirofertaWebApp") && sizeTokens == 2)) {
				if (httpServletRequest.getSession().isNew()) {
					httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
					httpServletResponse.setHeader("location", giroUrl);
					utilities.printDataHora(" | HOME - NEW...", isPrintToken);
				}
				chain.doFilter(request, response);
			}
			
			else if (sizeTokens >= 2 && (!token.equals("/") && !token.equals("GirofertaWebApp"))) {
				
/*				if (httpServletRequest.getSession().isNew()) {
					httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
					httpServletResponse.setHeader("location", giroUrl + token);
				}
				
				else */	if (token.equals("Controller")) {
					utilities.printDataHora(" | Controller...", isPrintToken);
					chain.doFilter(request, response);
				}
				
				else if (token.equals("home")) {
					utilities.printDataHora(" | home...", isPrintToken);
					httpServletResponse.sendRedirect(giroUrl);
				}
				
				else if (token.equals("busca")) {
					utilities.printDataHora(" | busca...", isPrintToken);
					boolean isFound = false;
					HistorySearch historySearch = null;
					List<Integer> listIdsAdvertisements = null;
					Advertisements adv = null;
					int amount = 0;
					try {
						if (request.getParameter("q") != null) {
							connection.beginTransaction();
							if (param == null)
								param = new HashMap<String, Object>();
							if (request.getParameter("loja") != null) {
								listIdsAdvertisements = searchInStore(connection, "%" + request.getParameter("q") + "%", request.getParameter("loja"));
								if (listIdsAdvertisements.size() > 0) {
									listResultSearch = new ArrayList<Advertisements>();
									for (int advertisement_id : listIdsAdvertisements) {
										adv = (Advertisements) connection.find(Advertisements.class, advertisement_id);
										if (adv != null)
											listResultSearch.add(adv);
									}
								}
							} else {
								listIdsAdvertisements = searchInStore(connection, "%" + request.getParameter("q") + "%", null);
								if (listIdsAdvertisements.size() > 0) {
									listResultSearch = new ArrayList<Advertisements>();
									for (int advertisement_id : listIdsAdvertisements) {
										adv = (Advertisements) connection.find(Advertisements.class, advertisement_id);
										if (adv != null)
											listResultSearch.add(adv);
									}
								}
							}
							
							if (listResultSearch != null && listResultSearch.size() > 0) {
								httpServletRequest.setAttribute("list", listResultSearch);
								isFound = true;
								amount = listResultSearch.size();
							}
							
							historySearch = new HistorySearch();
							if (httpServletRequest.getSession().getAttribute("currentUser") != null)
								historySearch.setLogin(1);
							else
								historySearch.setLogin(0);
							historySearch.setTerm(request.getParameter("q"));
							historySearch.setAmount(amount);
							if (isFound)
								historySearch.setFound(1);
							else
								historySearch.setFound(0);
							historySearch.setSource("S"); // S = store
							historySearch.setLatitude("");
							historySearch.setLongitude("");
							historySearch.setCreated_at(new Date());
							connection.save(historySearch);
							connection.commit();

							param.clear();
						} else
							httpServletResponse.sendRedirect(giroUrl + "pageNotFound");
						
					} finally {
						connection.closeTransaction();
						historySearch = null;
						listIdsAdvertisements = null;
						adv = null;
					}
					
/*					utilities.printNameValue(httpServletRequest.getParameterMap(), true);
					
					String user_url = null;
					String valueSearch = null;
//					Map<String, Object> param = null;
					List<?> listAdvertisements = null;
					int current = 0;		
					int maximum = 10;
					int totalResults = 0;			
					int totalPages = 0;
					String nav = null;
					try {
						connection.beginTransaction();
						if (param ==null)
							param = new HashMap<String, Object>();
						user_url = httpServletRequest.getParameter("user_url");
						valueSearch = httpServletRequest.getParameter("q");
//						if (valueSearch == null)
//							valueSearch = "";
//						else
//							valueSearch = valueSearch.replaceAll("%", "");
						if (httpServletRequest.getParameter("nav") == null) {
							param.put("user_url", user_url);
							param.put("value_search", "%" + valueSearch + "%");
							totalResults = connection.count(Advertisements.QUERY_COUNT_BY_TITLE_AND_BRAND, param);
							param.clear();
							if (totalResults > 0) {
								totalPages = totalResults / maximum;
							}
							else {
								totalResults = 0;			
								totalPages = 0;
							}
							httpServletRequest.getSession().setAttribute("totalResults", totalResults);
							httpServletRequest.getSession().setAttribute("totalPages", totalPages);
						}
						else {
							nav = httpServletRequest.getParameter("nav");
							current = (int) httpServletRequest.getSession().getAttribute("current");
							maximum = (int) httpServletRequest.getSession().getAttribute("maximum");
							totalResults = (int) httpServletRequest.getSession().getAttribute("totalResults");
							totalPages = (int) httpServletRequest.getSession().getAttribute("totalPages");
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
						param.put("user_url", user_url);
						param.put("value_search", "%" + valueSearch + "%");
						listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_TITLE_AND_BRAND, param, current, maximum);
						param.clear();
						httpServletRequest.setAttribute("user_url", user_url);
						httpServletRequest.setAttribute("q", valueSearch);
						httpServletRequest.getSession().setAttribute("current", current);
						httpServletRequest.getSession().setAttribute("maximum", maximum);
						httpServletRequest.setAttribute("navActionFirst", httpServletRequest.getSession().getAttribute("linkAdvertisementsListFirst"));
						httpServletRequest.setAttribute("navActionPrevious", httpServletRequest.getSession().getAttribute("linkAdvertisementsListPrevious"));
						httpServletRequest.setAttribute("navActionNext", httpServletRequest.getSession().getAttribute("linkAdvertisementsListNext"));
						httpServletRequest.setAttribute("navActionLast", httpServletRequest.getSession().getAttribute("linkAdvertisementsListLast"));
						httpServletRequest.setAttribute("searchAction", httpServletRequest.getSession().getAttribute("linkAdvertisementsListSearch"));
						httpServletRequest.setAttribute("list", listAdvertisements);
					} finally {
						connection.closeTransaction();
//						utilities = null;
						valueSearch = null;
						param = null;
						listAdvertisements = null;
						user = null;
						nav = null;
					}
*/					redirect.toPage("busca.jsp", false, httpServletRequest, httpServletResponse);
				}
				
				else if (token.equals("login")) {
					utilities.printDataHora(" | login...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.UsersActions&action=login&panel=true").forward(request, response);
				}
				
				else if (token.equals("checklogin")) {
					utilities.printDataHora(" | checkLogin...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.UsersActions&action=check&panel=false").forward(request, response);
				}
				
				else if (token.equals("logout")) {
					utilities.printDataHora(" | logout...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.UsersActions&action=logout&panel=false").forward(request, response);
				}
				
				else if (token.equals("register")) {
					utilities.printDataHora(" | register...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.UsersActions&action=form&panel=true").forward(request, response);
				}
				
//				else if (token.startsWith("advertisement")) {
//					utilities.printDataHora(" | advertisement... " + token, true);
//					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=advertisement&panel=true").forward(request, response);
//				}
				
				else if (token.startsWith("publish")) {
					utilities.printDataHora(" | list-advertisements "+token+"...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=publish&panel=true&publish"+token).forward(request, response);
				}
				
				else if (token.startsWith("unpublish")) {
					utilities.printDataHora(" | list-advertisements "+token+"...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=publish&panel=true&publish"+token).forward(request, response);
				}
				
				else if (token.equals("meus-anuncios")) {
					utilities.printDataHora(" | list-advertisements...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=list&panel=true").forward(request, response);
				}
				
				else if (uri.endsWith("meus-anuncios/first") || uri.endsWith("meus-anuncios/previous") || uri.endsWith("meus-anuncios/next") || uri.endsWith("meus-anuncios/last")) {
					utilities.printDataHora(" | list-advertisements "+token+"...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=list&panel=true&nav="+token).forward(request, response);
				}
				
				else if (uri.endsWith("meus-anuncios/search")) {
					utilities.printDataHora(" | list-advertisements search...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=list&panel=true").forward(request, response);
				}
				
				else if (token.equals("minhas-lojas")) {
					utilities.printDataHora(" | list-stores...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.StoresActions&action=list&panel=true").forward(request, response);
				}
				
				else if (uri.endsWith("minhas-lojas/first") || uri.endsWith("minhas-lojas/previous") || uri.endsWith("minhas-lojas/next") || uri.endsWith("minhas-lojas/last")) {
					utilities.printDataHora(" | list-stores "+token+"...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.StoresActions&action=list&panel=true&nav="+token).forward(request, response);
				}
				
				else if (uri.endsWith("minhas-lojas/search")) {
					utilities.printDataHora(" | list-stores search...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.StoresActions&action=list&panel=true").forward(request, response);
				}
				
				else if (token.equals("fique-por-dentro")) {
					utilities.printDataHora(" | fique-por-dentro...", isPrintToken);
					redirect.toPage("fique-por-dentro.jsp", false, httpServletRequest, httpServletResponse);	
				}
				
				else if (token.equals("resetpassword")) {
					utilities.printDataHora(" | resetpassword...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.UsersActions&action=formSendInstructionsResetPassword&panel=true").forward(request, response);
				}
				
				else if (token.equals("reportByAdv")) {
					utilities.printDataHora(" | report...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=reportByAdv&panel=true").forward(request, response);
				}
				
				else if (uri.endsWith("reportByAdv/first") || uri.endsWith("reportByAdv/previous") || uri.endsWith("reportByAdv/next") || uri.endsWith("reportByAdv/last")) {
					utilities.printDataHora(" | report "+token+"...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=reportByAdv&panel=true&nav="+token).forward(request, response);
				}
				
				else if (uri.endsWith("reportByAdv/search")) {
					utilities.printDataHora(" | report search...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=reportByAdv&panel=true").forward(request, response);
				}
				
				else if (token.equals("reportByDate")) {
					utilities.printDataHora(" | report...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=reportByDate&panel=true").forward(request, response);
				}
				
				else if (uri.endsWith("reportByDate/first") || uri.endsWith("reportByDate/previous") || uri.endsWith("reportByDate/next") || uri.endsWith("reportByDate/last")) {
					utilities.printDataHora(" | report "+token+"...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=reportByDate&panel=true&nav="+token).forward(request, response);
				}
				
				else if (uri.endsWith("reportByDate/search")) {
					utilities.printDataHora(" | report search...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdvertisementsActions&action=reportByDate&panel=true").forward(request, response);
				}
				
				else if (token.equals("minhas-contas")) {
					utilities.printDataHora(" | list-stores...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=list&panel=true").forward(request, response);
				}
				
				else if (uri.endsWith("minhas-contas/first") || uri.endsWith("minhas-contas/previous") || uri.endsWith("minhas-contas/next") || uri.endsWith("minhas-contas/last")) {
					utilities.printDataHora(" | list-stores "+token+"...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=list&panel=true&nav="+token).forward(request, response);
				}
				
				else if (uri.endsWith("minhas-contas/search")) {
					utilities.printDataHora(" | list-stores search...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=list&panel=true").forward(request, response);
				}
				
				else if (token.equals("sales")) {
					utilities.printDataHora(" | sales...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=sales&panel=true").forward(request, response);
				}
	
				else if (token.equals("managerSite")) {
					utilities.printDataHora(" | managerSite...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=setup&panel=true").forward(request, response);
				}
	
				else if (token.equals("jobs")) {
					utilities.printDataHora(" | jobs...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=jobs&panel=true").forward(request, response);
				}
	
				else if (token.equals("reports")) {
					utilities.printDataHora(" | reports admin...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=reports&panel=true").forward(request, response);
				}
	
				else if (token.equals("reportAdminByAdv")) {
					utilities.printDataHora(" | report admin...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=reportByAdv&panel=true").forward(request, response);
				}
	
				else if (uri.endsWith("reportAdminByAdv/first") || uri.endsWith("reportAdminByAdv/previous") || uri.endsWith("reportAdminByAdv/next") || uri.endsWith("reportAdminByAdv/last")) {
					utilities.printDataHora(" | reports admin "+token+"...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=reportByAdv&panel=true&nav="+token).forward(request, response);
				}
				
				else if (uri.endsWith("reportAdminByAdv/search")) {
					utilities.printDataHora(" | reports admin search...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=reportByAdv&panel=true").forward(request, response);
				}
				
				else if (token.equals("reportAdminByDate")) {
					utilities.printDataHora(" | reports admin...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=reportByDate&panel=true").forward(request, response);
				}
	
				else if (uri.endsWith("reportAdminByDate/first") || uri.endsWith("reportAdminByDate/previous") || uri.endsWith("reportAdminByDate/next") || uri.endsWith("reportAdminByDate/last")) {
					utilities.printDataHora(" | reports admin "+token+"...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=reportByDate&panel=true&nav="+token).forward(request, response);
				}
				
				else if (uri.endsWith("reportAdminByDate/search")) {
					utilities.printDataHora(" | reports admin search...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.AdminActions&action=reportByDate&panel=true").forward(request, response);
				}
				
				else if (token.equals("terms")) {
					utilities.printDataHora(" | terms...", isPrintToken);
					request.getRequestDispatcher("/Controller?form=actions.UsersActions&action=terms&panel=true").forward(request, response);
				}
	
				else if (token.equals("pageNotFound")) {
					utilities.printDataHora(" | pageNotFound...", isPrintToken);
					redirect.toPage("pageNotFound.jsp", false, httpServletRequest, httpServletResponse);
				}
				
				else if (token.equals("pageUnavailable")) {
					utilities.printDataHora(" | pageUnavailable...", isPrintToken);
					redirect.toPage("pageUnavailable.jsp", false, httpServletRequest, httpServletResponse);
				}
				
				else {
					if (sizeTokens > 3) {
						int advertisementId = 0; 
						if (server.equals("www.giroferta.com") || server.equals("giroferta.com") || server.equals(giroIP) && tokens[1].equals("advertisement"))
							advertisementId = Integer.parseInt(tokens[2]);
						else if (server.equals("localhost") && tokens[2].equals("advertisement"))
							advertisementId = Integer.parseInt(tokens[3]);
						else
							httpServletResponse.sendRedirect(giroUrl + "pageNotFound");
						try {
							connection.beginTransaction();
							advertisement = (Advertisements) connection.find(Advertisements.class, advertisementId);
							if (advertisement.getStatus() == 2) {
								httpServletRequest.setAttribute("currentAdv", advertisementId);							
								httpServletRequest.setAttribute("currentAdvertisement", advertisement);
								if (advertisement.getListAdvertisementStores() != null && advertisement.getListAdvertisementStores().size() != 0) {
									httpServletRequest.setAttribute("currentStore", advertisement.getListAdvertisementStores().get(0).getStore_id());
									if (param == null)
										param = new HashMap<String, Object>();
									param.put("advertisement_id", advertisement.getId());
									httpServletRequest.setAttribute("listAdvertisementImages", (List<AdvertisementImages>) connection.list(AdvertisementImages.QUERY_BY_ADVERTISEMENT, param));
									param.clear();
									redirect.toPage("advertisement.jsp", false, httpServletRequest, httpServletResponse);
								}
								else
									httpServletResponse.sendRedirect(giroUrl + "pageUnavailable");
							}
							else
								httpServletResponse.sendRedirect(giroUrl + "pageUnavailable");

						} finally {
							connection.closeTransaction();
						}
						redirect.toPage("advertisement.jsp", false, httpServletRequest, httpServletResponse);
					} else {
						try {
							connection.beginTransaction();
							if (param == null)
								param = new HashMap<String, Object>();
							param.put("url", token);
							user = (Users) connection.find(Users.QUERY_BY_URL, param);
							param.clear();
							if (user != null && user.getCount_advertisements() > 0) {
								if(user.getCount_advertisements() > 0) {
									user.setGross_click(user.getGross_click() + 1);
									connection.update(user);
									connection.commit();
									utilities.printDataHora(" | url store...", isPrintToken);
									param.put("user_id", user.getId());
//									httpServletRequest.setAttribute("listStoreAddress", connection.list(Stores.QUERY_BY_USER, param));
									
									listAdvMostAccessed = (List<Advertisements>) connection.list(Advertisements.QUERY_MOST_ACCESSED, param, 32);
									if (listAdvMostAccessed != null && listAdvMostAccessed.size() > 0) {
										Collections.shuffle(listAdvMostAccessed);
										if (listAdvMostAccessed.size() >= 8)
											httpServletRequest.setAttribute("listAdvMostAccessed", listAdvMostAccessed.subList(0, 8));
										else
											httpServletRequest.setAttribute("listAdvMostAccessed", listAdvMostAccessed.subList(0, listAdvMostAccessed.size()));
									}
									
									listAdvRecentlyAdded = (List<Advertisements>) connection.list(Advertisements.QUERY_RECENTLY_ADDED, param, 24);
									if (listAdvRecentlyAdded != null && listAdvRecentlyAdded.size() > 0) {
										Collections.shuffle(listAdvRecentlyAdded);
										if (listAdvRecentlyAdded.size() >= 12)
											httpServletRequest.setAttribute("listAdvRecentlyAdded", listAdvRecentlyAdded.subList(0, 12));
										else
											httpServletRequest.setAttribute("listAdvRecentlyAdded", listAdvRecentlyAdded.subList(0, listAdvRecentlyAdded.size()));
									}
										
									param.clear();
									
//									httpServletRequest.setAttribute("currentStore", user.getUrl());
//									httpServletRequest.setAttribute("currentStore", user.getListStores().get(0));
									httpServletRequest.setAttribute("currentStore", user);
//									redirect.toPage("home.jsp", false, httpServletRequest, httpServletResponse);
//									redirect.toPage("store.jsp", false, httpServletRequest, httpServletResponse);
								}
								else
									httpServletResponse.sendRedirect(giroUrl + "pageNotFound");
							}
							else 
								httpServletResponse.sendRedirect(giroUrl + "pageNotFound");
						} finally {
							connection.closeTransaction();
						}
//						redirect.toPage("home.jsp", false, httpServletRequest, httpServletResponse);
						redirect.toPage("store.jsp", false, httpServletRequest, httpServletResponse);
					}
				}
			}
			else
				httpServletResponse.sendRedirect(giroUrl + "pageNotFound");
		} finally {
			giroUrlServer = null;
			giroUrlLocal = null;
			giroIP = null;
			giroUrl = null;
			httpServletRequest = null; 
			httpServletResponse = null;
			redirect = null;
			config = null;
			utilities = null;
			uri = null;
			tokens = null;
			token = null;
			context = null;
			schema = null;
			server = null;
			pathImages = null;
			pathSitemap = null;
			uploadPath = null;
			form = null;
			action = null;
			param= null;
			user = null;
			advertisement = null;
			listResultSearch = null;
			listAdvMostAccessed = null;
			listAdvRecentlyAdded = null; 
			listMostAccessed = null;
			listRecentlyAdded = null;
		}
		return;
	}
	
	private List<AdvertisementStores> getListAdsStores2(Connection connection, int limitMostAccessed, int typeList) {
		return connection.getSession().doReturningWork( 
				new ReturningWork<List<AdvertisementStores>>() {
				@Override
				public List<AdvertisementStores> execute(java.sql.Connection con) throws SQLException {
					List<AdvertisementStores> listAux = new ArrayList<AdvertisementStores>();
					AdvertisementStores advSto = null;
					PreparedStatement pst = null;
					ResultSet rst = null; 
					String sql = null;
					if (typeList == 0)
						sql = 
							" SELECT advsto.* FROM advertisement_stores AS advsto "
							+ " INNER JOIN "
							+ " 	advertisements AS adv "
							+ " ON advsto.advertisement_id = adv.id "
							+ " INNER JOIN "
							+ " 	stores AS sto "
							+ " ON advsto.store_id = sto.id "
							+ " INNER JOIN "
							+ " 	users AS user "
							+ " ON sto.user_id = user.id "
							+ " WHERE "
							+ " 	advsto.status = 1 AND "
							+ " 	adv.status = 2 AND "
							+ " 	sto.status = 1 AND "
							+ "		adv.qtda_images > 0 AND "
							+ "		user.balance >= ? "
							+ " ORDER BY advsto.gross_click DESC "
							+ " LIMIT ? ";
					else 
						sql = 
							" SELECT "
							+ " DISTINCT "
							+ " 	advsto.* "
							+ " FROM advertisement_stores AS advsto "
							+ " INNER JOIN "
							+ " 	advertisements AS adv "
							+ " ON advsto.advertisement_id = adv.id "
							+ " INNER JOIN "
							+ " 	stores AS sto "
							+ " ON advsto.store_id = sto.id "
							+ " INNER JOIN "
							+ " 	users AS user "
							+ " ON sto.user_id = user.id "
							+ " WHERE "
							+ " 	advsto.status = 1 AND "
							+ " 	adv.status = 2 AND "
							+ " 	sto.status = 1 AND "
							+ "		adv.qtda_images > 0 AND "
							+ "		user.balance >= ? "
							+ " GROUP BY adv.created_at "
							+ " ORDER BY adv.created_at DESC "
							+ " LIMIT ? ";
					try {
						pst = con.prepareStatement(sql);
						pst.setBigDecimal(1, (((ClickValues)connection.find(ClickValues.class, 1)).getValue()));
						pst.setInt(2, limitMostAccessed);
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
	
	private List<AdvertisementStores> getListAdsStores(Connection connection, int limit, int typeList) {
		return connection.getSession().doReturningWork( 
			new ReturningWork<List<AdvertisementStores>>() {
			@Override
			public List<AdvertisementStores> execute(java.sql.Connection con) throws SQLException {
				List<AdvertisementStores> listAux = new ArrayList<AdvertisementStores>();
				AdvertisementStores advSto = null;
				PreparedStatement pst = null;
				ResultSet rst = null; 
				String sql = null;
				if (typeList == 0)
					sql = " SELECT * FROM ads_stores_most_accessed AS advsto "
//						+ " ORDER BY count_click DESC "
						+ " ORDER BY RAND() "
						+ " LIMIT ? ";
				else 
					sql = " SELECT * FROM ads_stores_recently_added AS advsto "
//						+ " ORDER BY created_at DESC "
						+ " ORDER BY RAND() "
						+ " LIMIT ? ";
				try {
					pst = con.prepareStatement(sql);
					pst.setInt(1, limit);
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
	
	private List<Integer> searchInStore(Connection connection, String value_search, String loja) {
		String value_search_aux = "";
		String[] tokensAux;
		Map<String, Object> param = new HashMap<String, Object>();
		List<String> tokensValidos = new ArrayList<String>();
		List<Integer> listIdsAdvertisements = null;
		int countTokens = 0;
		if (!value_search.equals("%%")) {
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
			listIdsAdvertisements =	getListAdvertisementsIds(connection, value_search_aux.toLowerCase(), loja);
		}
		return listIdsAdvertisements;
	}
	
	private List<Integer> getListAdvertisementsIds(Connection connection, String value_search, String loja) {
		return connection.getSession().doReturningWork( 
			new ReturningWork<List<Integer>>() {
			@Override
			public List<Integer> execute(java.sql.Connection con) throws SQLException {
				List<Integer> listAux = new ArrayList<Integer>();
				PreparedStatement pst = null;
				ResultSet rst = null;
				String sql = null;
				if (loja != null) {
					sql = 
						" SELECT adv.id FROM advertisements AS adv "
						+ " INNER JOIN users AS user ON adv.user_id = user.id "
						+ " INNER JOIN categories AS cat ON adv.category_id = cat.id "
						+ " WHERE "
						+ " (adv.title REGEXP ? OR adv.brand REGEXP ? OR cat.keywords REGEXP ?) AND "
						+ " adv.status = 2 AND "
						+ " user.url = ? ";
				}
				else {
					sql = 
						" SELECT adv.id FROM advertisements AS adv "
						+ " INNER JOIN categories AS cat ON adv.category_id = cat.id "
						+ " WHERE "
						+ " (adv.title REGEXP ? OR adv.brand REGEXP ? OR cat.keywords REGEXP ?) AND "
						+ " adv.status = 2 ";
				}
				try {
					pst = con.prepareStatement(sql);
					pst.setString(1, value_search);
					pst.setString(2, value_search);
					pst.setString(3, value_search);
					if (loja != null)
						pst.setString(4, loja);
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
	
}
