 package dao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;

import connection.Connection;
import entity.Advertisements;
import entity.ClickValues;
import entity.NewsletterLists;
import entity.Orders;
import entity.States;
import entity.Stores;
import entity.Users;
import interfaces.IDao;
import mail.MailActivation;
import mail.MailAlert;
import mail.MailResetPassword;
import spinwork.Manager;
import spinwork.util.Validations;
import utils.Encode;
import utils.Utilities;

public final class UsersDAO implements IDao {
	
	private static final String FORM = "users/form.jsp";
	private static final String LIST = "users/list.jsp";
//	private static final String SUCCESS = "users/success.jsp";
	private static final String PURCHASES = "users/purchases.jsp";
	private static final String SEND_INSTRUCTIONS_RESET_PASSWORD = "users/sendInstructionsResetPassword.jsp";
	private static final String RESET_PASSWORD = "users/resetPassword.jsp";
	private static final String UPDATE_PASSWORD = "users/updatePassword.jsp";
	private static final String ACTIVATION = "users/activation.jsp";
	private static final String TERMS = "users/terms.jsp";
	private Connection connection = null;
	
	public UsersDAO(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public String form(HttpServletRequest request, HttpServletResponse response) {
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		Users user = null;
		try {
			connection.beginTransaction();
			if (request.getParameter("typePerson") == null || request.getParameter("typePerson").equals("pf"))
				request.setAttribute("typePerson", "pf");
			else
				request.setAttribute("typePerson", "pj");
			request.setAttribute("listStates", connection.list(States.QUERY_LIST_ALL_WITH_ORDINATION));
			if ((request.getSession().getAttribute("currentUser") != null && request.getParameter("newAccount") == null) || (request.getSession().getAttribute("currentUser") != null && request.getParameter("newAccount").equals("false"))) {
				user = (Users) request.getSession().getAttribute("currentUser");
				if (user.getType_person().equals("F"))
					request.setAttribute("typePerson", "pf");
				else
					request.setAttribute("typePerson", "pj");
				if (request.getSession().getAttribute("user") == null)
					request.getSession().setAttribute("user", user);
				request.setAttribute("email_receive", user.getEmail_receive());
				request.setAttribute("terms_agree", user.getTerms_agree());
/*
				request.setAttribute("stateId", user.getCity_id().getState_id().getId());
				request.setAttribute("cityId", user.getCity_id().getId());
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("state_id", user.getCity_id().getState_id().getId());
				request.setAttribute("listCities", connection.list(Cities.QUERY_LIST_All_FILTERED_BY_STATE, param));
				param.clear();
*/			
			}
			else if (request.getSession().getAttribute("currentUser") != null && request.getParameter("newAccount") != null) {
				request.getSession().setAttribute("newAccount", true);
				request.getSession().setAttribute("user", null);
			}
			else
				request.getSession().setAttribute("user", null);
		} finally {
			connection.closeTransaction();
			user = null;
		}
		return FORM;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String save(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		utilities.printNameValue(request.getParameterMap(), isPrint);
		boolean isNew = false;
		Users user = null;
		NewsletterLists newsletterLists = null;
		ArrayList<String> messages = null;
		String url = request.getParameter("user.url");
		String cpf = request.getParameter("user.cpf");
		String cnpj = request.getParameter("user.cnpj");
		Map<String, Object> param = new HashMap<String, Object>();
		Validations validations = new Validations();
		Date dataAtual = new Date();
		Encode encode = new Encode();
		String password = request.getParameter("password");
		String retype_password = request.getParameter("retype_password");
		MailActivation mailActivation = null;
		MailAlert mailAlert = null;
		Thread threadMailActivation = null;
		Thread threadMailAlert = null;
		
		try {
			connection.beginTransaction();
			if (check(request))
				user = (Users) connection.find(Users.class, Integer.parseInt(request.getParameter("user.id")));
			else {
				user = new Users();
				isNew = true;
			}
			if (isNew || user.getType_account() != 11) {
				user = (Users) Manager.setObject(user, request, connection);
				messages = (ArrayList<String>) request.getAttribute("messages");
			}
			else if (user.getType_account() == 11) {
				messages = new ArrayList<>();
				url = request.getParameter("user.url");
				cpf = request.getParameter("user.cpf");
				cnpj = request.getParameter("user.cnpj");
				user.setName(request.getParameter("user.name"));
				user.setLast_name(request.getParameter("user.last_name"));
				user.setBirthdate(utilities.converteDate(request.getParameter("user.birthdate")));
				user.setGender(request.getParameter("user.gender"));
				user.setLink(request.getParameter("user.link"));
				user.setPhone(request.getParameter("user.phone"));
				user.setSecond_phone(request.getParameter("user.second_phone"));
				param = new HashMap<String, Object>();
				validations = new Validations();
				request.setAttribute("isValid", true);
				if (url != null && !url.equals("")) {
					param.put("url", url);
					if (connection.find(Users.QUERY_BY_URL, param) != null) {
						messages.add("user.urlDuplicate");
						request.setAttribute("isValid", false);					
					}
					param.clear();
					if (!validations.isUrl(url)) {
						messages.add("user.urlInvalid");
						request.setAttribute("isValid", false);					
					}
					user.setUrl(url);
				}
				else {
					messages.add("user.fillFieldUrl");
					request.setAttribute("isValid", false);
				}
				if (request.getParameter("typePerson").equals("pf")) {
					if (cpf != null && !cpf.equals("")) {
						param.put("cpf", cpf);
						if (connection.find(Users.QUERY_BY_CPF, param) != null) {
							messages.add("user.cpfDuplicate");
							request.setAttribute("isValid", false);
						}
						param.clear();
						if (!validations.isCpf(cpf)) {
							messages.add("user.cpfInvalid");
							request.setAttribute("isValid", false);
						}
						user.setCpf(cpf);
					}
					else {
						messages.add("user.fillFieldCpf");
						request.setAttribute("isValid", false);
					}
				} else {
					if (cnpj != null && !cnpj.equals("")) {
						param.put("cnpj", cnpj);
						if (connection.find(Users.QUERY_BY_CNPJ, param) != null) {
							messages.add("user.cnpjDuplicate");
							request.setAttribute("isValid", false);
						}
						param.clear();
						if (!validations.isCpf(cnpj)) {
							messages.add("user.cnpjInvalid");
							request.setAttribute("isValid", false);
						}
						user.setCnpj(cnpj);
					}
					else {
						messages.add("user.fillFieldCnpj");
						request.setAttribute("isValid", false);
					}
				}
			}
			else {
				messages = (ArrayList<String>) request.getAttribute("messages");
			}
			request.setAttribute("typePerson", request.getParameter("typePerson"));		
/*
			int stateId = Integer.parseInt(request.getParameter("ddUserState"));
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("state_id", stateId);
			request.setAttribute("listCities", connection.list(Cities.QUERY_LIST_All_FILTERED_BY_STATE, param));
			request.setAttribute("stateId", stateId);
			request.setAttribute("cityId", Integer.parseInt(request.getParameter("user.city_id")));
			param.clear();
*/
			if (request.getParameter("email_receive") == null)
				user.setEmail_receive(0);
			else
				user.setEmail_receive(1);
			request.setAttribute("email_receive", user.getEmail_receive());
			request.setAttribute("terms_agree", user.getTerms_agree());
			dataAtual = new Date();
			if (isNew || user.getType_account() == 11) {
				encode = new Encode();
				password = request.getParameter("password");
				retype_password = request.getParameter("retype_password");
				request.setAttribute("password", password);
				request.setAttribute("retype_password", retype_password);
				// PASSWORD
				if (password != null && !password.equals("") && !password.equals("0")) {
					if (!utilities.isPassword(password)) {
						messages.add("user.passwordInvalid");
						request.setAttribute("isValid", false);
					}
				}
				else {
					messages.add("user.fillFieldPassword");
					request.setAttribute("isValid", false);
				}
				// RETYPE_PASSWORD
				if (password.compareTo(retype_password) != 0) {
					messages.add("user.differentPasswords");
					request.setAttribute("isValid", false);
				}
				user.setEncrypted_password(encode.encryptsPassword(password));
				request.setAttribute("messages", messages);
				if (!user.getCpf().equals("")) {
					user.setType_person("F");
					user.setReset_password_token(encode.codificaBase64Encoder(utilities.onlyNumbers(user.getCpf())));
				}
				else {
					user.setType_person("J");
					user.setReset_password_token(encode.codificaBase64Encoder(utilities.onlyNumbers(user.getCnpj())));
				}
				encode = null;
			}
			if (isNew) {
				user.setLink("");
				user.setPhone("");
				user.setState_registration("");
				user.setSecond_phone("");
				user.setGender("0");
				user.setType_account(10);
				user.setPartner(0);
				user.setUrl_xml("");
				user.setType_xml(0);
				user.setImport_xml(0);
				user.setGross_click(0);
				user.setClick_values_id((ClickValues) connection.find(ClickValues.class, 1));
				user.setExtra(new BigDecimal("0.00"));
				user.setBalance(new BigDecimal("0.00"));
				user.setImage_file_name("");
				user.setImage_file_type("");
				if (request.getSession().getAttribute("newAccount") == null) {
					user.setVendor_id(null);
					user.setStatus(0);
				}
				else {
					user.setVendor_id((Users) request.getSession().getAttribute("currentUser"));
					user.setStatus(1);
				}
				user.setCount_stores(0);
				user.setCount_advertisements(0);
				user.setReset_password_sent_at(null);
				user.setRemenber_created_at(null);
				user.setSign_in_count(0);
				user.setCurrent_sign_in_at(dataAtual);
				user.setLast_sign_in_at(dataAtual);
				String ip = request.getHeader("X-Forwarded-For");
				if (ip == null)
					ip = InetAddress.getLoopbackAddress().getHostAddress();
				user.setCurrent_sign_in_ip(ip);
				user.setLast_sign_in_ip(ip);
				user.setCreated_at(dataAtual);
				if (request.getParameter("terms_agree") == null) {
					user.setTerms_agree(0);
					messages.add("user.fillFieldTermsAgree");
					request.setAttribute("isValid", false);
				}
				else
					user.setTerms_agree(1);
			}
			user.setUpdated_at(dataAtual);
			if ((boolean) request.getAttribute("isValid")) {
				if (!user.getLink().startsWith("http://") || !user.getLink().startsWith("https://"))
					user.setLink("http://" + user.getLink());
				if (isNew) {
					connection.save(user);
					if (request.getSession().getAttribute("newAccount") == null)
						messages.add("user.success");
					else
						messages.add("user.successNewAccount");
					request.setAttribute("messages", messages);
					request.setAttribute("typeMessage", "list-group-item-success");
					param = new HashMap<String, Object>();
					param.put("email", user.getEmail());
					newsletterLists = (NewsletterLists) connection.find(NewsletterLists.QUERY_FIND_BY_EMAIL, param);
					param.clear();
					if (newsletterLists == null) {
						newsletterLists = new NewsletterLists();
						if (user.getEmail_receive() == 1)
							newsletterLists.setStatus(1);
						else
							newsletterLists.setStatus(0);
						if (user.getType_person().equals("F"))
							newsletterLists.setName(user.getName());
						else
							newsletterLists.setName(user.getCompany_name());
						newsletterLists.setEmail(user.getEmail());
						newsletterLists.setCreated_at(dataAtual);
						newsletterLists.setUpdated_at(dataAtual);
						connection.save(newsletterLists);
					}
					else {
						if (user.getEmail_receive() == 1)
							newsletterLists.setStatus(1);
						else
							newsletterLists.setStatus(0);
						if (user.getType_person().equals("F"))
							newsletterLists.setName(user.getName());
						else
							newsletterLists.setName(user.getCompany_name());
						newsletterLists.setUpdated_at(dataAtual);
						connection.update(newsletterLists);
					}
					if (user.getType_person().equals("F")) {
						if (request.getSession().getAttribute("newAccount") == null) {
							mailActivation = new MailActivation(
									(String) request.getSession().getAttribute("giroUrl"),
									user.getName(), 
									user.getEmail(), 
									user.getReset_password_token(),
									(String) request.getSession().getAttribute("giroUrlServerImages")
								);
							threadMailActivation = new Thread(mailActivation);
							threadMailActivation.start();
						}
						mailAlert = new MailAlert(
								"Giroferta - Novo usuário", 
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
					else {
						if (request.getSession().getAttribute("newAccount") == null) {
							mailActivation = new MailActivation(
									(String) request.getSession().getAttribute("giroUrl"),
									user.getCompany_name(), 
									user.getEmail(), 
									user.getReset_password_token(),
									(String) request.getSession().getAttribute("giroUrlServerImages")
								);
							threadMailActivation = new Thread(mailActivation);
							threadMailActivation.start();
						}
						mailAlert = new MailAlert(
								"Giroferta - Novo usuário", 
								"\n\nID: " + user.getId() + 
								"\nCliente: " + user.getCompany_name() + 
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
				}
				else {
					if (user.getType_account() == 11)
						user.setType_account(10);
					connection.update(user);
					messages.add("user.updated");
					request.setAttribute("messages", messages);
					request.setAttribute("typeMessage", "list-group-item-success");
					param = new HashMap<String, Object>();
					param.put("email", user.getEmail());
					newsletterLists = (NewsletterLists) connection.find(NewsletterLists.QUERY_FIND_BY_EMAIL, param);
					param.clear();
					if (newsletterLists != null) {
						if (user.getEmail_receive() == 1)
							newsletterLists.setStatus(1);
						else
							newsletterLists.setStatus(0);
						if (user.getType_person().equals("F"))
							newsletterLists.setName(user.getName());
						else
							newsletterLists.setName(user.getCompany_name());
						newsletterLists.setUpdated_at(dataAtual);
						connection.update(newsletterLists);
					}
				}
				connection.commit();
				request.getSession().setAttribute("currentUser", user);
				request.getSession().setAttribute("user", user);
				if (request.getSession().getAttribute("newAccount") != null) {				
					request.getSession().setAttribute("newAccount", null);
					request.getSession().removeAttribute("newAccount");
				}
			}
			else {
				request.getSession().setAttribute("user", user);
				request.setAttribute("typeMessage", "list-group-item-danger");
			}
		} finally {
			connection.closeTransaction();
			utilities = null;
			user = null;
			newsletterLists = null;
			messages = null;
			url = null;
			cpf = null;
			cnpj = null;
			param = null;
			validations = null;
			dataAtual = null;
			encode = null;
			password = null;
			retype_password = null;
			mailActivation = null;
			mailAlert = null;
			threadMailActivation = null;
			threadMailAlert = null;
		}
		return form(request, response);
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		return LIST;
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response) {
		return LIST;
	}

	@Override
	public boolean check(HttpServletRequest request) {
		boolean isCheck;
		if (request.getParameter("user.id") != null && !request.getParameter("user.id").equals("") && !request.getParameter("user.id").equals("0"))
			isCheck = true;
		else 
			isCheck = false;
		return isCheck;
	}

	public String activation(HttpServletRequest request, HttpServletResponse response) {
		String token = null;
		Map<String, Object> param = null;
		Users user = null;
		try {
			connection.beginTransaction();
			token = request.getParameter("token");
			if (token != null && !token.equals("")) {
				param = new HashMap<String, Object>();
				param.put("reset_password_token", token);
				user = (Users) connection.find(Users.QUERY_ACTIVATION, param);
				param.clear();
				if (user != null) {
					user.setStatus(1);
					user.setUpdated_at(new Date());
					connection.update(user);
					connection.commit();
					if (user.getType_person().equals("F"))
						request.setAttribute("userActivation", user.getName());
					else
						request.setAttribute("userActivation", user.getCompany_name());
					
					request.getSession().setAttribute("currentUser", user);
					
					request.setAttribute("emailActivation", user.getEmail());
					request.setAttribute("successAccountActivation", true);
				}
				else
					request.setAttribute("successAccountActivation", false);
			}
			else
				request.setAttribute("successAccountActivation", false);
		} finally {
			connection.closeTransaction();
			token = null;
			param = null;
			user = null;
		}
		return ACTIVATION;
	}
	
	public String login(HttpServletRequest request, HttpServletResponse response) {
//		boolean isPrint = true;
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), isPrint);
		if (request.getParameter("currentAdv") != null && request.getParameter("currentSto") != null) {
			request.getSession().setAttribute("currentAdv", request.getParameter("currentAdv"));
			request.getSession().setAttribute("currentSto", request.getParameter("currentSto"));
		}
		else {
			request.getSession().removeAttribute("currentAdv");
			request.getSession().removeAttribute("currentSto");
		}
		return (String) request.getSession().getAttribute("LOGIN");
	}
	
	@SuppressWarnings("unchecked")
	public String checkLogin(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("emailLogin") == null || request.getParameter("passwordLogin") == null)
 			return (String) request.getSession().getAttribute("LOGIN");
//		boolean isPrint = true;
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), isPrint);
		String email = null;
		Encode encode = null;
		Map<String, Object> param = null;
		Users user = null;
//		AdvertisementStores advertisementStores = null;
		Advertisements advertisement = null;
		List<Stores> listStores = null;
		Date dataAtual = null;
		String ip = null;
		try {
			connection.beginTransaction();
//			String dataHora = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date());
//			System.out.println("1: " + dataHora + " - " + request.getRemoteAddr());
//			System.out.println("2: " + dataHora + " - " + request.getHeader("X-Forwarded-For"));
//			System.out.println("3: " + dataHora + " - " + InetAddress.getLoopbackAddress().getHostAddress());
			email = request.getParameter("emailLogin");
			encode = new Encode();
			param = new HashMap<String, Object>();
	        param.put("email", email);
	        param.put("encrypted_password", encode.encryptsPassword(request.getParameter("passwordLogin")));
			user = (Users) connection.find(Users.QUERY_BY_LOGIN, param);
			param.clear();
			if (user != null) {
				dataAtual = new Date();
				user.setSign_in_count(user.getSign_in_count()+1);
				user.setLast_sign_in_at(user.getCurrent_sign_in_at());
				user.setCurrent_sign_in_at(dataAtual);
				user.setLast_sign_in_ip(user.getCurrent_sign_in_ip());
				ip = request.getHeader("X-Forwarded-For");
				if (ip == null)
					ip = InetAddress.getLoopbackAddress().getHostAddress();
				user.setCurrent_sign_in_ip(ip);			
				connection.update(user);
				connection.commit();
				request.getSession().setAttribute("currentUser", user);
				request.getSession().setAttribute("user", user);
				if (request.getSession().getAttribute("currentAdv") != null && request.getSession().getAttribute("currentSto") != null) {
					param= new HashMap<String, Object>();
					
//					param.put("advertisement_id", Integer.parseInt((String) request.getSession().getAttribute("currentAdv")));
//					param.put("store_id", Integer.parseInt((String) request.getSession().getAttribute("currentSto")));
//					advertisementStores = (AdvertisementStores) connection.find(AdvertisementStores.QUERY_BY_ADVERTISEMENTSTORES, param);
//					request.setAttribute("currentAdvertisementStore", advertisementStores);
//					param.clear();
					
					advertisement = (Advertisements) connection.find(Advertisements.class, Integer.parseInt((String) request.getSession().getAttribute("currentAdv")));
					param.put("user_id", advertisement.getUser_id().getId());
					listStores = (List<Stores>) connection.list(Stores.QUERY_BY_USER, param);
					param.clear();
					
					request.setAttribute("currentAdvertisement", advertisement);
					request.setAttribute("currentStore", listStores.get(0));
					
					request.getSession().removeAttribute("currentAdv");
					request.getSession().removeAttribute("currentSto");
					connection.closeTransaction();
					
//					response.sendRedirect((String) request.getSession().getAttribute("giroUrl") + advertisementStores.getUrl());
					
					response.sendRedirect((String) request.getSession().getAttribute("giroUrl") + advertisement.getUrl());
				}
				else {
					connection.closeTransaction();
					response.sendRedirect((String) request.getSession().getAttribute("giroUrl"));
				}
			}
			else {
				request.setAttribute("loginValidationMessage", "Usuário ou senha incorretos!");
				request.getSession().setAttribute("currentUser", null);
				connection.closeTransaction();
				response.sendRedirect((String) request.getSession().getAttribute("giroUrl") + "login");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.closeTransaction();
//			utilities = null;
			email = null;
			encode = null;
			param = null;
			user = null;
//			advertisementStores = null;
			advertisement = null;
			listStores = null;
			dataAtual = null;
			ip = null;
		}
		return (String) request.getSession().getAttribute("HOME");
    }
	
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute("newAccount", null);
		request.getSession().setAttribute("currentUser", null);
		request.getSession().setAttribute("user", null);
		request.getSession().removeAttribute("newAccount");
		request.getSession().removeAttribute("currentUser");
		request.getSession().removeAttribute("user");
		/*se usar a oppção abaixo terá que recarregar os atributos no Config.java novamente!*/
//		request.getSession().invalidate(); 
		return (String) request.getSession().getAttribute("HOME");
    }
	
	public void loginSocial(HttpServletRequest request, HttpServletResponse response) {
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		Users user = null;
		String gson = null;
		String socialEmail = null;
		String socialId = null;
		String socialFirst_name = null;
		String socialLast_name = null;
		String socialNetwork = null;
		Map<String, Object> param = null;
		Date dataAtual = null;
		String ip = null;
		Users userNL = null;
		NewsletterLists newsletterLists = null;
		try {
			connection.beginTransaction();
			socialEmail = request.getParameter("socialEmail");
			socialId = request.getParameter("socialId");
			socialFirst_name = request.getParameter("socialFirst_name");
			socialLast_name = request.getParameter("socialLast_name");
			socialNetwork = request.getParameter("socialNetwork");
			param = new HashMap<String, Object>();
			param.put("email", socialEmail);
			if (socialNetwork.equals("facebook")) {
				param.put("id_facebook", socialId);
				user = (Users) connection.find(Users.QUERY_BY_IDFACEBOOK, param);
			}
			else if (socialNetwork.equals("google")) {
				param.put("id_google", socialId);
				user = (Users) connection.find(Users.QUERY_BY_IDGOOGLE, param);
			}
			param.clear();
			if (user == null) {
				param.put("email", socialEmail);
				user = (Users) connection.find(Users.QUERY_BY_EMAIL, param);
				param.clear();
				if (user != null) {
					dataAtual = new Date();
					if (socialNetwork.equals("facebook"))
						user.setId_facebook(socialId);
					else if (socialNetwork.equals("google"))
						user.setId_google(socialId);
					user.setSign_in_count(user.getSign_in_count()+1);
					user.setLast_sign_in_at(user.getCurrent_sign_in_at());
					user.setCurrent_sign_in_at(dataAtual);
					user.setLast_sign_in_ip(user.getCurrent_sign_in_ip());
					ip = request.getHeader("X-Forwarded-For");
					if (ip == null)
						ip = InetAddress.getLoopbackAddress().getHostAddress();
					user.setCurrent_sign_in_ip(ip);			
					connection.update(user);
//					connection.commit();
					request.getSession().setAttribute("currentUser", user);
					gson = new Gson().toJson(true);
				}
				else {
					dataAtual = new Date();
					user = new Users();
					user.setEmail(socialEmail);
					if (socialNetwork.equals("facebook"))
						user.setId_facebook(socialId);
					else if (socialNetwork.equals("google"))
						user.setId_google(socialId);
					user.setName(socialFirst_name);
					user.setLast_name(socialLast_name);
					user.setGender("0");
//					user.setBirthdate(socialBirthday);
					user.setEncrypted_password("");
					user.setUrl("");
					user.setCompany_name("");
					user.setContact_person("");
					user.setEmail_receive(1);
					user.setTerms_agree(1);
					user.setCpf("");
					user.setCnpj("");
					user.setType_person("F");
					user.setState_registration("");
					user.setLink("");
					user.setPhone("");
					user.setSecond_phone("");
//					user.setGender("0");
					user.setType_account(11);
					user.setPartner(0);
					user.setGross_click(0);
					user.setClick_values_id((ClickValues) connection.find(ClickValues.class, 1));
					user.setExtra(new BigDecimal("0.00"));
					user.setBalance(new BigDecimal("0.00"));
					user.setImage_file_name("");
					user.setImage_file_type("");
					user.setVendor_id(null);
					user.setStatus(1);
					user.setCount_stores(0);
					user.setCount_advertisements(0);
					user.setReset_password_token("");
					user.setReset_password_sent_at(null);
					user.setRemenber_created_at(null);
					user.setSign_in_count(1);
					user.setCurrent_sign_in_at(dataAtual);
					user.setLast_sign_in_at(dataAtual);
					ip = request.getHeader("X-Forwarded-For");
					if (ip == null)
						ip = InetAddress.getLoopbackAddress().getHostAddress();
					user.setCurrent_sign_in_ip(ip);
					user.setLast_sign_in_ip(ip);
					user.setCreated_at(dataAtual);
					user.setUpdated_at(dataAtual);
					connection.save(user);
//					connection.commit();
					request.getSession().setAttribute("currentUser", user);
					gson = new Gson().toJson(true);
				}
			}
			else {
				dataAtual = new Date();
				user.setSign_in_count(user.getSign_in_count()+1);
				user.setLast_sign_in_at(user.getCurrent_sign_in_at());
				user.setCurrent_sign_in_at(dataAtual);
				user.setLast_sign_in_ip(user.getCurrent_sign_in_ip());
				ip = request.getHeader("X-Forwarded-For");
				if (ip == null)
					ip = InetAddress.getLoopbackAddress().getHostAddress();
				user.setCurrent_sign_in_ip(ip);			
				connection.update(user);
//				connection.commit();
				request.getSession().setAttribute("currentUser", user);
				gson = new Gson().toJson(true);
			}
			param.put("email", socialEmail);
			newsletterLists = (NewsletterLists) connection.find(NewsletterLists.QUERY_FIND_BY_EMAIL, param);
			param.clear();
			if (newsletterLists != null && newsletterLists.getStatus() == 0) {
				newsletterLists.setStatus(1);
				newsletterLists.setUpdated_at(dataAtual);
				connection.update(newsletterLists);
			}
			else {
				newsletterLists = new NewsletterLists();
				newsletterLists.setName(socialFirst_name + " " + socialLast_name);
				newsletterLists.setEmail(socialEmail);
				newsletterLists.setStatus(1);
				newsletterLists.setCreated_at(dataAtual);
				newsletterLists.setUpdated_at(dataAtual);
				connection.save(newsletterLists);
			}
			param.put("email", socialEmail);
			userNL = (Users) connection.find(Users.QUERY_BY_EMAIL, param);
			param.clear();
			if (userNL != null && userNL.getEmail_receive() == 0) {
				userNL.setEmail_receive(1);
				userNL.setUpdated_at(dataAtual);
				connection.update(userNL);
			}
			connection.commit();
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gson);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			connection.closeTransaction();
//			utilities = null;
			user = null;
			gson = null;
			socialEmail = null;
			socialId = null;
			socialFirst_name = null;
			socialLast_name = null;
			socialNetwork = null;
			param = null;
			dataAtual = new Date();
			ip = null;
			newsletterLists = null;
			userNL = null;
		}
	}

