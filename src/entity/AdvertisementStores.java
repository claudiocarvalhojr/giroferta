package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "advertisement_stores")
@NamedQueries({
	@NamedQuery(
			name = "query_advertisementStores_count_total", 
			query = "SELECT COUNT(aux) "
					+ "FROM AdvertisementStores aux "
					+ "WHERE "
					+ "aux.status = 1"),
	@NamedQuery(
			name = "query_advertisementStores_count_total_by_adv_actives", 
			query = "SELECT COUNT(aux) "
					+ "FROM AdvertisementStores aux "
					+ "WHERE "
					+ "aux.advertisement_id.status = 2 AND "
					+ "aux.status = 1"),
	@NamedQuery(
			name = "query_advertisementStores_by_advertisementStores", 
			query = "FROM AdvertisementStores aux "
					+ "WHERE "
					+ "aux.advertisement_id.id = :advertisement_id AND "
					+ "aux.store_id.id = :store_id AND "
//					+ "aux.advertisement_id.status = 2 AND "
					+ "aux.status = 1"),
	@NamedQuery(
			name = "query_advertisementStores_by_advertisement", 
			query = "FROM AdvertisementStores aux "
					+ "WHERE "
					+ "aux.advertisement_id.id = :advertisement_id AND "
					+ "aux.advertisement_id.status = 2 AND "
					+ "aux.status = 1"),
	@NamedQuery(
			name = "query_advertisementStores_by_store", 
			query = "FROM AdvertisementStores aux "
					+ "WHERE "
					+ "aux.advertisement_id.status = 2 AND "
					+ "aux.store_id.status = 1 AND "
					+ "aux.store_id.id = :store_id AND "
					+ "aux.status = 1"),
//	@NamedQuery(
//	name = "query_advertisementStores_count_by_advertisementStores", 
//	query = "SELECT COUNT(aux) "
//			+ "FROM AdvertisementStores aux "
//			+ "WHERE "
//			+ "aux.advertisement_id.id = :advertisement_id AND "
//			+ "aux.store_id.id = :store_id AND "
////			+ "aux.advertisement_id.status = 2 AND "
//			+ "aux.status = 1"),
//	@NamedQuery(
//			name = "query_advertisementStores_list_all_url_active", 
//			query = "SELECT aux.url "
//					+ "FROM AdvertisementStores aux "
//					+ "WHERE "
//					+ "aux.status = 1"),
//	@NamedQuery(
//			name = "query_advertisementStores_by_user", 
//			query = "FROM AdvertisementStores aux "
//					+ "WHERE "
//					+ "aux.store_id.user_id.id = :user_id AND "
//					+ "aux.status = 1"),
//	@NamedQuery(
//			name = "query_advertisementStores_by_title_and_stores", 
//			query = "FROM AdvertisementStores aux "
//					+ "WHERE "
//					+ "(aux.advertisement_id.title LIKE :value_search OR "
//					+ "aux.advertisement_id.brand LIKE :value_search) AND "
//					+ "aux.advertisement_id.status = 2 AND "
//					+ "aux.store_id.status = 1 AND "
//					+ "aux.store_id.id = :store_id AND "
//					+ "aux.status = 1"),
//	@NamedQuery(
//			name = "query_advertisementStores_by_title", 
//			query = "FROM AdvertisementStores aux "
//					+ "WHERE "
//					+ "(aux.advertisement_id.title LIKE :value_search OR "
//					+ "aux.advertisement_id.brand LIKE :value_search) AND "
//					+ "aux.advertisement_id.status = 2 AND "
//					+ "aux.store_id.status = 1 AND "
//					+ "aux.status = 1 "
//					+ "ORDER BY aux.store_id.latitude ASC"),
//	@NamedQuery(
//			name = "query_advertisementStores_random", 
//			query = "FROM AdvertisementStores aux "
//					+ "WHERE "
//					+ "aux.advertisement_id.status = 2 AND "
//					+ "aux.store_id.status = 1 AND "
//					+ "aux.status = 1 "
//					+ "ORDER BY RAND()"),
//	@NamedQuery(
//			name = "query_advertisementStores_most_accessed", 
//			query = "FROM AdvertisementStores aux "
//					+ "WHERE "
//					+ "aux.advertisement_id.status = 2 AND "
//					+ "aux.store_id.status = 1 AND "
//					+ "aux.status = 1 "
//					+ "ORDER BY aux.count_click DESC"),
//	@NamedQuery(
//			name = "query_advertisementStores_new_ads", 
//			query = "FROM AdvertisementStores aux "
//					+ "WHERE "
//					+ "aux.advertisement_id.status = 2 AND "
//					+ "aux.store_id.status = 1 AND "
//					+ "aux.status = 1 "
//					+ "ORDER BY aux.created_at DESC"),
})

public final class AdvertisementStores implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_COUNT_TOTAL                  = "query_advertisementStores_count_total";
	public static final String QUERY_COUNT_BY_ADV_ACTIVES         = "query_advertisementStores_count_total_by_adv_actives";
	public static final String QUERY_BY_ADVERTISEMENTSTORES       = "query_advertisementStores_by_advertisementStores";
	public static final String QUERY_BY_ADVERTISEMENT             = "query_advertisementStores_by_advertisement";
	public static final String QUERY_BY_STORE                     = "query_advertisementStores_by_store";

	//	public static final String QUERY_COUNT_BY_ADVERTISEMENTSTORES = "query_advertisementStores_count_by_advertisementStores";
//	public static final String QUERY_LIST_ALL_URL_ACTIVE          = "query_advertisementStores_list_all_url_active";
//	public static final String QUERY_BY_USER                      = "query_advertisementStores_by_user";
//	public static final String QUERY_BY_TITLE_AND_STORES          = "query_advertisementStores_by_title_and_stores";
//	public static final String QUERY_BY_TITLE                     = "query_advertisementStores_by_title";
//	public static final String QUERY_RANDOM                       = "query_advertisementStores_random";
//	public static final String QUERY_MOST_ACCESSED                = "query_advertisementStores_most_accessed";
//	public static final String QUERY_NEW_ADS                      = "query_advertisementStores_new_ads";

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY) 
//	@Column(name = "ID")
//	private Integer id;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADVERTISEMENT_ID", referencedColumnName = "ID", nullable = false) 
	private Advertisements advertisement_id;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID", referencedColumnName = "ID", nullable = false) 
	private Stores store_id;

	@Column(name = "URL")
	private String url;

	@Column(name = "GROSS_CLICK")
	private Integer gross_click;
	
	@Column(name = "COUNT_CLICK")
	private Integer count_click;
	
	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at; 

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at; 

//	public Integer getId() {
//		return id;
//	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getGross_click() {
		return gross_click;
	}

	public void setGross_click(Integer gross_click) {
		this.gross_click = gross_click;
	}

	public Integer getCount_click() {
		return count_click;
	}

	public void setCount_click(Integer count_click) {
		this.count_click = count_click;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
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
	
}
