package entity; 

import java.io.Serializable;
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

import spinwork.validations.Cnpj;
import spinwork.validations.Duplicate;
import spinwork.validations.Email;
import spinwork.validations.Max;
import spinwork.validations.NotNull;
import spinwork.validations.Size; 

@Entity
@Table(name = "stores")
@NamedQueries({
	@NamedQuery(
			name = "query_stores_count_total", 
			query = "SELECT COUNT(stores) FROM Stores stores "
					+ "WHERE "
					+ "stores.status = 1"),
	@NamedQuery(
			name = "query_stores_count_by_name_or_filial", 
			query = "SELECT COUNT(stores) FROM Stores stores "
					+ "WHERE "
					+ "(stores.name LIKE :name OR "
					+ "stores.filial LIKE :filial) AND "
					+ "stores.user_id.id = :user_id AND "
					+ "stores.status = 1"),
	@NamedQuery(
			name = "query_stores_count_by_url", 
			query = "SELECT COUNT(stores) FROM Stores stores "
					+ "WHERE "
					+ "stores.user_id.url LIKE :url AND "
					+ "stores.status = 1"),
	@NamedQuery(
			name = "query_stores_count_by_user", 
			query = "SELECT COUNT(stores) FROM Stores stores "
					+ "WHERE "
					+ "stores.user_id.id = :user_id AND "
					+ "stores.status = 1"),
	@NamedQuery(
			name = "query_stores_by_name_or_filial", 
			query = "FROM Stores stores "
					+ "WHERE "
					+ "(stores.name LIKE :name OR "
					+ "stores.filial LIKE :filial) AND "
					+ "stores.user_id.id = :user_id AND "
					+ "stores.status = 1 "
					+ "ORDER BY stores.city_id.name, stores.filial"),
	@NamedQuery(
			name = "query_stores_by_url", 
			query = "FROM Stores stores "
					+ "WHERE "
					+ "stores.user_id.url LIKE :url AND "
					+ "stores.status = 1"),
	@NamedQuery(
			name = "query_stores_by_user", 
			query = "FROM Stores stores "
					+ "WHERE "
					+ "stores.user_id.id = :user_id"),
	@NamedQuery(
			name = "query_stores_by_cnpj", 
			query = "FROM Stores stores "
					+ "WHERE "
					+ "stores.cnpj = :cnpj"),
//	@NamedQuery(
//			name = "query_stores_by_id", 
//			query = "FROM Stores aux "
//					+ "WHERE "
//					+ "aux.id = :store_id")
})

public final class Stores implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_COUNT_TOTAL             = "query_stores_count_total";
	public static final String QUERY_COUNT_BY_NAME_OR_FILIAL = "query_stores_count_by_name_or_filial";
	public static final String QUERY_COUNT_BY_URL            = "query_stores_count_by_url";
	public static final String QUERY_COUNT_BY_USER           = "query_stores_count_by_user";
	public static final String QUERY_BY_NAME_OR_FILIAL  = "query_stores_by_name_or_filial";
	public static final String QUERY_BY_URL                  = "query_stores_by_url";
	public static final String QUERY_BY_USER                 = "query_stores_by_user";
	public static final String QUERY_BY_CNPJ                 = "query_stores_by_cnpj";
//	public static final String QUERY_BY_ID                   = "query_stores_by_id";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 

	@NotNull(message = "store.fillFieldName")
	@Max(255)
	@Column(name = "NAME") 
	private String name; 

//	@NotNull(message = "store.fillFieldFilial")
	@Max(255)
	@Column(name = "FILIAL") 
	private String filial; 

	@NotNull(message = "store.fillFieldNeighborhood")
	@Max(255)
	@Column(name = "NEIGHBORHOOD") 
	private String neighborhood; 

	@NotNull(message = "store.fillFieldAddress")
	@Max(255)
	@Column(name = "ADDRESS") 
	private String address; 

	@NotNull(message = "store.fillFieldNumber")
	@Max(10)
	@Column(name = "NUMBER") 
	private String number; 

	@Max(255)
	@Column(name = "COMPLEMENT") 
	private String complement; 

	@NotNull(message = "store.fillFieldPostalCode")
	@Max(10)
	@Column(name = "POSTAL_CODE") 
	private String postal_code; 

//	@NotNull(message = "store.fillFieldLatitude")
	@Max(255)
	@Column(name = "LATITUDE") 
	private String latitude; 

//	@NotNull(message = "store.fillFieldLongitude")
	@Max(255)
	@Column(name = "LONGITUDE") 
	private String longitude; 

////	@NotNull(message = "store.fillFieldLatitude")
//	@Column(name = "LAT") 
//	private Double lat; 
//
////	@NotNull(message = "store.fillFieldLongitude")
//	@Column(name = "LNG") 
//	private Double lng; 

