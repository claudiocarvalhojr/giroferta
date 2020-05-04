package pagseguro;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.enums.DocumentType;
import br.com.uol.pagseguro.enums.ShippingType;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.service.TransactionSearchService;
import connection.Connection;
import entity.Orders;
import entity.Users;
import utils.Utilities;

public final class PagSeguroUtils {
	
	private static final String FORM = "buyCredit.jsp";
	private static final String SUCCESS = "confirmation.jsp";
	private static final String FAIL = "pageError.jsp";
	private Connection connection = null;
	
//	public PagSeguroUtils() {}
	
	public PagSeguroUtils(Connection connection) {
		this.connection = connection;
	}
	
	public String form(HttpServletRequest request, HttpServletResponse response) {
		return FORM;
	}

	public String confirmation(HttpServletRequest request, HttpServletResponse response) {
		try {
			connection.beginTransaction();
			String checkoutCode = "";
			String transactionCode = "";
			if (request.getParameter("checkoutCode") != null && request.getParameter("transactionCode") != null) {
				checkoutCode = request.getParameter("checkoutCode");
				transactionCode = request.getParameter("transactionCode");
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("checkout_code", checkoutCode);
				Orders order = (Orders) connection.find(Orders.QUERY_FIND_BY_CHECKOUT_CODE, param);
				param.clear();
				if (order != null && order.getChecked() == 0) {
					Transaction transaction = null;
					try {
						transaction = TransactionSearchService.searchByCode(getCredentials(), transactionCode);
						if (transaction != null) {
							order.setTransaction_code(transaction.getCode());
							order.setDate(transaction.getDate());
							order.setLast_event(transaction.getLastEventDate());
							order.setEscrow_end_date(transaction.getEscrowEndDate());
							order.setGross_amount(transaction.getGrossAmount());
							order.setNet_amount(transaction.getNetAmount());
							order.setDiscount_amount(transaction.getDiscountAmount());
							order.setExtra_amount(transaction.getExtraAmount());
							order.setFee_amount(transaction.getFeeAmount());
							order.setCod_method(transaction.getPaymentMethod().getType().getValue());
							order.setMethod(transaction.getPaymentMethod().getType().name());
							order.setCod_status(transaction.getStatus().getValue());
							order.setStatus(transaction.getStatus().name());
							order.setDescription(transaction.getStatus().getDescription());
							order.setUpdated_at(new Date());
							connection.update(order);
							connection.commit();
							request.setAttribute("order", order);
						}
						else {
							connection.closeTransaction();
							return FAIL;
						}
					}
					catch (PagSeguroServiceException e) {
						System.err.println(e.getMessage());
					}
				}
				else {
					connection.closeTransaction();
					return FAIL;
				}
			}
			else {
				connection.closeTransaction();
				return FAIL;
			}
		} finally {
			connection.closeTransaction();
		}
		return SUCCESS;
	}

