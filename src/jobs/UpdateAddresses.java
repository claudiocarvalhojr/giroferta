package jobs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import connection.Connection;
import entity.Addresses;
import entity.HistoryCountClick;
import entity.HistoryGrossClick;
import entity.HistoryMostAccessed;
import entity.HistoryPurchases;
import entity.HistoryRecentlyAdded;
import entity.HistorySearch;
import utils.Utilities;

public class UpdateAddresses extends TimerTask {
	
	private Connection connection = null;
	
	public UpdateAddresses(Connection connection) {
		this.connection = connection;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		long initialTime = System.currentTimeMillis();
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		URL url = null;
		HttpURLConnection httpConnection = null;
		BufferedReader br = null;
		String line = "";
		String texto = "";
		String status = null;
		JSONParser jsonParser = null;
		JSONObject jsonObject = null;
		JSONArray results = null;
		JSONObject resultsObject1 = null;
		JSONArray addressComponents = null;
		JSONObject addressComponentsObjects = null;
		JSONArray aux = null;
		Addresses addresses = null;
		Map<String, Object> param = new HashMap<String, Object>();
		int countOK = 0;
		String value = "";
		try {
			utilities.printDataHora(" - UPDATE ADDRESSES => INICIO ...", isPrint);
			connection.beginTransaction();
			
			Calendar calendarInitial = Calendar.getInstance();
			calendarInitial.add(Calendar.DAY_OF_MONTH, -2);
			calendarInitial.set(Calendar.MONTH, 0);
			calendarInitial.set(Calendar.YEAR, 2016);
			calendarInitial.set(Calendar.HOUR_OF_DAY, 0);
			calendarInitial.set(Calendar.MINUTE, 0);
			calendarInitial.set(Calendar.SECOND, 0);
			Date initialDate = calendarInitial.getTime();			
			
			Calendar calendarFinal = Calendar.getInstance();
			calendarFinal.add(Calendar.DAY_OF_MONTH, -1);
			calendarFinal.set(Calendar.HOUR_OF_DAY, 23);
			calendarFinal.set(Calendar.MINUTE, 59);
			calendarFinal.set(Calendar.SECOND, 59);
			Date finalDate = calendarFinal.getTime();
			
			param.put("initialDate", initialDate);
			param.put("currentDate", finalDate);
			
			/* 1 - HISTORY COUNT CLICK ************************************* */
			for (HistoryCountClick hcc : (List<HistoryCountClick>) connection.list(HistoryCountClick.QUERY_HISTORY_COUNT_CLICK_BY_DATE, param)) {
				param.clear();
				
				if (!hcc.getLatitude().equals("undefined") &&
					!hcc.getLatitude().equals("") && 
					!hcc.getLatitude().equals("0") && 
					hcc.getLatitude() != null && 
						!hcc.getLongitude().equals("undefined") && 
						!hcc.getLongitude().equals("") && 
						!hcc.getLongitude().equals("0") && 
						hcc.getLongitude() != null) {
				
					param.put("latitude", hcc.getLatitude());
					param.put("longitude", hcc.getLongitude());
					addresses = (Addresses) connection.find(Addresses.QUERY_ADDRESSES_BY_LAT_LONG, param); 
					if (addresses == null) {
						addresses = new Addresses();
						addresses.setLatitude(hcc.getLatitude());
						addresses.setLongitude(hcc.getLongitude());
						addresses.setUpdated(0);
						addresses.setCreated_at(hcc.getCreated_at());
						connection.save(addresses);
						hcc.setAddresses_id(addresses);
						connection.update(hcc);
						countOK++;
					} else {
						hcc.setAddresses_id(addresses);
						connection.update(hcc);
						countOK++;
					}
//					System.out.println(countOK + ") HCC - ID: " + hcc.getId() + " | " + hcc.getLatitude() + " | " + hcc.getLongitude() + " | " + hcc.getCreated_at());
				}
				
			}
			param.clear();
			param.put("initialDate", initialDate);
			param.put("currentDate", finalDate);
			
			/* 2 - HISTORY GROSS CLICK ************************************* */
			for (HistoryGrossClick hgc : (List<HistoryGrossClick>) connection.list(HistoryGrossClick.QUERY_HISTORY_GROSS_CLICK_BY_DATE, param)) {
				param.clear();
				
				if (!hgc.getLatitude().equals("undefined") &&
					!hgc.getLatitude().equals("") && 
					!hgc.getLatitude().equals("0") && 
					hgc.getLatitude() != null && 
						!hgc.getLongitude().equals("undefined") && 
						!hgc.getLongitude().equals("") && 
						!hgc.getLongitude().equals("0") && 
						hgc.getLongitude() != null) {
				
					param.put("latitude", hgc.getLatitude());
					param.put("longitude", hgc.getLongitude());
					addresses = (Addresses) connection.find(Addresses.QUERY_ADDRESSES_BY_LAT_LONG, param); 
					if (addresses == null) {
						addresses = new Addresses();
						addresses.setLatitude(hgc.getLatitude());
						addresses.setLongitude(hgc.getLongitude());
						addresses.setUpdated(0);
						addresses.setCreated_at(hgc.getCreated_at());
						connection.save(addresses);
						hgc.setAddresses_id(addresses);
						connection.update(hgc);
						countOK++;
					} else {
						hgc.setAddresses_id(addresses);
						connection.update(hgc);
						countOK++;
					}
//					System.out.println(countOK + ") HGC - ID: " + hgc.getId() + " | " + hgc.getLatitude() + " | " + hgc.getLongitude() + " | " + hgc.getCreated_at());
				}
				
			}
			param.clear();
			param.put("initialDate", initialDate);
			param.put("currentDate", finalDate);
			
			/* 3 - HISTORY MOST ACCESSED *********************************** */
			for (HistoryMostAccessed hma : (List<HistoryMostAccessed>) connection.list(HistoryMostAccessed.QUERY_HISTORY_MOST_ACCESSED_BY_DATE, param)) {
				param.clear();
				
				if (!hma.getLatitude().equals("undefined") &&
					!hma.getLatitude().equals("") && 
					!hma.getLatitude().equals("0") && 
					hma.getLatitude() != null && 
						!hma.getLongitude().equals("undefined") && 
						!hma.getLongitude().equals("") && 
						!hma.getLongitude().equals("0") && 
						hma.getLongitude() != null) {
				
					param.put("latitude", hma.getLatitude());
					param.put("longitude", hma.getLongitude());
					addresses = (Addresses) connection.find(Addresses.QUERY_ADDRESSES_BY_LAT_LONG, param); 
					if (addresses == null) {
						addresses = new Addresses();
						addresses.setLatitude(hma.getLatitude());
						addresses.setLongitude(hma.getLongitude());
						addresses.setUpdated(0);
						addresses.setCreated_at(hma.getCreated_at());
						connection.save(addresses);
						hma.setAddresses_id(addresses);
						connection.update(hma);
						countOK++;
					} else {
						hma.setAddresses_id(addresses);
						connection.update(hma);
						countOK++;
					}
//					System.out.println(countOK + ") HMA - ID: " + hma.getId() + " | " + hma.getLatitude() + " | " + hma.getLongitude() + " | " + hma.getCreated_at());
				}
				
			}
			param.clear();
			param.put("initialDate", initialDate);
			param.put("currentDate", finalDate);
			
			/* 4 - HISTORY PURCHASES *************************************** */
			for (HistoryPurchases hpu : (List<HistoryPurchases>) connection.list(HistoryPurchases.QUERY_HISTORY_PURCHASES_BY_DATE, param)) {
				param.clear();
				
				if (!hpu.getLatitude().equals("undefined") &&
					!hpu.getLatitude().equals("") && 
					!hpu.getLatitude().equals("0") && 
					hpu.getLatitude() != null && 
						!hpu.getLongitude().equals("undefined") && 
						!hpu.getLongitude().equals("") && 
						!hpu.getLongitude().equals("0") && 
						hpu.getLongitude() != null) {
				
					param.put("latitude", hpu.getLatitude());
					param.put("longitude", hpu.getLongitude());
					addresses = (Addresses) connection.find(Addresses.QUERY_ADDRESSES_BY_LAT_LONG, param); 
					if (addresses == null) {
						addresses = new Addresses();
						addresses.setLatitude(hpu.getLatitude());
						addresses.setLongitude(hpu.getLongitude());
						addresses.setUpdated(0);
						addresses.setCreated_at(hpu.getCreated_at());
						connection.save(addresses);
						hpu.setAddresses_id(addresses);
						connection.update(hpu);
						countOK++;
					} else {
						hpu.setAddresses_id(addresses);
						connection.update(hpu);
						countOK++;
					}
//					System.out.println(countOK + ") HPU - ID: " + hpu.getId() + " | " + hpu.getLatitude() + " | " + hpu.getLongitude() + " | " + hpu.getCreated_at());
				}
				
			}
			param.clear();
			param.put("initialDate", initialDate);
			param.put("currentDate", finalDate);
			
			/* 5 - HISTORY RECENTLY ADDED ********************************** */
			for (HistoryRecentlyAdded hra : (List<HistoryRecentlyAdded>) connection.list(HistoryRecentlyAdded.QUERY_HISTORY_RECENTLY_ADDED_BY_DATE, param)) {
				param.clear();
				
				if (!hra.getLatitude().equals("undefined") &&
					!hra.getLatitude().equals("") && 
					!hra.getLatitude().equals("0") && 
					hra.getLatitude() != null && 
						!hra.getLongitude().equals("undefined") && 
						!hra.getLongitude().equals("") && 
						!hra.getLongitude().equals("0") && 
						hra.getLongitude() != null) {
					
					param.put("latitude", hra.getLatitude());
					param.put("longitude", hra.getLongitude());
					addresses = (Addresses) connection.find(Addresses.QUERY_ADDRESSES_BY_LAT_LONG, param); 
					if (addresses == null) {
						addresses = new Addresses();
						addresses.setLatitude(hra.getLatitude());
						addresses.setLongitude(hra.getLongitude());
						addresses.setUpdated(0);
						addresses.setCreated_at(hra.getCreated_at());
						connection.save(addresses);
						hra.setAddresses_id(addresses);
						connection.update(hra);
						countOK++;
					} else {
						hra.setAddresses_id(addresses);
						connection.update(hra);
						countOK++;
					}
//					System.out.println(countOK + ") HRA - ID: " + hra.getId() + " | " + hra.getLatitude() + " | " + hra.getLongitude() + " | " + hra.getCreated_at());
				}
				
			}
			param.clear();
			param.put("initialDate", initialDate);
			param.put("currentDate", finalDate);
			
			/* 6 - HISTORY SEARCH ****************************************** */
			for (HistorySearch hse : (List<HistorySearch>) connection.list(HistorySearch.QUERY_HISTORY_SEARCH_BY_DATE, param)) {
				param.clear();
				
				if (!hse.getLatitude().equals("undefined") &&
					!hse.getLatitude().equals("") && 
					!hse.getLatitude().equals("0") && 
					hse.getLatitude() != null && 
						!hse.getLongitude().equals("undefined") && 
						!hse.getLongitude().equals("") && 
						!hse.getLongitude().equals("0") && 
						hse.getLongitude() != null) {
				
					param.put("latitude", hse.getLatitude());
					param.put("longitude", hse.getLongitude());
					addresses = (Addresses) connection.find(Addresses.QUERY_ADDRESSES_BY_LAT_LONG, param); 
					if (addresses == null) {
						addresses = new Addresses();
						addresses.setLatitude(hse.getLatitude());
						addresses.setLongitude(hse.getLongitude());
						addresses.setUpdated(0);
						addresses.setCreated_at(hse.getCreated_at());
						connection.save(addresses);
						hse.setAddresses_id(addresses);
						connection.update(hse);
						countOK++;
					} else {
						hse.setAddresses_id(addresses);
						connection.update(hse);
						countOK++;
					}
					hse.setTerm(hse.getTerm().replaceAll( "([\\ud800-\\udbff\\udc00-\\udfff])", ""));				
					connection.update(hse);
//					System.out.println(countOK + ") HSE - ID: " + hse.getId() + " | " + hse.getLatitude() + " | " + hse.getLongitude() + " | " + hse.getCreated_at());
				}
				
			}
			param.clear();

			for (Addresses addr : (List<Addresses>) connection.list(Addresses.QUERY_ADDRESSES_BY_UPDATED)) {
				url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + addr.getLatitude() + "," + addr.getLongitude() + "&location_type=ROOFTOP&result_type=street_address&key=AIzaSyB9nIlzJVvTKJlZcyFXCahdtrYktC_xPwE");
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setConnectTimeout(0);
				if (httpConnection.getResponseCode() == 200) {
					br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "UTF-8"));
					line = "";
					texto = "";
					while (line != null) {
						texto += line + "\n";
						line = br.readLine();
					}
					br.close();
					jsonParser = new JSONParser();
					jsonObject = (JSONObject) jsonParser.parse(texto);
					results = (JSONArray) jsonObject.get("results");							
					status = (String) jsonObject.get("status");
					if (status.compareTo("OK") == 0) {
						addresses = addr;
						resultsObject1 = (JSONObject) results.get(0);
						value = (String) resultsObject1.get("formatted_address");
						if (value != null && value.length() <= 255)
							addresses.setFormatted(value.toUpperCase());
						addressComponents = (JSONArray) resultsObject1.get("address_components");
						addressComponentsObjects = null;
						aux = null;
						for (int i=0; i < addressComponents.size(); i++) {
							addressComponentsObjects = (JSONObject) addressComponents.get(i);
							aux = (JSONArray) addressComponentsObjects.get("types");
							for (int j=0; j < aux.size(); j++) {
								if (((String) aux.get(j)).compareTo("administrative_area_level_1") == 0) {
									value = (String) addressComponentsObjects.get("short_name");
									if (value != null && value.length() <= 2)
										addresses.setState(value.toUpperCase());
								}
								if (((String) aux.get(j)).compareTo("country") == 0) {
									value = (String) addressComponentsObjects.get("short_name");
									if (value != null && value.length() <= 2)
										addresses.setCountry(value.toUpperCase());
								}
								if (((String) aux.get(j)).compareTo("administrative_area_level_2") == 0) {
									value = (String) addressComponentsObjects.get("long_name");
									if (value != null && value.length() <= 255)
										addresses.setCitie(value.toUpperCase());
								}
							}
						}
						countOK++;
						addresses.setUpdated(1);
						addresses.setUpdated_at(new Date());
						connection.update(addresses);
					}
				}
			}
			connection.commit();
			utilities.printDataHora(" - UPDATE ADDRESSES => ... FIM | TOTAL: " + countOK + " | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.closeTransaction();
			utilities = null;
			url = null;
			httpConnection = null;
			br = null;
			line = null;
			texto = null;
			status = null;
			jsonParser = null;
			jsonObject = null;
			results = null;
			resultsObject1 = null;
			addressComponents = null;
			addressComponentsObjects = null;
			aux = null;
			value = null;
		}
		
	}
	
}
