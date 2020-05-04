package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "history_count_click")
@NamedQueries({
	@NamedQuery(
			name = "query_count_history_click_by_user_and_date", 
			query = "SELECT COUNT(hcc) FROM HistoryCountClick hcc "
					+ " WHERE "
					+ " hcc.advertisement_id.user_id.id = :user_id AND "
					+ " hcc.created_at BETWEEN :initialDate AND :currentDate"),
	@NamedQuery(
			name = "query_history_count_click_by_user_and_date", 
			query = "FROM HistoryCountClick hcc "
					+ " WHERE "
					+ " hcc.advertisement_id.user_id.id = :user_id AND "
					+ " hcc.created_at BETWEEN :initialDate AND :currentDate "
					+ " ORDER BY hcc.created_at DESC"),
	@NamedQuery(
			name = "query_count_history_click_by_date", 
			query = "SELECT COUNT(hcc) FROM HistoryCountClick hcc "
					+ " WHERE "
					+ " hcc.created_at BETWEEN :initialDate AND :currentDate"),
	@NamedQuery(
			name = "query_history_count_click_by_date", 
			query = "FROM HistoryCountClick hcc "
					+ " WHERE "
					+ " hcc.created_at BETWEEN :initialDate AND :currentDate AND "
					+ " hcc.addresses_id IS NULL "
					+ " ORDER BY hcc.created_at "),
})

public final class HistoryCountClick implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_COUNT_HISTORY_CLICK_BY_USER_AND_DATE = "query_count_history_click_by_user_and_date";
	public static final String QUERY_HISTORY_COUNT_CLICK_BY_USER_AND_DATE = "query_history_count_click_by_user_and_date";
	public static final String QUERY_COUNT_HISTORY_CLICK_BY_DATE = "query_count_history_click_by_date";
	public static final String QUERY_HISTORY_COUNT_CLICK_BY_DATE = "query_history_count_click_by_date";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 

	@JoinColumn(name = "ADVERTISEMENT_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Advertisements advertisement_id;
	
	@JoinColumn(name = "STORE_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Stores store_id;
	
	@JoinColumn(name = "ADDRESSES_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Addresses addresses_id;
	
	@Column(name = "LOGIN")
	private Integer login;
	
	@Column(name = "LATITUDE")
	private String latitude;
	
	@Column(name = "LONGITUDE")
	private String longitude;
	
	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at;

	public Integer getId() {
		return id;
	}

	public Advertisements getAdvertisement_id() {
		return advertisement_id;
	}

	public void setAdvertisement_id(Advertisements advertisement_id) {
		this.advertisement_id = advertisement_id;
	}

	public Stores getStore_id() {
		return store_id;
	}

	public void setStore_id(Stores store_id) {
		this.store_id = store_id;
	}

	public Addresses getAddresses_id() {
		return addresses_id;
	}

	public void setAddresses_id(Addresses addresses_id) {
		this.addresses_id = addresses_id;
	}

	public Integer getLogin() {
		return login;
	}

	public void setLogin(Integer login) {
		this.login = login;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

}
