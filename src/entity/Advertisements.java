package entity; 

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import spinwork.validations.Max;
import spinwork.validations.NotNull; 

@Entity
@Table(name = "advertisements")

@NamedQueries({
	@NamedQuery(
			name = "query_advertisements_count_total", 
			query = "SELECT COUNT(advertisements) "
					+ "FROM Advertisements advertisements"),
	@NamedQuery(
			name = "query_advertisements_count_total_published", 
			query = "SELECT COUNT(advertisements) "
					+ "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.status = 2"),
	@NamedQuery(
			name = "query_advertisements_count_total_by_user", 
			query = "SELECT COUNT(advertisements) "
					+ "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3)"),
	@NamedQuery(
			name = "query_advertisements_count_by_title", 
			query = "SELECT COUNT(advertisements) "
					+ "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "advertisements.title LIKE :title AND "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3)"),
	@NamedQuery(
			name = "query_advertisements_count_by_title_and_brand_with_store", 
			query = " SELECT COUNT(advertisements) "
					+ " FROM Advertisements advertisements "
					+ " WHERE "
					+ " (advertisements.title LIKE :value_search OR "
					+ " advertisements.brand LIKE :value_search) AND "
					+ " advertisements.status = 2 AND "
					+ " advertisements.user_id.url = :user_url "),
	@NamedQuery(
			name = "query_advertisements_count_by_title_and_brand", 
			query = " SELECT COUNT(advertisements) "
					+ " FROM Advertisements advertisements "
					+ " WHERE "
					+ " (advertisements.title LIKE :value_search OR "
					+ " advertisements.brand LIKE :value_search) AND "
					+ " advertisements.status = 2 "),
	@NamedQuery(
			name = "query_advertisements_count_report_by_user", 
			query = "SELECT COUNT(advertisements) "
					+ "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "),
	@NamedQuery(
			name = "query_advertisements_count_report", 
			query = "SELECT COUNT(advertisements) "
					+ "FROM Advertisements advertisements "
					+ "WHERE "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "),
	@NamedQuery(
			name = "query_advertisements_count_published_by_url", 
			query = "SELECT COUNT(advertisements) "
					+ "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.url LIKE :url AND "
					+ "advertisements.status = 2"),
	@NamedQuery(
			name = "query_advertisements_published_by_user", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "advertisements.status = 2"),
	@NamedQuery(
			name = "query_advertisements_list_by_title", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "advertisements.title LIKE :title AND "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "
					+ "ORDER BY advertisements.title"),
	@NamedQuery(
			name = "query_advertisements_list_by_title_and_brand_with_store", 
			query = " FROM Advertisements advertisements "
					+ " WHERE "
					+ " (advertisements.title LIKE :value_search OR "
					+ " advertisements.brand LIKE :value_search) AND "
//					+ " advertisements.qtda_images > 0 AND "
					+ " advertisements.status = 2 AND "
					+ " advertisements.user_id.url = :user_url "
					+ " ORDER BY advertisements.title ASC "),
	@NamedQuery(
			name = "query_advertisements_list_by_title_and_brand", 
			query = " FROM Advertisements advertisements "
					+ " WHERE "
					+ " (advertisements.title LIKE :value_search OR "
					+ " advertisements.brand LIKE :value_search) AND "
//					+ " advertisements.qtda_images > 0 AND "
					+ " advertisements.status = 2 "
					+ " ORDER BY advertisements.title ASC "),
	@NamedQuery(
			name = "query_advertisements_list_by_gross_click_and_user", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "
					+ "ORDER BY advertisements.gross_click DESC"),
	@NamedQuery(
			name = "query_advertisements_list_by_count_click_and_user", 
			query = " FROM Advertisements advertisements "
					+ " WHERE "
					+ " advertisements.user_id.id = :user_id AND "
					+ " (advertisements.status = 1 OR "
					+ " advertisements.status = 2 OR "
					+ " advertisements.status = 3) "
					+ " ORDER BY advertisements.count_click DESC"),
	@NamedQuery(
			name = "query_advertisements_list_by_purchase_and_user", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "
					+ "ORDER BY advertisements.listHistory_purchases.size DESC"),
	@NamedQuery(
			name = "query_advertisements_list_by_most_accessed_and_user", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "
					+ "ORDER BY advertisements.listHistory_most_accessed.size DESC"),
	@NamedQuery(
			name = "query_advertisements_list_by_recently_added_and_user", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "
					+ "ORDER BY advertisements.listHistory_recently_added.size DESC"),
	@NamedQuery(
			name = "query_advertisements_list_by_gross_click", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "
					+ "ORDER BY advertisements.gross_click DESC"),
	@NamedQuery(
			name = "query_advertisements_list_by_count_click", 
			query = " FROM Advertisements advertisements "
					+ " WHERE "
					+ " (advertisements.status = 1 OR "
					+ " advertisements.status = 2 OR "
					+ " advertisements.status = 3) "
					+ " ORDER BY advertisements.count_click DESC"),
	@NamedQuery(
			name = "query_advertisements_list_by_purchase", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "
					+ "ORDER BY advertisements.listHistory_purchases.size DESC"),
	@NamedQuery(
			name = "query_advertisements_list_by_most_accessed", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "
					+ "ORDER BY advertisements.listHistory_most_accessed.size DESC"),
	@NamedQuery(
			name = "query_advertisements_list_by_recently_added", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "(advertisements.status = 1 OR "
					+ "advertisements.status = 2 OR "
					+ "advertisements.status = 3) "
					+ "ORDER BY advertisements.listHistory_recently_added.size DESC"),
	@NamedQuery(
			name = "query_advertisements_by_reference", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "advertisements.reference = :reference"),
	@NamedQuery(
			name = "query_advertisements_by_id", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.id = :advertisement_id"),
	@NamedQuery(
			name = "query_advertisements_by_user", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id"),
	@NamedQuery(
			name = "query_advertisements_by_user_and_all_stores", 
			query = "FROM Advertisements advertisements "
					+ "WHERE "
					+ "advertisements.user_id.id = :user_id AND "
					+ "advertisements.all_stores = 1"),
	@NamedQuery(
			name = "query_advertisements_list_all", 
			query = "FROM Advertisements advertisements"),
	@NamedQuery(
			name = "query_advertisements_most_accessed", 
			query = " FROM Advertisements advertisements "
					+ " WHERE "
					+ " advertisements.user_id.id = :user_id AND "
					+ " advertisements.qtda_images > 0 AND "
					+ " advertisements.status = 2 "
					+ " ORDER BY advertisements.count_click DESC "),
	@NamedQuery(
			name = "query_advertisements_recently_added", 
			query = " FROM Advertisements advertisements "
					+ " WHERE "
					+ " advertisements.user_id.id = :user_id AND "
					+ " advertisements.qtda_images > 0 AND "
					+ " advertisements.status = 2 "
					+ " ORDER BY advertisements.created_at DESC ")
})

public final class Advertisements implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_COUNT_TOTAL      		              = "query_advertisements_count_total";
	public static final String QUERY_COUNT_TOTAL_PUBLISHED                = "query_advertisements_count_total_published";
	public static final String QUERY_COUNT_TOTAL_BY_USER                  = "query_advertisements_count_total_by_user";
	public static final String QUERY_COUNT_BY_TITLE                       = "query_advertisements_count_by_title";
	public static final String QUERY_COUNT_BY_TITLE_AND_BRAND_WITH_STORE  = "query_advertisements_count_by_title_and_brand_with_store";
	public static final String QUERY_COUNT_BY_TITLE_AND_BRAND             = "query_advertisements_count_by_title_and_brand";
	public static final String QUERY_COUNT_REPORT_AND_USER                = "query_advertisements_count_report_by_user";
	public static final String QUERY_COUNT_REPORT                         = "query_advertisements_count_report";
	public static final String QUERY_COUNT_BY_PUBLISHED_BY_URL            = "query_advertisements_count_published_by_url";
	public static final String QUERY_PUBLISHED_BY_USER                    = "query_advertisements_published_by_user";
	public static final String QUERY_LIST_BY_TITLE                        = "query_advertisements_list_by_title";
	public static final String QUERY_LIST_BY_TITLE_AND_BRAND_WITH_STORE   = "query_advertisements_list_by_title_and_brand_with_store";
	public static final String QUERY_LIST_BY_TITLE_AND_BRAND              = "query_advertisements_list_by_title_and_brand";
	public static final String QUERY_LIST_BY_GROSS_CLICK_AND_USER         = "query_advertisements_list_by_gross_click_and_user";
	public static final String QUERY_LIST_BY_COUNT_CLICK_AND_USER         = "query_advertisements_list_by_count_click_and_user";
	public static final String QUERY_LIST_BY_PURCHASE_AND_USER            = "query_advertisements_list_by_purchase_and_user";
	public static final String QUERY_LIST_BY_MOST_ACCESSED_AND_USER       = "query_advertisements_list_by_most_accessed_and_user";
	public static final String QUERY_LIST_BY_RECENTLY_ADDED_AND_USER      = "query_advertisements_list_by_recently_added_and_user";
	public static final String QUERY_LIST_BY_GROSS_CLICK                  = "query_advertisements_list_by_gross_click";
	public static final String QUERY_LIST_BY_COUNT_CLICK                  = "query_advertisements_list_by_count_click";
	public static final String QUERY_LIST_BY_PURCHASE                     = "query_advertisements_list_by_purchase";
	public static final String QUERY_LIST_BY_MOST_ACCESSED                = "query_advertisements_list_by_most_accessed";
	public static final String QUERY_LIST_BY_RECENTLY_ADDED               = "query_advertisements_list_by_recently_added";
	public static final String QUERY_BY_REFERENCE                         = "query_advertisements_by_reference";
	public static final String QUERY_BY_USER                              = "query_advertisements_by_user";
	public static final String QUERY_BY_ID                                = "query_advertisements_by_id";
	public static final String QUERY_BY_USER_AND_ALL_STORES               = "query_advertisements_by_user_and_all_stores";
	public static final String QUERY_LIST_ALL				              = "query_advertisements_list_all";
	public static final String QUERY_MOST_ACCESSED			              = "query_advertisements_most_accessed";
	public static final String QUERY_RECENTLY_ADDED			              = "query_advertisements_recently_added";
	
	public Advertisements() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 

	@Column(name = "URL")
	private String url;
	
	@Column(name = "REFERENCE")
	private String reference;
	
	@NotNull(message = "advertisement.fillFieldTitle")
	@Max(255)
	@Column(name = "TITLE") 
	private String title; 

	@NotNull(message = "advertisement.fillFieldDescription")
	@Type(type="text")
	@Column(name = "DESCRIPTION") 
	private String description; 

//	@NotNull(message = "advertisement.fillFieldPrice")
	@Column(name = "PRICE") 
	private BigDecimal price; 

	@Max(255)
	@Column(name = "ITEM_STATE") 
	private String item_state; 

	@Max(255)
	@Column(name = "AVAILABILITY") 
	private String availability; 

	@Max(255)
	@Column(name = "BRAND") 
	private String brand; 
	
	@Max(1000)
	@Column(name = "LINK") 
	private String link; 

	@Column(name = "ALL_STORES")
	private Integer all_stores;
	
	@Column(name = "BALANCE")
	private BigDecimal balance;
	
	@Column(name = "QTDA_IMAGES")
	private Integer qtda_images;
	
	@Column(name = "MAX_IMAGES")
	private Integer max_images;
	
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

//	@NotNull(message = "advertisement.fillFieldGoogleCategory")
	@JoinColumn(name = "GOOGLE_CATEGORY_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private GoogleCategories google_category_id; 

	@JoinColumn(name = "USER_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user_id; 

	@JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Categories category_id; 

	@OneToMany(mappedBy = "advertisement_id")
//	@OneToMany(mappedBy = "advertisement_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
	private List<AdvertisementStores> listAdvertisementStores;
	
//	@OneToMany(mappedBy = "advertisement_id")
	@OneToMany(mappedBy = "advertisement_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
	private List<AdvertisementImages> listAdvertisementImages;

//	@OneToMany(mappedBy = "advertisement_id")
	@OneToMany(mappedBy = "advertisement_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
	private List<HistoryCountClick> listHistory_count_click;

//	@OneToMany(mappedBy = "advertisement_id")
	@OneToMany(mappedBy = "advertisement_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
	private List<HistoryMostAccessed> listHistory_most_accessed;

//	@OneToMany(mappedBy = "advertisement_id")
	@OneToMany(mappedBy = "advertisement_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
	private List<HistoryPurchases> listHistory_purchases;
	
//	@OneToMany(mappedBy = "advertisement_id")
	@OneToMany(mappedBy = "advertisement_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
	private List<HistoryRecentlyAdded> listHistory_recently_added;


//	// update: 0	delete: 0 
//	@OneToMany(mappedBy = "advertisement_id") 
//	private Collection<AdvertisementImages> advertisementImagesCollection;

	public Integer getId() {
		return id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getItem_state() {
		return item_state;
	}

	public void setItem_state(String item_state) {
		this.item_state = item_state;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getAll_stores() {
		return all_stores;
	}

	public void setAll_stores(Integer all_stores) {
		this.all_stores = all_stores;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getQtda_images() {
		return qtda_images;
	}

	public void setQtda_images(Integer qtda_images) {
		this.qtda_images = qtda_images;
	}

	public Integer getMax_images() {
		return max_images;
	}

	public void setMax_images(Integer max_images) {
		this.max_images = max_images;
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

	public GoogleCategories getGoogle_category_id() {
		return google_category_id;
	}

	public void setGoogle_category_id(GoogleCategories google_category_id) {
		this.google_category_id = google_category_id;
	}

	public Users getUser_id() {
		return user_id;
	}

	public void setUser_id(Users user_id) {
		this.user_id = user_id;
	}

	public Categories getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Categories category_id) {
		this.category_id = category_id;
	}

	public List<AdvertisementStores> getListAdvertisementStores() {
		return listAdvertisementStores;
	}

	public void setListAdvertisementStores(List<AdvertisementStores> listAdvertisementStores) {
		this.listAdvertisementStores = listAdvertisementStores;
	}

	public List<AdvertisementImages> getListAdvertisementImages() {
		return listAdvertisementImages;
	}

	public void setListAdvertisementImages(List<AdvertisementImages> listAdvertisementImages) {
		this.listAdvertisementImages = listAdvertisementImages;
	}

	public List<HistoryCountClick> getListHistory_count_click() {
		return listHistory_count_click;
	}

	public void setListHistory_count_click(List<HistoryCountClick> listHistory_count_click) {
		this.listHistory_count_click = listHistory_count_click;
	}

	public List<HistoryMostAccessed> getListHistory_most_accessed() {
		return listHistory_most_accessed;
	}

	public void setListHistory_most_accessed(List<HistoryMostAccessed> listHistory_most_accessed) {
		this.listHistory_most_accessed = listHistory_most_accessed;
	}

	public List<HistoryPurchases> getListHistory_purchases() {
		return listHistory_purchases;
	}

	public void setListHistory_purchases(List<HistoryPurchases> listHistory_purchases) {
		this.listHistory_purchases = listHistory_purchases;
	}

	public List<HistoryRecentlyAdded> getListHistory_recently_added() {
		return listHistory_recently_added;
	}

	public void setListHistory_recently_added(List<HistoryRecentlyAdded> listHistory_recently_added) {
		this.listHistory_recently_added = listHistory_recently_added;
	}

//	public Collection<AdvertisementImages> getAdvertisementImagesCollection() {
//		return advertisementImagesCollection;
//	}
//
//	public void setAdvertisementImagesCollection(Collection<AdvertisementImages> advertisementImagesCollection) {
//		this.advertisementImagesCollection = advertisementImagesCollection;
//	} 

} 