	public String formSendInstructionsResetPassword(HttpServletRequest request, HttpServletResponse response) {
		return SEND_INSTRUCTIONS_RESET_PASSWORD;
	}

	public String sendInstructionsResetPassword(HttpServletRequest request, HttpServletResponse response) {
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		String email = request.getParameter("email"); 
		if (email == null || email.equals("")) {
			ArrayList<String> messages = new ArrayList<String>();
			messages.add("sendInstructionsResetPassword.fillFieldEmail");
			request.setAttribute("email", email);
			request.setAttribute("messages", messages);
			email = null; 
			messages = null;
			return SEND_INSTRUCTIONS_RESET_PASSWORD;
		}
		Map<String, Object> param = null;
		Users user = null;
		MailResetPassword mailResetPassword = null;
		Thread threadMailActivation = null;
		ArrayList<String> messages = null;
		request.setAttribute("resetPassword.email", email);
		try {
			connection.beginTransaction();			
			param = new HashMap<String, Object>();
			param.put("email", email);
			user = (Users) connection.find(Users.QUERY_BY_EMAIL, param);
			param.clear();
			if (user != null) {
				if (user.getType_person().equals("F")) {
					mailResetPassword = new MailResetPassword(
							(String) request.getSession().getAttribute("giroUrl"),
							user.getName(), 
							user.getEmail(), 
							user.getReset_password_token(),
							(String) request.getSession().getAttribute("giroUrlServerImages")
						);
					threadMailActivation = new Thread(mailResetPassword);
					threadMailActivation.start();
				}
				else {
					mailResetPassword = new MailResetPassword(
							(String) request.getSession().getAttribute("giroUrl"),
							user.getCompany_name(), 
							user.getEmail(), 
							user.getReset_password_token(),
							(String) request.getSession().getAttribute("giroUrlServerImages")
						);
					threadMailActivation = new Thread(mailResetPassword);
					threadMailActivation.start();
				}
				request.setAttribute("successSendInstructionsResetPassword", true);
			}
			else {
				messages = new ArrayList<String>();
				messages.add("sendInstructionsResetPassword.invalidEmail");
				request.setAttribute("email", email);
				request.setAttribute("messages", messages);
			}
		} finally {
			connection.closeTransaction();
			email = null;
			param = null;
			user = null;
			mailResetPassword = null;
			threadMailActivation = null;
			messages = null;
		}
		return SEND_INSTRUCTIONS_RESET_PASSWORD;
	}

