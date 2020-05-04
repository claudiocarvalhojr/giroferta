package dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import entity.Admin;
import entity.Advertisements;
import entity.ClickValues;
import entity.HistoryCountClick;
import entity.HistoryCourtesy;
import entity.HistoryGrossClick;
import entity.HistoryMostAccessed;
import entity.HistoryPurchases;
import entity.HistoryRecentlyAdded;
import entity.Users;
import jobs.FixesImages;
import jobs.ImportImages;
import jobs.ImportXML;
import jobs.Jobs;
import jobs.ReplyAdsInStores;
import jobs.UpdateHighLights;
import jobs.UpdateSitemap;
import spinwork.util.Validations;
import utils.Encode;
import utils.Utilities;

public class AdminDAO {
	
	private static final String MANAGE_ACCOUNTS = "admin/accounts.jsp";
	private static final String COURTESY = "admin/courtesy.jsp";
	private static final String LIST = "admin/list.jsp";
	private static final String SALES = "admin/sales.jsp";
	private static final String MANAGER = "admin/manager.jsp";
	private static final String JOBS = "admin/jobs.jsp";
	private static final String REPORTS = "admin/reports.jsp";
	private static final String REPORT_BY_ADV = "admin/report_by_adv.jsp";
	private static final String REPORT_BY_DATE = "admin/report_by_date.jsp";
	private Connection connection = null;
	
	public AdminDAO(Connection connection) {
		this.connection = connection;
	}
	
	public String manageAccountsForm(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		return MANAGE_ACCOUNTS;
	}

