package jobs;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.hibernate.jdbc.Work;

import connection.Connection;
import entity.Advertisements;
import entity.Stores;
import entity.Users;
import utils.Utilities;

public class ReplyAdsInStores extends TimerTask {

	private Connection connection = null;
	
	private int idUser = 0;
	
	public ReplyAdsInStores(Connection connection, int idUser) {
		this.connection = connection;
		this.idUser = idUser;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		long initialTime = System.currentTimeMillis();
		boolean isPrint = true;
		boolean isPrintUnit = false;
		Utilities utilities = new Utilities();
		List<Users> listUsers = null;
		List<Stores> listStores = new ArrayList<Stores>();
		List<Advertisements> listAdvertisements = new ArrayList<Advertisements>();
		Map<String, Object> param = new HashMap<String, Object>();
		int limitAdvertisements = 100;
		int totalAdvertisements = 0;
		int qtdaAtualAdvertisements = 0;
		try {
			utilities.printDataHora(" - REPLY ADS IN STORES => INICIO ...", isPrint);
			connection.beginTransaction();
			if (idUser == 0)
				listUsers = (List<Users>) connection.list(Users.QUERY_IMPORT_ALL);
			else {
				param.put("user_id", idUser);
				listUsers = (List<Users>) connection.list(Users.QUERY_IMPORT_ALL_BY_USER);
				param.clear();
			}
			for (Users user : listUsers) {
				qtdaAtualAdvertisements = 0;
				if (idUser == 0)
					param.put("user_id", user.getId());
				else
					param.put("user_id", idUser);
				totalAdvertisements = connection.count(Advertisements.QUERY_COUNT_TOTAL_BY_USER, param);
				param.clear();
				if (idUser == 0)
					param.put("user_id", user.getId());
				else
					param.put("user_id", idUser);
				listStores = (List<Stores>) connection.list(Stores.QUERY_BY_USER, param);
				param.clear();
				if (totalAdvertisements > limitAdvertisements) {
					totalAdvertisements /= limitAdvertisements;
					for (int i=0; i <= totalAdvertisements; i++) {
						utilities.printDataHora(" - REPLY ADS IN STORES => USER/EMAIL: " + user.getId() + " | " + user.getEmail() + " | QTDA: " + qtdaAtualAdvertisements, isPrintUnit);
						if (idUser == 0)
							param.put("user_id", user.getId());
						else
							param.put("user_id", idUser);
						listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_BY_USER_AND_ALL_STORES, param, qtdaAtualAdvertisements, limitAdvertisements);
						param.clear();
						saveOrUpdate(listAdvertisements, listStores);
						qtdaAtualAdvertisements += limitAdvertisements;
					}
				}
				else {
					utilities.printDataHora(" - REPLY ADS IN STORES => USER/EMAIL: " + user.getId() + " | " + user.getEmail() + " | QTDA: " + totalAdvertisements, isPrintUnit);
					if (idUser == 0)
						param.put("user_id", user.getId());
					else
						param.put("user_id", idUser);
					listAdvertisements = (List<Advertisements>) connection.list(Advertisements.QUERY_BY_USER_AND_ALL_STORES, param);
					param.clear();
					saveOrUpdate(listAdvertisements, listStores);
				}
			}
			connection.commit();
			utilities.printDataHora(" - REPLY ADS IN STORES => ... FIM | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			connection.closeTransaction();
			utilities = null;
			listUsers = null;
			listStores = null;
			listAdvertisements = null;
			param = null;
//			System.gc();
		}
	}
	