	public String formResetPassword(HttpServletRequest request, HttpServletResponse response) {
//		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		String token = null;
		Map<String, Object> param = null;
		Users user = null;
		ArrayList<String> messages = null;
		try {
			connection.beginTransaction();
			token = request.getParameter("token");
			if (token != null && !token.equals("")) {
				param = new HashMap<String, Object>();
				param.put("reset_password_token", token);
				user = (Users) connection.find(Users.QUERY_BY_TOKEN, param);
				param.clear();
				if (user != null)
					request.getSession().setAttribute("currentUserResetPassword", user);
				else {
					messages = new ArrayList<String>();
					messages.add("resetPass.fillFieldEmail");
					request.setAttribute("messages", messages);
//					utilities = null;
					token = null;
					param = null;
					user = null;
					messages = null;
					return RESET_PASSWORD;
				}
			}
		} finally {
			connection.closeTransaction();
//			utilities = null;
			token = null;
			param = null;
			user = null;
			messages = null;
		}
		return RESET_PASSWORD;
	}
	
	public String confirmResetPassword(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUserResetPassword") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		Utilities utilities = new Utilities();
//		utilities.printNameValue(request.getParameterMap(), true);
		Users user = (Users) request.getSession().getAttribute("currentUserResetPassword");
		Encode encode = new Encode();
		boolean isValid = true;
		ArrayList<String> messages = new ArrayList<String>();
		String newPassword = request.getParameter("newPassword");
		String retypePassword = request.getParameter("retypePassword");
		
		try {
			if (newPassword.equals("")) {
				isValid = false;	
				messages.add("resetPassword.fillFieldNewPassword");
			}
			if (retypePassword.equals("")) {
				isValid = false;	
				messages.add("resetPassword.fillFieldRetypePassword");
			}
			if (newPassword.compareTo(retypePassword) != 0) {
				isValid = false;	
				messages.add("resetPassword.differentPasswords");
			}			
			if (!utilities.isPassword(newPassword)) {
				isValid = false;
				messages.add("resetPassword.newPasswordInvalid");
			}
			if (isValid) { 
				user.setEncrypted_password(encode.encryptsPassword(newPassword));
				user.setUpdated_at(new Date());
				connection.beginTransaction();
				connection.update(user);
				connection.commit();
				connection.closeTransaction();
				request.getSession().setAttribute("currentUser", user);
				request.setAttribute("successResetPassword", true);
			}
			else {
				request.setAttribute("newPassword", newPassword);
				request.setAttribute("retypePassword", retypePassword);
				request.setAttribute("messages", messages);
			}
		} finally {
			utilities = null;
			user = null;
			encode = null;
			messages = null;
			newPassword = null;
			retypePassword = null;
		}
		return RESET_PASSWORD;
	}