	@SuppressWarnings("unchecked")
	public String manageAccountsSearch(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users userManageAccounts = null; 
		try {
			connection.beginTransaction();
			userManageAccounts = getUser(request, request.getParameter("tfManageAccountsCpfCnpj"));
 			if (userManageAccounts != null) {
				request.setAttribute("listClickValues", (List<ClickValues>) connection.list(ClickValues.QUERY_ALL));
				request.setAttribute("userManageAccounts", userManageAccounts);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			userManageAccounts = null;
		}
		request.setAttribute("tfManageAccountsCpfCnpj", request.getParameter("tfManageAccountsCpfCnpj"));
		return MANAGE_ACCOUNTS;
	}

	@SuppressWarnings("unchecked")
	public String manageAccountsApplyActive(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users userManageAccounts = null; 
		try {
			connection.beginTransaction();
			userManageAccounts = getUser(request, request.getParameter("tfManageAccountsCpfCnpj"));
 			if (userManageAccounts != null) {
				if (request.getParameter("selManageAccountsActive") != null) {
					if (request.getParameter("selManageAccountsActive").equals("1")) {
						if (userManageAccounts.getStatus() == 0) {
							userManageAccounts.setStatus(Integer.parseInt(request.getParameter("selManageAccountsActive")));
							connection.update(userManageAccounts);
							connection.commit();
							request.setAttribute("message", "Conta ativada!");
						}
						else 
							request.setAttribute("message", "Conta já ativa, não pode ser ativada novamente!");
					}
					else
						request.setAttribute("message", "Conta não pode ser desativada!");
				}
				else 
					request.setAttribute("message", "Algo deu errado!");
				request.setAttribute("listClickValues", (List<ClickValues>) connection.list(ClickValues.QUERY_ALL));
				request.setAttribute("userManageAccounts", userManageAccounts);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			userManageAccounts = null;
		}
		request.setAttribute("tfManageAccountsCpfCnpj", request.getParameter("tfManageAccountsCpfCnpj"));
		return MANAGE_ACCOUNTS;
	}
	
	@SuppressWarnings("unchecked")
	public String manageAccountsApplyProfile(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users userManageAccounts = null; 
		try {
			connection.beginTransaction();
			userManageAccounts = getUser(request, request.getParameter("tfManageAccountsCpfCnpj"));
 			if (userManageAccounts != null) {
				if (request.getParameter("selManageAccountsProfile") != null) {
					if (request.getParameter("selManageAccountsProfile").equals("2")) {
						userManageAccounts.setType_account(2);
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Perfil alterado!");
					}
					else if (request.getParameter("selManageAccountsProfile").equals("5")) {
						userManageAccounts.setType_account(5);
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Perfil alterado!");
					}
					else if (request.getParameter("selManageAccountsProfile").equals("10")) {
						if (!userManageAccounts.getCpf().equals("") || !userManageAccounts.getCnpj().equals(""))
							userManageAccounts.setType_account(10);
						else
							userManageAccounts.setType_account(11);
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Perfil alterado!");
					}
					else
						request.setAttribute("message", "Algo deu errado!");
				}
				else 
					request.setAttribute("message", "Algo deu errado!");
				request.setAttribute("listClickValues", (List<ClickValues>) connection.list(ClickValues.QUERY_ALL));
				request.setAttribute("userManageAccounts", userManageAccounts);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			userManageAccounts = null;
		}
		request.setAttribute("tfManageAccountsCpfCnpj", request.getParameter("tfManageAccountsCpfCnpj"));
		return MANAGE_ACCOUNTS;
	}
	
	@SuppressWarnings("unchecked")
	public String manageAccountsApplyPassword(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users userManageAccounts = null;
		Encode encode = null;
		try {
			connection.beginTransaction();
			userManageAccounts = getUser(request, request.getParameter("tfManageAccountsCpfCnpj"));
 			if (userManageAccounts != null) {
				if (request.getParameter("tfManageAccountsPassword") != null && !request.getParameter("tfManageAccountsPassword").equals("")) {
					if (utilities.isPassword(request.getParameter("tfManageAccountsPassword"))) {
						encode = new Encode();
						userManageAccounts.setEncrypted_password(encode.encryptsPassword(request.getParameter("tfManageAccountsPassword")));
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Senha alterada!");
					}
					else 
						request.setAttribute("message", "Senha inválida, formato incorreto!");
				}
				else 
					request.setAttribute("message", "Senha inválida, o campo Nova Senha deve ser preenchido!");
				request.setAttribute("listClickValues", (List<ClickValues>) connection.list(ClickValues.QUERY_ALL));
				request.setAttribute("userManageAccounts", userManageAccounts);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			userManageAccounts = null;
			encode = null;
		}
		request.setAttribute("tfManageAccountsCpfCnpj", request.getParameter("tfManageAccountsCpfCnpj"));
		return MANAGE_ACCOUNTS;
	}
	
	@SuppressWarnings("unchecked")
	public String manageAccountsApplyExtraCredit(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users userManageAccounts = null;
		BigDecimal extraCredit = new BigDecimal(0);
		try {
			connection.beginTransaction();
			userManageAccounts = getUser(request, request.getParameter("tfManageAccountsCpfCnpj"));
 			if (userManageAccounts != null) {
				if (request.getParameter("tfManageAccountsExtraCredit") != null && !request.getParameter("tfManageAccountsExtraCredit").equals("")) {
					extraCredit = utilities.converteBigDecimal(request.getParameter("tfManageAccountsExtraCredit"));
					if (extraCredit.compareTo(new BigDecimal(0)) > 0) {
						userManageAccounts.setExtra(userManageAccounts.getExtra().add(extraCredit));
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Crédito extra aplicado!");
					}
					else
						request.setAttribute("message", "Valor deve ser maior do que zero!");
				}
				else 
					request.setAttribute("message", "Algo deu errado!");
				request.setAttribute("listClickValues", (List<ClickValues>) connection.list(ClickValues.QUERY_ALL));
				request.setAttribute("userManageAccounts", userManageAccounts);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			userManageAccounts = null;
		}
		request.setAttribute("tfManageAccountsCpfCnpj", request.getParameter("tfManageAccountsCpfCnpj"));
		return MANAGE_ACCOUNTS;
	}
	
	@SuppressWarnings("unchecked")
	public String manageAccountsApplyCurrentClick(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users userManageAccounts = null;
		try {
			connection.beginTransaction();
			userManageAccounts = getUser(request, request.getParameter("tfManageAccountsCpfCnpj"));
 			if (userManageAccounts != null) {
				if (request.getParameter("selManageAccountsCurrentClick") != null) {
					if (userManageAccounts.getClick_values_id().getId() != Integer.parseInt(request.getParameter("selManageAccountsCurrentClick"))) {
						userManageAccounts.setClick_values_id((ClickValues) connection.find(ClickValues.class, Integer.parseInt(request.getParameter("selManageAccountsCurrentClick"))));
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Valor do clique alterado!");
					}
					else 
						request.setAttribute("message", "O valor do clique continua sendo o mesmo!");
				}
				else 
					request.setAttribute("message", "Algo deu errado!");
				request.setAttribute("listClickValues", (List<ClickValues>) connection.list(ClickValues.QUERY_ALL));
				request.setAttribute("userManageAccounts", userManageAccounts);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			userManageAccounts = null;
		}
		request.setAttribute("tfManageAccountsCpfCnpj", request.getParameter("tfManageAccountsCpfCnpj"));
		return MANAGE_ACCOUNTS;
	}
	
	@SuppressWarnings("unchecked")
	public String manageAccountsApplyImportXml(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users userManageAccounts = null;
		try {
			connection.beginTransaction();
			userManageAccounts = getUser(request, request.getParameter("tfManageAccountsCpfCnpj"));
 			if (userManageAccounts != null) {
				if (request.getParameter("selManageAccountsImportXml") != null) {
					if (request.getParameter("selManageAccountsImportXml").equals("1")) {
						userManageAccounts.setImport_xml(1);
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Importação do XML habilitada!");
					}
					else if (request.getParameter("selManageAccountsImportXml").equals("0")) {
						userManageAccounts.setImport_xml(0);
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Importação do XML desabilitada!");
					}
					else 
						request.setAttribute("message", "Algo deu errado!");
				}
				else 
					request.setAttribute("message", "Algo deu errado!");
				request.setAttribute("listClickValues", (List<ClickValues>) connection.list(ClickValues.QUERY_ALL));
				request.setAttribute("userManageAccounts", userManageAccounts);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			userManageAccounts = null;
		}
		request.setAttribute("tfManageAccountsCpfCnpj", request.getParameter("tfManageAccountsCpfCnpj"));
		return MANAGE_ACCOUNTS;
	}
	
	@SuppressWarnings("unchecked")
	public String manageAccountsApplyTypeXml(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users userManageAccounts = null;
		try {
			connection.beginTransaction();
			userManageAccounts = getUser(request, request.getParameter("tfManageAccountsCpfCnpj"));
 			if (userManageAccounts != null) {
				if (request.getParameter("selManageAccountsTypeXml") != null) {
					if (request.getParameter("selManageAccountsTypeXml").equals("0")) {
						if (userManageAccounts.getImport_xml() == 0) {
							userManageAccounts.setType_xml(0);
							connection.update(userManageAccounts);
							connection.commit();
							request.setAttribute("message", "Tipo do XML alterado!");
						}
						else
							request.setAttribute("message", "Antes, desabilite a importação do XML!");
					}
					else if (request.getParameter("selManageAccountsTypeXml").equals("1")) {
						userManageAccounts.setType_xml(1);
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Tipo do XML alterado!");
					}
					else if (request.getParameter("selManageAccountsTypeXml").equals("2")) {
						userManageAccounts.setType_xml(2);
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Tipo do XML alterado!");
					}
					else if (request.getParameter("selManageAccountsTypeXml").equals("3")) {
						userManageAccounts.setType_xml(3);
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Tipo do XML alterado!");
					}
					else if (request.getParameter("selManageAccountsTypeXml").equals("4")) {
						userManageAccounts.setType_xml(4);
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "Tipo do XML alterado!");
					}
					else 
						request.setAttribute("message", "Algo deu errado!");
				}
				else 
					request.setAttribute("message", "Algo deu errado!");
				request.setAttribute("listClickValues", (List<ClickValues>) connection.list(ClickValues.QUERY_ALL));
				request.setAttribute("userManageAccounts", userManageAccounts);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			userManageAccounts = null;
		}
		request.setAttribute("tfManageAccountsCpfCnpj", request.getParameter("tfManageAccountsCpfCnpj"));
		return MANAGE_ACCOUNTS;
	}
	
	@SuppressWarnings("unchecked")
	public String manageAccountsApplyUrlXml(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users userManageAccounts = null;
		try {
			connection.beginTransaction();
			userManageAccounts = getUser(request, request.getParameter("tfManageAccountsCpfCnpj"));
 			if (userManageAccounts != null) {
				if (request.getParameter("tfManageAccountsUrlXml") != null) {
					if (userManageAccounts.getImport_xml() == 0) {
						userManageAccounts.setUrl_xml(request.getParameter("tfManageAccountsUrlXml"));
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "URL do XML alterada!");
					}
					else if (userManageAccounts.getImport_xml() == 1 && !request.getParameter("tfManageAccountsUrlXml").equals("")) {
						userManageAccounts.setUrl_xml(request.getParameter("tfManageAccountsUrlXml"));
						connection.update(userManageAccounts);
						connection.commit();
						request.setAttribute("message", "URL do XML alterada!");
					}
					else if (userManageAccounts.getImport_xml() == 1 && request.getParameter("tfManageAccountsUrlXml").equals(""))
						request.setAttribute("message", "Antes, desabilite a importação do XML!");
					else
						request.setAttribute("message", "Algo deu errado!");
				}
				else 
					request.setAttribute("message", "URL inválida, o campo URL do XML deve ser preenchido!");
				request.setAttribute("listClickValues", (List<ClickValues>) connection.list(ClickValues.QUERY_ALL));
				request.setAttribute("userManageAccounts", userManageAccounts);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			userManageAccounts = null;
		}
		request.setAttribute("tfManageAccountsCpfCnpj", request.getParameter("tfManageAccountsCpfCnpj"));
		return MANAGE_ACCOUNTS;
	}
	
	public String courtesyForm(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		return COURTESY;
	}

	@SuppressWarnings("unchecked")
	public String courtesySearch(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users userCourtesy = null; 
		Admin admin = null;
		Map<String, Object> param = null;
		List<HistoryCourtesy> listHistoryCourtesy = null;
		BigDecimal sumCourtesy = new BigDecimal(0);
		BigDecimal totalCourtesy = new BigDecimal(0);
		try {
			connection.beginTransaction();
			userCourtesy = getUser(request, request.getParameter("tfCourtesyCpfCnpj"));
 			if (userCourtesy != null) {
 				param = new HashMap<String, Object>();
				param.put("user_id", userCourtesy.getId());
				listHistoryCourtesy = (List<HistoryCourtesy>) connection.list(HistoryCourtesy.QUERY_BY_USER, param);
				param.clear();
				for (HistoryCourtesy hCourtesy : listHistoryCourtesy)
					sumCourtesy = sumCourtesy.add(hCourtesy.getCredit());
				admin = (Admin) connection.find(Admin.class, 1);
				totalCourtesy = (userCourtesy.getExtra().add(admin.getMax_courtesy()).subtract(sumCourtesy));
				request.setAttribute("listHistoryCourtesy", listHistoryCourtesy);
				request.setAttribute("userCourtesy", userCourtesy);
				request.setAttribute("tfCourtesyCredit", totalCourtesy);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			userCourtesy = null;
			admin = null;
			param = null;
			listHistoryCourtesy = null;
		}
		request.setAttribute("tfCourtesyCpfCnpj", request.getParameter("tfCourtesyCpfCnpj"));
		return COURTESY;
	}

	@SuppressWarnings("unchecked")
	public String courtesyApply(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users user = null;
		Users userCourtesy = null; 
		Admin admin = null;
		HistoryCourtesy historyCourtesy = null;
		Map<String, Object> param = null;
		List<HistoryCourtesy> listHistoryCourtesy = null;
		BigDecimal sumCourtesy = new BigDecimal(0);
		BigDecimal totalCourtesy = new BigDecimal(0);
		BigDecimal creditCourtesy = new BigDecimal(0);
		try {
			connection.beginTransaction();
			userCourtesy = getUser(request, request.getParameter("tfCourtesyCpfCnpj"));
 			if (userCourtesy != null) {
 				param = new HashMap<String, Object>();
				param.put("user_id", userCourtesy.getId());
				listHistoryCourtesy = (List<HistoryCourtesy>) connection.list(HistoryCourtesy.QUERY_BY_USER, param);
				param.clear();
				for (HistoryCourtesy hCourtesy : listHistoryCourtesy)
					sumCourtesy = sumCourtesy.add(hCourtesy.getCredit());
				admin = (Admin) connection.find(Admin.class, 1);
				totalCourtesy = (userCourtesy.getExtra().add(admin.getMax_courtesy()).subtract(sumCourtesy));
				if (request.getParameter("tfCourtesyCredit") != null && !request.getParameter("tfCourtesyCredit").equals("")) {
					creditCourtesy = utilities.converteBigDecimal(request.getParameter("tfCourtesyCredit"));
					if (creditCourtesy.compareTo(new BigDecimal(0)) > 0) {
						if (creditCourtesy.compareTo(totalCourtesy) <= 0) {
							user = (Users) request.getSession().getAttribute("currentUser");
							totalCourtesy = totalCourtesy.subtract(creditCourtesy);
							userCourtesy.setBalance(userCourtesy.getBalance().add(creditCourtesy));
							userCourtesy.setVendor_id(user);
							connection.update(userCourtesy);
							historyCourtesy = new HistoryCourtesy();
							historyCourtesy.setVendor_id(user);
							historyCourtesy.setUser_id(userCourtesy);
							historyCourtesy.setCredit(creditCourtesy);
							historyCourtesy.setCreated_at(new Date());
							connection.save(historyCourtesy);
							connection.commit();
							param.put("user_id", userCourtesy.getId());
							listHistoryCourtesy = (List<HistoryCourtesy>) connection.list(HistoryCourtesy.QUERY_BY_USER, param);
							param.clear();
							request.setAttribute("message", "Crédito aplicado!");
						}
						else
							request.setAttribute("message", "Valor maior do que o disponível!");
					}
					else
						request.setAttribute("message", "Valor deve ser maior do que zero!");
				}
				else
					request.setAttribute("message", "Valor inválido!");
				request.setAttribute("listHistoryCourtesy", listHistoryCourtesy);
				request.setAttribute("userCourtesy", userCourtesy);
				request.setAttribute("tfCourtesyCredit", totalCourtesy);
			}
			else
				request.setAttribute("message", "CPF ou CNPJ inválido!");
		} finally {
			connection.closeTransaction();
			utilities = null;
			user = null;
			userCourtesy = null;
			admin = null;
			historyCourtesy = null;
			param = null;
			listHistoryCourtesy = null;
		}
		request.setAttribute("tfCourtesyCpfCnpj", request.getParameter("tfCourtesyCpfCnpj"));
		return COURTESY;
	}
	
	@SuppressWarnings("unchecked")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		Users user = (Users) request.getSession().getAttribute("currentUser");
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		String valueSearch = null;
		Map<String, Object> param = null;
		List<Users> listUsers = null;
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
//				param.put("title", "%" + valueSearch + "%");
				totalResults = connection.count(Users.QUERY_COUNT_BY_VENDOR, param);
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
//			param.put("title", "%" + valueSearch + "%");
			listUsers = (List<Users>) connection.list(Users.QUERY_BY_VENDOR, param, current, maximum);
			param.clear();
			request.setAttribute("valueSearch", valueSearch);
			request.getSession().setAttribute("current", current);
			request.getSession().setAttribute("maximum", maximum);
			request.setAttribute("navActionFirst", request.getSession().getAttribute("linkVendorMyAccountsListFirst"));
			request.setAttribute("navActionPrevious", request.getSession().getAttribute("linkVendorMyAccountsListPrevious"));
			request.setAttribute("navActionNext", request.getSession().getAttribute("linkVendorMyAccountsListNext"));
			request.setAttribute("navActionLast", request.getSession().getAttribute("linkVendorMyAccountsListLast"));
			request.setAttribute("searchAction", request.getSession().getAttribute("linkVendorMyAccountsListSearch"));
			request.setAttribute("list", listUsers);
		} finally {
			connection.closeTransaction();
//			utilities = null;
			valueSearch = null;
			param = null;
			listUsers = null;
			user = null;
			nav = null;
		}
		return LIST;
	}
	
	public String sales(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		return SALES;
	}

	public String manager(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		return MANAGER;
	}

	public String jobs(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		return JOBS;
	}

	public void fullProccess(HttpServletRequest request, HttpServletResponse response) {
		new Jobs(connection, 0, false, (String) request.getSession().getAttribute("pathImages"), (String) request.getSession().getAttribute("pathSitemap")).run();
	}

	public void importXML(HttpServletRequest request, HttpServletResponse response) {
		new ImportXML(connection, 0).run();
	}

	public void replyAdsStores(HttpServletRequest request, HttpServletResponse response) {
		new ReplyAdsInStores(connection, 0).run();
	}

	public void updateSitemap(HttpServletRequest request, HttpServletResponse response) {
		new UpdateSitemap(connection, (String) request.getSession().getAttribute("pathSitemap")).run();
	}

	public void importImages(HttpServletRequest request, HttpServletResponse response) {
		new ImportImages(connection, (String) request.getSession().getAttribute("pathImages"), 0, false).run();
	}

	public void fixesImages(HttpServletRequest request, HttpServletResponse response) {
//		new Thread() {
//			public void run() {
				new FixesImages(connection, (String) request.getSession().getAttribute("pathImages")).run();
//			}
//		}.start();
	}

	public void updateHighLights(HttpServletRequest request, HttpServletResponse response) {
		new UpdateHighLights(connection).run();
	}

	public void updateURLs(HttpServletRequest request, HttpServletResponse response) {
		new AdvertisementsDAO(connection).generateURLs();
	}
	
	public String reports(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		return REPORTS;
	}

	@SuppressWarnings("unchecked")
	public String reportByAdv(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
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
			param = new HashMap<String, Object>();
			valueSearch = request.getParameter("valueSearch");
			if (valueSearch == null)
				valueSearch = "";
			else
				valueSearch = valueSearch.replaceAll("%", "");
			if (request.getParameter("nav") == null) {
				totalResults = connection.count(Advertisements.QUERY_COUNT_REPORT, param);
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
			type_report = request.getParameter("type_report");
			if (type_report == null)
				type_report = (String) request.getSession().getAttribute("type_report");
			if (type_report != null) {
				request.getSession().setAttribute("type_report", type_report);
				if (type_report.equals("gross")) {
					request.setAttribute("subtitleReport", "Total de Cliques");
					listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_GROSS_CLICK, param, current, maximum);
					listCopy = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_GROSS_CLICK, param, current, maximum);
				}
				else if (type_report.equals("count")) {
					request.setAttribute("subtitleReport", "Contabilizados");
					listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_COUNT_CLICK, param, current, maximum);
					listCopy = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_COUNT_CLICK, param, current, maximum);
				}
				else if (type_report.equals("purchase")) {
					request.setAttribute("subtitleReport", "Botão Comprar");
					listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_PURCHASE, param, current, maximum);
					listCopy = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_PURCHASE, param, current, maximum);
				}
			}
			else {
				request.setAttribute("subtitleReport", "Contabilizados");
				listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_COUNT_CLICK, param, current, maximum);
				listCopy = (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_BY_COUNT_CLICK, param, current, maximum);
			}
			Collections.reverse(listAdvertisements);			
			param.clear();
			request.setAttribute("valueSearch", valueSearch);
			request.getSession().setAttribute("current", current);
			request.getSession().setAttribute("maximum", maximum);
			request.setAttribute("navActionFirst", request.getSession().getAttribute("linkAdminReportByAdvFirst"));
			request.setAttribute("navActionPrevious", request.getSession().getAttribute("linkAdminReportByAdvPrevious"));
			request.setAttribute("navActionNext", request.getSession().getAttribute("linkAdminReportByAdvNext"));
			request.setAttribute("navActionLast", request.getSession().getAttribute("linkAdminReportByAdvLast"));
			request.setAttribute("searchAction", request.getSession().getAttribute("linkAdminReportByAdvSearch"));
			request.setAttribute("list", listAdvertisements);
			request.setAttribute("listCopy", listCopy);
		} finally {
			connection.closeTransaction();
//			utilities = null;
			valueSearch = null;
			param = null;
			listAdvertisements = null;
			listCopy = null;
			nav = null;
			type_report = null; 
		}
		return REPORT_BY_ADV;
	}
	
	@SuppressWarnings("unchecked")
	public String reportByDate(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
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
			param = new HashMap<String, Object>();
			valueSearch = request.getParameter("valueSearch");
			if (valueSearch == null)
				valueSearch = "";
			else
				valueSearch = valueSearch.replaceAll("%", "");
			if (request.getParameter("nav") == null) {
				param.put("initialDate", initialDate);
				param.put("currentDate",finalDate);
				report_type = request.getParameter("selReportType");
				if (report_type == null)
					report_type = (String) request.getSession().getAttribute("report_type");
				if (report_type != null) {
					if (report_type.equals("CC")) {
						request.getSession().setAttribute("report_type", "CC");
						totalResults = connection.count(HistoryCountClick.QUERY_COUNT_HISTORY_CLICK_BY_DATE, param);
					}
					else if (report_type.equals("GC")) {
						request.getSession().setAttribute("report_type", "GC");
						totalResults = connection.count(HistoryGrossClick.QUERY_COUNT_HISTORY_GROSS_CLICK_BY_DATE, param);
					}
					else if (report_type.equals("PU")) {
						request.getSession().setAttribute("report_type", "PU");
						totalResults = connection.count(HistoryPurchases.QUERY_COUNT_HISTORY_PURCHASES_BY_DATE, param);
					}
					else if (report_type.equals("MA")) {
						request.getSession().setAttribute("report_type", "MA");
						totalResults = connection.count(HistoryMostAccessed.QUERY_COUNT_HISTORY_MOST_ACCESSED_BY_DATE, param);
					}
					else if (report_type.equals("RA")) {
						request.getSession().setAttribute("report_type", "RA");
						totalResults = connection.count(HistoryRecentlyAdded.QUERY_COUNT_HISTORY_RECENTLY_ADDED_BY_DATE, param);
					}
				}
				else {
					request.getSession().setAttribute("report_type", "CC");
					totalResults = connection.count(HistoryCountClick.QUERY_COUNT_HISTORY_CLICK_BY_DATE, param);
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
			param.put("initialDate", initialDate);
			param.put("currentDate", finalDate);
			report_type = request.getParameter("selReportType");
			if (report_type == null)
				report_type = (String) request.getSession().getAttribute("report_type");
			if (report_type != null) {
				if (report_type.equals("CC")) {
					request.getSession().setAttribute("report_type", "CC");
					listHistoryCountClick = (List<HistoryCountClick>) connection.list(HistoryCountClick.QUERY_HISTORY_COUNT_CLICK_BY_DATE, param, current, maximum);
					request.setAttribute("list", listHistoryCountClick);
				}
				else if (report_type.equals("GC")) {
					request.getSession().setAttribute("report_type", "GC");
					listHistoryGrossClick = (List<HistoryGrossClick>) connection.list(HistoryGrossClick.QUERY_HISTORY_GROSS_CLICK_BY_DATE, param, current, maximum);
					request.setAttribute("list", listHistoryGrossClick);
				}
				else if (report_type.equals("PU")) {
					request.getSession().setAttribute("report_type", "PU");
					listHistoryPurchases = (List<HistoryPurchases>) connection.list(HistoryPurchases.QUERY_HISTORY_PURCHASES_BY_DATE, param, current, maximum);
					request.setAttribute("list", listHistoryPurchases);
				}
				else if (report_type.equals("MA")) {
					request.getSession().setAttribute("report_type", "MA");
					listHistoryMostAccessed = (List<HistoryMostAccessed>) connection.list(HistoryMostAccessed.QUERY_HISTORY_MOST_ACCESSED_BY_DATE, param, current, maximum);
					
					for(HistoryMostAccessed hma : listHistoryMostAccessed) {
						if (hma.getAddresses_id() != null) {
							System.out.println("citie: " + hma.getAddresses_id().getCitie());
							System.out.println("state: " + hma.getAddresses_id().getState());
							System.out.println("country: " + hma.getAddresses_id().getCountry());
						}
						else
							System.out.println(hma.getId() + " null...");
					}
					
					request.setAttribute("list", listHistoryMostAccessed);
				}
				else if (report_type.equals("RA")) {
					request.getSession().setAttribute("report_type", "RA");
					listHistoryRecentlyAdded = (List<HistoryRecentlyAdded>) connection.list(HistoryRecentlyAdded.QUERY_HISTORY_RECENTLY_ADDED_BY_DATE, param, current, maximum);
					request.setAttribute("list", listHistoryRecentlyAdded);
				}
			}
			else {
				request.getSession().setAttribute("report_type", "CC");
				listHistoryCountClick = (List<HistoryCountClick>) connection.list(HistoryCountClick.QUERY_HISTORY_COUNT_CLICK_BY_DATE, param, current, maximum);
				request.setAttribute("list", listHistoryCountClick);
			}
			param.clear();
			request.setAttribute("valueSearch", valueSearch);
			request.getSession().setAttribute("current", current);
			request.getSession().setAttribute("maximum", maximum);
			request.setAttribute("navActionFirst", request.getSession().getAttribute("linkAdminReportByDateFirst"));
			request.setAttribute("navActionPrevious", request.getSession().getAttribute("linkAdminReportByDatePrevious"));
			request.setAttribute("navActionNext", request.getSession().getAttribute("linkAdminReportByDateNext"));
			request.setAttribute("navActionLast", request.getSession().getAttribute("linkAdminReportByDateLast"));
			request.setAttribute("searchAction", request.getSession().getAttribute("linkAdminReportByDateSearch"));
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
			nav = null;
		}
		return REPORT_BY_DATE;
	}
	
	private Users getUser(HttpServletRequest request, String cpfCnpj) {
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users user = null; 
		Map<String, Object> param = null;
		Validations validations = null;
		try {
			validations = new Validations();
			if (validations.isCnpj(cpfCnpj)) {
				param = new HashMap<String, Object>();
				param.put("cnpj", cpfCnpj);
				user = (Users) connection.find(Users.QUERY_BY_CNPJ, param);
				param.clear();
			}
			else if (validations.isCpf(cpfCnpj)) {
				param = new HashMap<String, Object>();
				param.put("cpf", cpfCnpj);
				user = (Users) connection.find(Users.QUERY_BY_CPF, param);
				param.clear();
			}
		} finally {
			utilities = null;
			param = null;
			validations = null;
		}
		return user;
	}
	
//	private void redirect(String page, boolean panel, HttpServletRequest request, HttpServletResponse response) {
//		try {
//			new Redirect().toPage(page, panel, request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
