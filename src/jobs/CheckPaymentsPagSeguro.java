package jobs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import br.com.uol.pagseguro.domain.TransactionSearchResult;
import br.com.uol.pagseguro.domain.TransactionSummary;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.TransactionSearchService;
import connection.Connection;
import entity.Advertisements;
import entity.Orders;
import pagseguro.PagSeguroUtils;
import utils.Utilities;

public class CheckPaymentsPagSeguro extends TimerTask {

	private Connection connection = null;
	
	public CheckPaymentsPagSeguro(Connection connection) {
		this.connection = connection;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		utilities.printDataHora(" - PAGSEGURO => INICIO ...", isPrint);
		Calendar initialDate = null;
		Calendar finalDate = null;
		Integer page = 0;
		Integer maxPageResults = 0;
		TransactionSearchResult transactionSearchResult = null;
		List<TransactionSummary> listTransactionSummaries = null;
		Orders order = null;
		Map<String, Object> param = null;
		List<Advertisements> listAdvertisements = null;
		try {
			connection.beginTransaction();
			initialDate = Calendar.getInstance();
			initialDate.add(Calendar.DAY_OF_MONTH, -15);
			finalDate = Calendar.getInstance();
			utilities.printDataHora(" - PAGSEGURO => INITIAL DATE: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(initialDate.getTime()) + " - FINAL DATE: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(finalDate.getTime()), isPrint);
			page = Integer.valueOf(1);
			maxPageResults = Integer.valueOf(1000);
			transactionSearchResult = TransactionSearchService.searchByDate(new PagSeguroUtils(connection).getCredentials(), initialDate.getTime(), finalDate.getTime(), page, maxPageResults);
			if (transactionSearchResult != null) {
				listTransactionSummaries = transactionSearchResult.getTransactionSummaries();
				param = new HashMap<String, Object>();
				for (TransactionSummary currentTransactionSummary : listTransactionSummaries) {
					param.put("reference", currentTransactionSummary.getReference());
					order = (Orders) connection.find(Orders.QUERY_FIND_BY_REFERENCE, param);
					param.clear();
					if (order != null && order.getChecked() == 0) {
						utilities.printDataHora(" - PAGSEGURO => CODE: "  + currentTransactionSummary.getCode(), isPrint);
						order.setTransaction_code(currentTransactionSummary.getCode());
						order.setDate(currentTransactionSummary.getDate());
						order.setLast_event(currentTransactionSummary.getLastEvent());
						order.setEscrow_end_date(currentTransactionSummary.getEscrowEndDate());
						order.setGross_amount(currentTransactionSummary.getGrossAmount());
						order.setNet_amount(currentTransactionSummary.getNetAmount());
						order.setDiscount_amount(currentTransactionSummary.getDiscountAmount());
						order.setExtra_amount(currentTransactionSummary.getExtraAmount());
						order.setFee_amount(currentTransactionSummary.getFeeAmount());
						order.setCod_method(currentTransactionSummary.getPaymentMethod().getType().getValue());
						order.setMethod(currentTransactionSummary.getPaymentMethod().getType().name());
						order.setCod_status(currentTransactionSummary.getStatus().getValue());
						order.setStatus(currentTransactionSummary.getStatus().name());
						order.setDescription(currentTransactionSummary.getStatus().getDescription());
						order.setUpdated_at(new Date());
						if (order.getCod_status() == 3 && order.getChecked() == 0) {
							order.getUser_id().setBalance(order.getUser_id().getBalance().add(order.getCredit()));
							order.setChecked(1);
							param.put("user_id", order.getUser_id().getId());
							listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_PUBLISHED_BY_USER, param);
							param.clear();
							for (Advertisements aux : listAdvertisements) {
								aux.setStatus(2);
								connection.update(aux);
							}
						}
						else if (order.getCod_status() == 7 && order.getChecked() == 0) {
							order.setChecked(1);
						}
						connection.update(order);
					}
				}
				connection.commit();
				utilities.printDataHora(" - PAGSEGURO => ... FIM", isPrint);
			}
		} catch (PagSeguroServiceException e) {
			System.err.println(e.getMessage());
		} finally {
			connection.closeTransaction();
			utilities = null;
			initialDate = null;
			finalDate = null;
			page = 0;
			maxPageResults = 0;
			transactionSearchResult = null;
			listTransactionSummaries = null;
			order = null;
			param = null;
			listAdvertisements = null;
		}
	}

}