	public String formUpdatePassword(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		return UPDATE_PASSWORD;
	}

	public String confirmUpdatePassword(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("password") == null && request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		Utilities utilities = new Utilities();
		Users user = (Users) request.getSession().getAttribute("currentUser");
		Encode encode = new Encode();
		boolean isValid = true;
		ArrayList<String> messages = new ArrayList<String>();
		String currentPassword = request.getParameter("currentPassword");
		String newPassword = request.getParameter("newPassword");
		String retypePassword = request.getParameter("retypePassword");
		
		try {
			if (currentPassword.equals("")) {
				isValid = false;	
				messages.add("updatePassword.fillFieldCurrentPassword");
			}
			if (newPassword.equals("")) {
				isValid = false;	
				messages.add("updatePassword.fillFieldNewPassword");
			}
			if (retypePassword.equals("")) {
				isValid = false;	
				messages.add("updatePassword.fillFieldRetypePassword");
			}
			if (newPassword.compareTo(retypePassword) != 0) {
				isValid = false;	
				messages.add("updatePassword.differentPasswords");
			}			
			if (user.getEncrypted_password().compareTo(encode.encryptsPassword(currentPassword)) != 0) {
				isValid = false;
				messages.add("updatePassword.differentCurrentPassword");
			}
			if (encode.encryptsPassword(currentPassword).compareTo(encode.encryptsPassword(newPassword)) == 0) {
				isValid = false;
				messages.add("updatePassword.passwordEquals");
			}
			if (!utilities.isPassword(newPassword)) {
				isValid = false;
				messages.add("updatePassword.newPasswordInvalid");
			}
			if (isValid) { 
				user.setEncrypted_password(encode.encryptsPassword(newPassword));
				user.setUpdated_at(new Date());
				connection.beginTransaction();
				connection.update(user);
				connection.commit();
				connection.closeTransaction();
				request.getSession().setAttribute("currentUser", user);
				request.setAttribute("successUpdatePassword", true);
			}
			else {
				request.setAttribute("currentPassword", currentPassword);
				request.setAttribute("newPassword", newPassword);
				request.setAttribute("retypePassword", retypePassword);
				request.setAttribute("messages", messages);
			}
		} finally {
			utilities = null;
			user = (Users) request.getSession().getAttribute("currentUser");
			encode = null;
			messages = null;
			currentPassword = null;
			newPassword = null;
			retypePassword = null;
		}
		return UPDATE_PASSWORD;
	}