	public void checkoutCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			connection.beginTransaction();
			String gson = "";
	    	Utilities util = new Utilities();
			Users user = (Users) request.getSession().getAttribute("currentUser");
			String ddd = "";
			String telefone = "";
			String cpfCnpj = "";
			DocumentType typePerson;
			if (!user.getPhone().equals("")) {
				telefone = util.onlyNumbers(user.getPhone());
				ddd = telefone.substring(0, 2);
				telefone = telefone.substring(2, telefone.length());
			}
			if (user.getType_person().equals("F")) {
				cpfCnpj = user.getCpf();
				typePerson = DocumentType.CPF;
			}
			else {
				cpfCnpj = user.getCnpj();
				typePerson = DocumentType.CNPJ;
			}
			Checkout checkout = new Checkout();
			String creditNumber = request.getParameter("credit");
			String credit = creditNumber + ".00";
			Date dataAtual = new Date();
			Orders order = new Orders();
			order.setUser_id(user);
			order.setCheckout_code("");
			order.setTransaction_code("");
			order.setCredit(new BigDecimal(credit));
			order.setQuantity(1);
			order.setChecked(0);
			order.setCreated_at(dataAtual);
			order.setUpdated_at(dataAtual);
			connection.save(order);
			String reference = ""; 
			if (creditNumber.length() == 2)
				reference = "CREDIT00" + creditNumber + "-USER" + user.getId() + "-ORDER" + order.getId();
			else if (creditNumber.length() == 3)
				reference = "CREDIT0" + creditNumber + "-USER" + user.getId() + "-ORDER" + order.getId();
			else if (creditNumber.length() == 4)
				reference = "CREDIT" + creditNumber + "-USER" + user.getId() + "-ORDER" + order.getId();
			checkout.addItem(
					"CREDIT" + creditNumber, 
					"GIROFERTA - CRÉDITO DE R$ " + creditNumber + ",00", 
					Integer.valueOf(1), 
					new BigDecimal(credit), 
					new Long(0), 
					new BigDecimal("0.00")
				);
/*
			checkout.setShippingAddress(
						user.getCity_id().getState_id().getCountry_id().getInitials(), 
						user.getCity_id().getState_id().getInitials(), 
						user.getCity_id().getName(), 
						user.getNeighborhood(), 
						util.onlyNumbers(user.getPostal_code()), 
						user.getAddress(), 
						user.getNumber(), 
						user.getComplement()
					);
*/
			checkout.setShippingCost(new BigDecimal("0.00"));
			checkout.setShippingType(ShippingType.NOT_SPECIFIED);
			if (user.getType_person().equals("F")) {
				checkout.setSender(
						user.getName() + " " + user.getLast_name(),
						user.getEmail(), 
						ddd, 
						telefone, 
						typePerson, 
						cpfCnpj
					);
			}
			else {
				checkout.setSender(
						user.getName() + " " + user.getLast_name(),
						user.getEmail(), 
						ddd, 
						telefone
					);
			}
			checkout.setCurrency(Currency.BRL);
			checkout.setReference(reference);
//			checkout.setRedirectURL((String) request.getSession().getAttribute("giroUrl") + "Controller?form=actions.PagSeguroActions&action=confirmation&panel=true");
//			checkout.setNotificationURL((String) request.getSession().getAttribute("giroUrl") + "Controller?form=actions.PagSeguroActions&action=resource&op=notifications");
			try {
				String checkoutCode = checkout.register(getCredentials(), true);
				order.setCheckout_code(checkoutCode);
				order.setReference(reference);
				connection.update(order);
				connection.commit();
				gson = new Gson().toJson(checkoutCode);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gson);
			} catch (PagSeguroServiceException e) {
				System.err.println("PagSeguroServiceException: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("IOException: " + e.getMessage());
			}
		} finally {
			connection.closeTransaction();
		}
	}

	public Credentials getCredentials() {
		
		PagSeguroConfig.setProductionEnvironment();
//		PagSeguroConfig.setSandboxEnvironment();
		
		AccountCredentials credentials = null;
		
		try {
			
			credentials = PagSeguroConfig.getAccountCredentials();
			
//			System.out.println("\nENVIROMENT: " + PagSeguroConfig.getEnvironment());

			credentials.setEmail("pagamentos@giroferta.com");
//			credentials.setEmail("financeiro@giroferta.com");
			
			// pagamentos@giroferta.com
			credentials.setProductionToken("7F131D36287A45D1816B9DEB71AAFCCB");
//			credentials.setSandboxToken("BD4A1F9E91B24949A8D4D46A9B1BA136");
			
			// financeiro@giroferta.com
//			credentials.setProductionToken("1CB07792718A487BAA8C1BE89BDB498F");
//			credentials.setSandboxToken("2D47DF0B059D43A0AB9FCC67D33E077D");
			
//			System.out.println("EMAIL: " + credentials.getEmail());
//			System.out.println("TOKEN: " + credentials.getToken() +"\n");
			
		} catch (PagSeguroServiceException e) {
			e.printStackTrace();
		}
		
		return credentials;
	}

}