//	@NotNull(message = "store.fillFieldCnpj")
	@Size(min = 14, max = 18, message = "store.cnpjInvalid")
	@Cnpj(message = "store.cnpjInvalid")
	@Duplicate(namedQuery = QUERY_BY_CNPJ, message = "store.cnpjDuplicate")
	@Column(name = "CNPJ") 
	private String cnpj; 

	@Max(255)
	@Column(name = "STATE_REGISTRATION") 
	private String state_registration; 

	@Max(255)
	@Column(name = "COMPANY_NAME") 
	private String company_name; 

	@Max(255)
	@Column(name = "COMPANY_FANTASY_NAME") 
	private String company_fantasy_name; 

	@NotNull(message = "store.fillFieldContactPhone")
	@Max(20)
	@Column(name = "CONTACT_PHONE") 
	private String contact_phone; 

	@NotNull(message = "store.fillFieldContactEmail")
	@Max(100)
	@Email(message = "store.emailInvalid")
	@Column(name = "CONTACT_EMAIL") 
	private String contact_email; 

	@Max(255)
	@Column(name = "COMPANY_SLOGAN") 
	private String company_slogan; 

	@Type(type="text")
	@Column(name = "COMPANY_DESCRIPTION") 
	private String company_description; 
	
	@Column(name = "QTDA_IMAGES")
	private Integer qtda_images;
	
	@Column(name = "MAX_IMAGES")
	private Integer max_images;
	
	@Column(name = "IMAGE_FILE_NAME") 
	private String image_file_name; 

	@Column(name = "IMAGE_FILE_TYPE")
	private String image_file_type; 

	@Column(name = "STATUS") 
	private Integer status; 

	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at; 

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at; 

	@NotNull(message = "store.fillFieldCity")
	@JoinColumn(name = "CITY_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Cities city_id; 

	@JoinColumn(name = "USER_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user_id; 

//	@OneToMany(mappedBy = "store_id")
//	@OneToMany(mappedBy = "store_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
//	private List<AdvertisementStores> listAdvertisementStores;
	
	@OneToMany(mappedBy = "store_id")
//	@OneToMany(mappedBy = "store_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
	private List<AdvertisementStores> listAdvertisementStores;
	
//	@OneToMany(mappedBy = "store_id")
	@OneToMany(mappedBy = "store_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
	private List<StoreImages> listStoreImages;
	
//	// update: 0	delete: 0 
//	@OneToMany(mappedBy = "store_id") 
//	private Collection<Advertisements> advertisementsCollection;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilial() {
		return filial;
	}

	public void setFilial(String filial) {
		this.filial = filial;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
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

//	public Double getLat() {
//		return lat;
//	}
//
//	public void setLat(Double lat) {
//		this.lat = lat;
//	}
//
//	public Double getLng() {
//		return lng;
//	}
//
//	public void setLng(Double lng) {
//		this.lng = lng;
//	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getState_registration() {
		return state_registration;
	}

	public void setState_registration(String state_registration) {
		this.state_registration = state_registration;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCompany_fantasy_name() {
		return company_fantasy_name;
	}

	public void setCompany_fantasy_name(String company_fantasy_name) {
		this.company_fantasy_name = company_fantasy_name;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public String getContact_email() {
		return contact_email;
	}

	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}

	public String getCompany_slogan() {
		return company_slogan;
	}

	public void setCompany_slogan(String company_slogan) {
		this.company_slogan = company_slogan;
	}

	public String getCompany_description() {
		return company_description;
	}

	public void setCompany_description(String company_description) {
		this.company_description = company_description;
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

	public String getImage_file_name() {
		return image_file_name;
	}

	public void setImage_file_name(String image_file_name) {
		this.image_file_name = image_file_name;
	}

	public String getImage_file_type() {
		return image_file_type;
	}

	public void setImage_file_type(String image_file_type) {
		this.image_file_type = image_file_type;
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

	public Cities getCity_id() {
		return city_id;
	}

	public void setCity_id(Cities city_id) {
		this.city_id = city_id;
	}

	public Users getUser_id() {
		return user_id;
	}

	public void setUser_id(Users user_id) {
		this.user_id = user_id;
	}

	public List<AdvertisementStores> getListAdvertisementStores() {
		return listAdvertisementStores;
	}

	public void setListAdvertisementStores(List<AdvertisementStores> listAdvertisementStores) {
		this.listAdvertisementStores = listAdvertisementStores;
	}

	public List<StoreImages> getListStoreImages() {
		return listStoreImages;
	}

	public void setListStoreImages(List<StoreImages> listStoreImages) {
		this.listStoreImages = listStoreImages;
	}

//	public Collection<Advertisements> getAdvertisementsCollection() {
//		return advertisementsCollection;
//	}
//
//	public void setAdvertisementsCollection(Collection<Advertisements> advertisementsCollection) {
//		this.advertisementsCollection = advertisementsCollection;
//	} 

} 