	public void uploadImage(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") != null) {
//			Utilities utilities = null;
			Users user = null;
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
				user = (Users) connection.find(Users.class, Integer.parseInt(request.getParameter("user_id")));
				messages = new ArrayList<String>();
				pathImages = (String) request.getSession().getAttribute("pathImages");
				nameImage = String.valueOf(user.getId());
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
//										utilities = new Utilities();
										original = ImageIO.read(item.getInputStream());
										if (original.getWidth() <= 1000) {
//											alterada = utilities.redimensionar(original, 640, 480);
											pathImages = pathImages + "users/" + nameImage + "/";
											uploadPath = new File(pathImages);
											if (!uploadPath.exists())
												uploadPath.mkdirs();
											uploadPath.setWritable(true, true);
											nameImage =  (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(dataAtual) + "_" + nameImage + "." + ext;
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
					user.setImage_file_name("users/" + user.getId() + "/" + nameImage);
					user.setImage_file_type(ext);
					user.setUpdated_at(dataAtual);
					connection.beginTransaction();
					connection.update(user);
					connection.commit();
					request.getSession().setAttribute("currentUser", user);
				}
				request.setAttribute("messages", messages);
				connection.closeTransaction();
				try {
					request.getRequestDispatcher("./Controller?form=actions.UsersActions&action=form&panel=true").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} finally {
				connection.closeTransaction();
//				utilities = null;
				user = null;
				messages = null;
				pathImages = null;
				nameImage = null;
				ext = null;
				dataAtual = null;
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
		
//		Utilities utilities = null;
//		String image = null;
//		String pathImages = null;
//		String serverImages = null;
//		InputStream inputStream = null;
//		OutputStream outputStream = null;
//		URL urlImage = null;
//		HttpURLConnection httpCode = null;
//		try {
//			utilities = new Utilities();
//			image = request.getParameter("image");
//			pathImages = (String) request.getSession().getAttribute("pathImages");
//			image = pathImages + image;
//			if (new File(image).exists()) {
//				outputStream = response.getOutputStream();
//				outputStream.write(utilities.toBytes(new FileInputStream(image)));
//				outputStream.flush();
//				outputStream.close();
//			} else {
//				serverImages = (String) request.getSession().getAttribute("giroUrlServerImages");
//				image = serverImages + "images/defaultImage.png";
//				urlImage = new URL(image);
//				httpCode = (HttpURLConnection) urlImage.openConnection();
//				if (httpCode.getResponseCode() == 200) {
//					inputStream = urlImage.openStream();
//					outputStream = response.getOutputStream();
//					outputStream.write(utilities.toBytes(inputStream));
//					outputStream.flush();
//					outputStream.close();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			utilities = null;
//			image = null;
//			pathImages = null;
//			serverImages = null;
//			inputStream = null;
//			outputStream = null;
//			urlImage = null;
//			httpCode = null;
//		}
		
	}
	
	public void deleteImage(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("currentUser") != null) {
			Users user = null;
			String pathImages = null;
			File file = null;
			try {
				connection.beginTransaction();
				user = (Users) connection.find(Users.class, Integer.parseInt(request.getParameter("user_id")));
				pathImages = (String) request.getSession().getAttribute("pathImages");
				file = new File(pathImages + user.getImage_file_name());
				if (file.exists()) {
					file.setWritable(true, true);
					file.delete();
					user.setImage_file_name("");
					user.setImage_file_type("");
					user.setUpdated_at(new Date());
					connection.update(user);
					connection.commit();
					file = null;
					request.getSession().setAttribute("currentUser", user);
				}
				connection.closeTransaction();
				try {
					request.getRequestDispatcher("./Controller?form=actions.UsersActions&action=form&panel=true").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					connection.closeTransaction();
					user = null;
					pathImages = null;
					file = null;
				}
			} finally {
				connection.closeTransaction();
				user = null;
				pathImages = null;
				file = null;
			}
		}
	}
	
	public String purchases(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getSession().getAttribute("currentUser") == null)
			return (String) request.getSession().getAttribute("LOGIN");
		
		Users user = null;
		int current = 0;		
		int maximum = 15;
		int totalPages = 0;
		int totalResults = 0;
		Map<String, Object> param = null;
		List<?> listPurchasesHistoric = null;
		
		try {
			connection.beginTransaction();
			user = (Users) request.getSession().getAttribute("currentUser");
			current = Integer.parseInt(request.getParameter("current"));		
			param = new HashMap<String, Object>();
			param.put("user_id", user.getId());
			listPurchasesHistoric = connection.list(Orders.QUERY_LIST_ALL_BY_USER, param, current, maximum);
			param.clear();
			totalResults = listPurchasesHistoric.size();
			if (totalResults > 0)
				totalPages = totalResults / maximum;
			request.setAttribute("current", current);
			request.setAttribute("maximum", maximum);
			request.setAttribute("totalResults", totalResults);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("listPurchasesHistoric", listPurchasesHistoric);
		} finally {
			connection.closeTransaction();
			user = null;
			param = null;
			listPurchasesHistoric = null;
		}
			
		return PURCHASES;
	}
	
	public List<Advertisements> reverse(List<Advertisements> list) {
		List<Advertisements> copy = list;
	    Collections.reverse(copy);
	    return copy;
	}
	
	public String terms(HttpServletRequest request, HttpServletResponse response) {
		return TERMS;
	}
	
}
