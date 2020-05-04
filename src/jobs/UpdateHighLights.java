package jobs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.hibernate.jdbc.ReturningWork;

import connection.Connection;
import entity.Admin;
import entity.AdsStoresMostAccessed;
import entity.AdsStoresRecentlyAdded;
import entity.AdvertisementStores;
import entity.Advertisements;
import entity.ClickValues;
import entity.Stores;
import utils.Utilities;

public class UpdateHighLights extends TimerTask {

	private Connection connection = null;
	private int total = 0;
	private int parcial = 0;
	
	public UpdateHighLights(Connection connection) {
		this.connection = connection;
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getParcial() {
		return parcial;
	}

	public void setParcial(int parcial) {
		this.parcial = parcial;
	}

	@Override
	public void run() {
		long initialTime = System.currentTimeMillis();
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		Admin admin = null;
		AdsStoresMostAccessed adsStoresMostAccessed = null;
		AdsStoresRecentlyAdded adsStoresRecentlyAdded = null;
		Date dataAtual = new Date();
		List<AdvertisementStores> listMostAccessed = null;
		List<AdvertisementStores> listRecentlyAdded = null;
		try {
			utilities.printDataHora(" - UPDATE HIGHLIGHTS => INICIO ...", isPrint);
			connection.beginTransaction();
			admin = (Admin) connection.find(Admin.class, 1);
			listMostAccessed = getListAdsStores(connection, (admin.getLimit_most_accessed() * 6), 0);
			listRecentlyAdded = getListAdsStores(connection,(admin.getLimit_new_ads() * 4), 1);
			setTotal(listMostAccessed.size() + listRecentlyAdded.size());
	        for(AdvertisementStores advSto : listMostAccessed) {
	        	setParcial(getParcial() + 1);
	        	adsStoresMostAccessed = new AdsStoresMostAccessed();
	        	adsStoresMostAccessed.setAdvertisement_id(advSto.getAdvertisement_id());
	        	adsStoresMostAccessed.setStore_id(advSto.getStore_id());
	        	adsStoresMostAccessed.setUrl(advSto.getAdvertisement_id().getUrl());
	        	adsStoresMostAccessed.setGross_click(advSto.getGross_click());
	        	adsStoresMostAccessed.setCount_click(advSto.getCount_click());
	        	adsStoresMostAccessed.setStatus(advSto.getStatus());
	        	adsStoresMostAccessed.setCreated_at(advSto.getCreated_at());
	        	adsStoresMostAccessed.setUpdated_at(dataAtual);
	        	connection.save(adsStoresMostAccessed);
	        }
	        for(AdvertisementStores advSto : listRecentlyAdded) {
	        	setParcial(getParcial() + 1);
	        	adsStoresRecentlyAdded = new AdsStoresRecentlyAdded();
	        	adsStoresRecentlyAdded.setAdvertisement_id(advSto.getAdvertisement_id());
	        	adsStoresRecentlyAdded.setStore_id(advSto.getStore_id());
	        	adsStoresRecentlyAdded.setUrl(advSto.getAdvertisement_id().getUrl());
	        	adsStoresRecentlyAdded.setGross_click(advSto.getGross_click());
	        	adsStoresRecentlyAdded.setCount_click(advSto.getCount_click());
	        	adsStoresRecentlyAdded.setStatus(advSto.getStatus());
	        	adsStoresRecentlyAdded.setCreated_at(advSto.getCreated_at());
	        	adsStoresRecentlyAdded.setUpdated_at(dataAtual);
	        	connection.save(adsStoresRecentlyAdded);
	        }
			connection.getSession().createSQLQuery("TRUNCATE TABLE ads_stores_most_accessed").executeUpdate();
			connection.getSession().createSQLQuery("TRUNCATE TABLE ads_stores_recently_added").executeUpdate();
	        connection.commit();
			utilities.printDataHora(" - UPDATE HIGHLIGHTS => ... FIM | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} finally {
			connection.closeTransaction();
			utilities = null;
			admin = null;
			adsStoresMostAccessed = null;
			adsStoresRecentlyAdded = null;
			dataAtual = null;
			listMostAccessed = null;
			listRecentlyAdded = null;
		}
		
	}

	private List<AdvertisementStores> getListAdsStores(Connection connection, int limitMostAccessed, int typeList) {
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
						+ " ORDER BY advsto.count_click DESC "
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
	
}