	private int saveOrUpdate(final List<Advertisements> listAdvertisements, final List<Stores> listStores) {
		int count = 0;
		connection.getSession().doWork(new Work() {
			@Override
			public void execute(java.sql.Connection con) throws SQLException {
				boolean isPrint = false;
				int count = 0;
				Utilities utilities = new Utilities();
				Date dataAtual = new Date();
				PreparedStatement st = null;
				String query = "INSERT INTO advertisement_stores (advertisement_id, store_id, url, gross_click, count_click, status, created_at, updated_at) "
							+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?) "
							+ " ON DUPLICATE KEY UPDATE "
							+ " url = VALUES(url), updated_at = VALUES(updated_at) ";
				try {
					st = con.prepareStatement(query);
					for (Stores store : listStores) {
						for (Advertisements advertisement : listAdvertisements) {
							st.setInt(1, advertisement.getId());
							st.setInt(2, store.getId());
							st.setString(3, "advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
							st.setInt(4, 0);
							st.setInt(5, 0);
							st.setInt(6, store.getStatus());
							st.setTimestamp(7, getCurrentDatetime(dataAtual));
							st.setTimestamp(8, getCurrentDatetime(dataAtual));
							st.addBatch();
							count++;
							if (count % 1000 == 0)
								utilities.printDataHora(" - REPLY ADS IN STORES => LOJA: " + store.getFilial() + " | QTDA: " + count, isPrint);
						}
					}
					st.executeBatch();
					con.commit();
				} finally {
					st.close();
				}
			}
		});
		return count;
	}
	
//	private void saveOrUpdate2(final List<Advertisements> listAdvertisements, final List<Stores> listStores) {
//		connection.getSession().doWork(new Work() {
//			@Override
//			public void execute(java.sql.Connection con) throws SQLException {
//				boolean isPrint = false;
//				int count = 0;
//				Utilities utilities = new Utilities();
//				Date dataAtual = new Date();
//				PreparedStatement pstmInsert = null;
//				PreparedStatement pstmUpdate = null;
//				String queryInsert = "INSERT INTO advertisement_stores (advertisement_id, store_id, url, gross_click, count_click, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//				String queryUpdate = "UPDATE advertisement_stores SET url = ?, updated_at = ? WHERE advertisement_id = ? AND store_id = ?";
//				try {
//					pstmInsert = con.prepareStatement(queryInsert);
//					pstmUpdate = con.prepareStatement(queryUpdate);
//					for (Stores store : listStores) {
//						for (Advertisements advertisement : listAdvertisements) {
//							if (Duration.between(advertisement.getCreated_at().toInstant(), Instant.now()).toDays() == 0) {
//								pstmInsert.setInt(1, advertisement.getId());
//								pstmInsert.setInt(2, store.getId());
//								pstmInsert.setString(3, "advertisement/" + advertisement.getId() + "/" + store.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
//								pstmInsert.setInt(4, 0);
//								pstmInsert.setInt(5, 0);
//								pstmInsert.setInt(6, store.getStatus());
//								pstmInsert.setTimestamp(7, getCurrentDatetime(dataAtual));
//								pstmInsert.setTimestamp(8, getCurrentDatetime(dataAtual));
//								pstmInsert.addBatch();
//								count++;
//							}
//							else {
//								pstmUpdate.setString(1, "advertisement/" + advertisement.getId() + "/" + store.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
//								pstmUpdate.setTimestamp(2, getCurrentDatetime(dataAtual));
//								pstmUpdate.setInt(3, advertisement.getId());
//								pstmUpdate.setInt(4, store.getId());
//								pstmUpdate.addBatch();
//								count++;
//							}
//							if (count % 1000 == 0)
//								utilities.printDataHora(" - REPLY ADS IN STORES => LOJA: " + store.getFilial() + " | QTDA: " + count, isPrint);
//						}
//					}
//					pstmInsert.executeBatch();
//					pstmUpdate.executeBatch();
//					con.commit();
//				} finally {
//					pstmInsert.close();
//					pstmUpdate.close();
//				}
//			}
//		});
//	}
	
//	private void save(final List<Advertisements> listAdvertisements, final List<Stores> listStores) {
//		connection.getSession().doWork(new Work() {
//			@Override
//			public void execute(java.sql.Connection con) throws SQLException {
//				boolean isPrint = false;
//				int count = 0;
//				Utilities utilities = new Utilities();
//				Date dataAtual = new Date();
//				PreparedStatement st = null;
//				String query = "INSERT INTO advertisement_stores (advertisement_id, store_id, url, gross_click, count_click, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//				try {
//					st = con.prepareStatement(query);
//					for (Stores store : listStores) {
//						for (Advertisements advertisement : listAdvertisements) {
//							st.setInt(1, advertisement.getId());
//							st.setInt(2, store.getId());
//							st.setString(3, "advertisement/" + advertisement.getId() + "/" + store.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
//							st.setInt(4, 0);
//							st.setInt(5, 0);
//							st.setInt(6, store.getStatus());
//							st.setTimestamp(7, getCurrentDatetime(dataAtual));
//							st.setTimestamp(8, getCurrentDatetime(dataAtual));
//							st.addBatch();
//							count++;
//							if (count % 1000 == 0)
//								utilities.printDataHora(" - REPLY ADS IN STORES => LOJA: " + store.getFilial() + " | QTDA: " + count, isPrint);
//						}
//					}
//					st.executeBatch();
//					con.commit();
//				} finally {
//					st.close();
//				}
//			}
//		});
//	}
	
//	private void update(final List<Advertisements> listAdvertisements, final List<Stores> listStores) {
//		connection.getSession().doWork(new Work() {
//			@Override
//			public void execute(java.sql.Connection con) throws SQLException {
//				boolean isPrint = false;
//				int count = 0;
//				Utilities utilities = new Utilities();
//				Date dataAtual = new Date();
//				PreparedStatement st = null;
//				String query = "UPDATE advertisement_stores SET url = ?, updated_at = ? WHERE advertisement_id = ? AND store_id = ?";
//				try {
//					st = con.prepareStatement(query);
//					for (Stores store : listStores) {
//						for (Advertisements advertisement : listAdvertisements) {
//							st.setString(1, "advertisement/" + advertisement.getId() + "/" + store.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
//							st.setTimestamp(2, getCurrentDatetime(dataAtual));
//							st.setInt(3, advertisement.getId());
//							st.setInt(4, store.getId());
//							st.addBatch();
//							count++;
//							if (count % 1000 == 0)
//								utilities.printDataHora(" - REPLY ADS IN STORES => LOJA: " + store.getFilial() + " | QTDA: " + count, isPrint);
//						}
//					}
//					st.executeBatch();
//					con.commit();
//				} finally {
//					st.close();
//				}
//			}
//		});
//	}
	
	public java.sql.Date getCurrentDate(Date dataAtual) {
	    return new java.sql.Date(dataAtual.getTime());
	}
	
	public java.sql.Timestamp getCurrentDatetime(Date dataAtual) {
	    return new java.sql.Timestamp(dataAtual.getTime());
	}
	
}
