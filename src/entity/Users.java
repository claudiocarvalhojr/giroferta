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

import spinwork.validations.Cnpj;
import spinwork.validations.Cpf;
import spinwork.validations.Duplicate;
import spinwork.validations.Email;
import spinwork.validations.Max;
import spinwork.validations.NotNull;
import spinwork.validations.NotNullWithException;
import spinwork.validations.Size;
import spinwork.validations.Url; 

@Entity
@Table(name = "users")
@NamedQueries({
	@NamedQuery(
			name = "query_users_import_all", 
			query = "FROM Users user WHERE user.status = 1"),
	@NamedQuery(
			name = "query_users_import_all_by_user", 
			query = " FROM Users user "
					+ " WHERE "
					+ " user.id = :user_id AND "
					+ " user.status = 1 "),
	@NamedQuery(
			name = "query_users_import_xml", 
			query = " FROM Users user "
					+ " WHERE "
					+ " user.url_xml <> '' AND "
					+ " user.type_xml <> 0 AND "
					+ " user.import_xml = 1 AND "
					+ " user.status = 1 "),
	@NamedQuery(
			name = "query_users_import_xml_by_user", 
			query = " FROM Users user "
					+ " WHERE "
					+ " user.id = :user_id AND "
					+ " user.url_xml <> '' AND "
					+ " user.type_xml <> 0 AND "
					+ " user.import_xml = 1 AND "
					+ " user.status = 1 "),
	@NamedQuery(
			name = "query_users_by_login", 
			query = "FROM Users user WHERE user.email = :email AND user.encrypted_password = :encrypted_password"),
	@NamedQuery(
			name = "query_users_by_email", 
			query = "FROM Users user WHERE user.email = :email"),
	@NamedQuery(
			name = "query_users_by_idfacebook", 
			query = "FROM Users user WHERE user.email = :email AND user.id_facebook = :id_facebook"),
	@NamedQuery(
			name = "query_users_by_idgoogle", 
			query = "FROM Users user WHERE user.email = :email AND user.id_google = :id_google"),
	@NamedQuery(
			name = "query_users_list_all_url_active", 
			query = "SELECT user.url "
					+ "FROM Users user "
					+ "WHERE "
					+ "user.status = 1 AND "
					+ "user.url <> '' "
					+ "ORDER BY user.url"),
	@NamedQuery(
			name = "query_users_by_url", 
			query = "FROM Users user WHERE user.url = :url"),
	@NamedQuery(
			name = "query_users_by_cpf", 
			query = "FROM Users user WHERE user.cpf = :cpf"),
	@NamedQuery(
			name = "query_users_by_cnpj", 
			query = "FROM Users user WHERE user.cnpj = :cnpj"),
	@NamedQuery(
			name = "query_users_by_token", 
			query = "FROM Users user WHERE user.reset_password_token = :reset_password_token"),
	@NamedQuery(
			name = "query_users_by_date_unconfirmed", 
			query = "FROM Users user WHERE user.status = 0 AND user.updated_at BETWEEN :initialDate AND :currentDate"),
	@NamedQuery(
			name = "query_users_activation", 
			query = "FROM Users user WHERE user.reset_password_token = :reset_password_token AND user.status = 0"),
	@NamedQuery(
			name = "query_users_count_by_vendor", 
			query = "SELECT COUNT(user) "
					+ "FROM Users user WHERE vendor_id.id = :user_id"),
	@NamedQuery(
			name = "query_users_by_vendor", 
			query = "FROM Users user WHERE vendor_id.id = :user_id"),
	@NamedQuery(
			name = "query_stores_home", 
			query = "FROM Users user "
					+ " WHERE "
//					+ " user.type_person = 'J' AND "
					+ " user.image_file_name <> '' AND "
					+ " user.balance > user.click_values_id.value AND "
					+ " user.status = 1"
					+ " ORDER BY RAND() ")
})

public final class Users implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String QUERY_IMPORT_ALL          = "query_users_import_all";
	public static final String QUERY_IMPORT_ALL_BY_USER  = "query_users_import_all_by_user";
	public static final String QUERY_IMPORT_XML          = "query_users_import_xml";
	public static final String QUERY_IMPORT_XML_BY_USER  = "query_users_import_xml_by_user";
	public static final String QUERY_BY_LOGIN            = "query_users_by_login";
	public static final String QUERY_BY_EMAIL            = "query_users_by_email";
	public static final String QUERY_BY_IDFACEBOOK       = "query_users_by_idfacebook";
	public static final String QUERY_BY_IDGOOGLE         = "query_users_by_idgoogle";
	public static final String QUERY_LIST_ALL_URL_ACTIVE = "query_users_list_all_url_active";
	public static final String QUERY_BY_URL              = "query_users_by_url";
	public static final String QUERY_BY_CPF              = "query_users_by_cpf";
	public static final String QUERY_BY_CNPJ             = "query_users_by_cnpj";
	public static final String QUERY_BY_TOKEN            = "query_users_by_token";
	public static final String QUERY_BY_DATE_UNCONFIRMED = "query_users_by_date_unconfirmed";
	public static final String QUERY_ACTIVATION          = "query_users_activation";
	public static final String QUERY_COUNT_BY_VENDOR     = "query_users_count_by_vendor";
	public static final String QUERY_BY_VENDOR           = "query_users_by_vendor";
	public static final String QUERY_STORES_HOME         = "query_stores_home";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 
	
	@NotNull(message = "user.fillFieldEmail")
	@Max(100)
	@Email(message = "user.emailInvalid")
	@Duplicate(namedQuery = QUERY_BY_EMAIL, message = "user.emailDuplicate")
	@Column(name = "EMAIL", nullable = false)
	private String email;

	@Max(50)
	@Column(name = "ID_FACEBOOK")
	private String id_facebook;

	@Max(50)
	@Column(name = "ID_GOOGLE")
	private String id_google;

	@JoinColumn(name = "CLICK_VALUES_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private ClickValues click_values_id;

	@NotNull
	@Max(255)
	@Column(name = "ENCRYPTED_PASSWORD")
	private String encrypted_password;

	@NotNull(message = "user.fillFieldUrl")
	@Max(100)
	@Url(message = "user.urlInvalid")
	@Duplicate(namedQuery = QUERY_BY_URL, message = "user.urlDuplicate")
	@Column(name = "URL") 
	private String url; 
	
	@NotNullWithException(exception = "pf", message = "user.fillFieldCompanyName")
	@Max(255)
	@Column(name = "COMPANY_NAME")
	private String company_name;

	@NotNullWithException(exception = "pf", message = "user.fillFieldContactPerson")
	@Max(255)
	@Column(name = "CONTACT_PERSON")
	private String contact_person;
	
	@Max(1)
	@Column(name = "TYPE_PERSON")
	private String type_person;

	@NotNullWithException(exception = "pj", message = "user.fillFieldName")
	@Max(255)
	@Column(name = "NAME") 
	private String name; 

	@NotNullWithException(exception = "pj", message = "user.fillFieldLastName")
	@Max(255)
	@Column(name = "LAST_NAME") 
	private String last_name; 

	@NotNullWithException(exception = "pj", message = "user.fillFieldCpf")
	@Size(min = 11, max = 14, message = "user.cpfInvalid")
	@Cpf(message = "user.cpfInvalid")
	@Duplicate(namedQuery = QUERY_BY_CPF, message = "user.cpfDuplicate")
	@Column(name = "CPF") 
	private String cpf; 

	@NotNullWithException(exception = "pf", message = "user.fillFieldCnpj")
	@Size(min = 14, max = 18, message = "user.cnpjInvalid")
	@Cnpj(message = "user.cnpjInvalid")
	@Duplicate(namedQuery = QUERY_BY_CNPJ, message = "user.cnpjDuplicate")
	@Column(name = "CNPJ") 
	private String cnpj; 

//	@NotNullWithException(exception = "pf", message = "user.fillFieldStateRegistration")
	@Max(25)
	@Column(name = "STATE_REGISTRATION") 
	private String state_registration; 

//	@NotNull(message = "user.fillFieldPhone")
	@Max(20)
	@Column(name = "PHONE") 
	private String phone; 

	@Max(20)
	@Column(name = "SECOND_PHONE") 
	private String second_phone; 

//	@NotNullWithException(exception = "pj", message = "user.fillFieldBirthdate")
	@Column(name = "BIRTHDATE") 
	@Temporal(TemporalType.DATE) 
	private Date birthdate; 

//	@NotNullWithException(exception = "pj", message = "user.fillFieldGender")
	@Column(name = "GENDER") 
	private String gender; 

	@Column(name = "TYPE_ACCOUNT") 
	private Integer type_account; 
/*
	@NotNull(message = "user.fillFieldNeighborhood")
	@Max(255)
	@Column(name = "NEIGHBORHOOD") 
	private String neighborhood; 

	@NotNull(message = "user.fillFieldAddress")
	@Max(255)
	@Column(name = "ADDRESS") 
	private String address; 

	@NotNull(message = "user.fillFieldNumber")
	@Max(10)
	@Column(name = "NUMBER") 
	private String number; 

	@Max(255)
	@Column(name = "COMPLEMENT") 
	private String complement; 

	@NotNull(message = "user.fillFieldPostalCode")
	@Max(10)
	@Column(name = "POSTAL_CODE") 
	private String postal_code; 
*/
	@Column(name = "EMAIL_RECEIVE") 
	private Integer email_receive; 

	@Column(name = "TERMS_AGREE") 
	private Integer terms_agree; 

	@Column(name = "RESET_PASSWORD_TOKEN") 
	private String reset_password_token; 

	@Column(name = "RESET_PASSWORD_SENT_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date reset_password_sent_at; 

	@Column(name = "REMENBER_CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date remenber_created_at; 

	@Column(name = "SIGN_IN_COUNT") 
	private Integer sign_in_count; 

	@Column(name = "CURRENT_SIGN_IN_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date current_sign_in_at; 

	@Column(name = "LAST_SIGN_IN_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date last_sign_in_at; 

	@Column(name = "CURRENT_SIGN_IN_IP") 
	private String current_sign_in_ip; 

	@Column(name = "LAST_SIGN_IN_IP") 
	private String last_sign_in_ip; 
	
	@Max(255)	
	@Column(name = "LINK")
	private String link;
	
	@Max(255)	
	@Column(name = "URL_XML")
	private String url_xml;
	
	@Column(name = "TYPE_XML")
	private Integer type_xml;
	
	@Column(name = "IMPORT_XML")
	private Integer import_xml;
	
	@Column(name = "PARTNER")
	private Integer partner;
	
	@Column(name = "GROSS_CLICK")
	private Integer gross_click;
	
	@Column(name = "EXTRA")
	private BigDecimal extra;
	
	@Column(name = "BALANCE")
	private BigDecimal balance;
	
	@Column(name = "IMAGE_FILE_NAME") 
	private String image_file_name; 

	@Column(name = "IMAGE_FILE_TYPE")
	private String image_file_type; 

	@Column(name = "COUNT_STORES") 
	private Integer count_stores; 

	@Column(name = "COUNT_ADVERTISEMENTS") 
	private Integer count_advertisements; 

	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Users vendor_id; 
	
	@Column(name = "STATUS") 
	private Integer status; 

	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at; 

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at; 

/*
	@NotNull(message = "user.fillFieldCity")
	@JoinColumn(name = "CITY_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Cities city_id; 
*/
	
//	@OneToMany(mappedBy = "user_id")
//	@OneToMany(mappedBy = "user_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
//	private List<Advertisements> listAdvertisements;
	
//	@OneToMany(mappedBy = "user_id")
//	@OneToMany(mappedBy = "user_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
//	private List<Stores> listStores;
	
//	// update: 0	delete: 0 
//	@OneToMany(mappedBy = "user_id") 
//	private Collection<Advertisements> advertisementsCollection; 
//
//	// update: 0	delete: 0 
//	@OneToMany(mappedBy = "user_id") 
//	private Collection<Stores> storesCollection;

//	@OneToMany(mappedBy = "user_id")
	@OneToMany(mappedBy = "user_id", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
	private List<Stores> listStores;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId_facebook() {
		return id_facebook;
	}

	public void setId_facebook(String id_facebook) {
		this.id_facebook = id_facebook;
	}

	public String getId_google() {
		return id_google;
	}

	public void setId_google(String id_google) {
		this.id_google = id_google;
	}

	public ClickValues getClick_values_id() {
		return click_values_id;
	}

	public void setClick_values_id(ClickValues click_values_id) {
		this.click_values_id = click_values_id;
	}

	public String getEncrypted_password() {
		return encrypted_password;
	}

	public void setEncrypted_password(String encrypted_password) {
		this.encrypted_password = encrypted_password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getContact_person() {
		return contact_person;
	}

	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getType_person() {
		return type_person;
	}

	public void setType_person(String type_person) {
		this.type_person = type_person;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSecond_phone() {
		return second_phone;
	}

	public void setSecond_phone(String second_phone) {
		this.second_phone = second_phone;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getType_account() {
		return type_account;
	}

	public void setType_account(Integer type_account) {
		this.type_account = type_account;
	}
/*
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
*/
	public Integer getEmail_receive() {
		return email_receive;
	}

	public void setEmail_receive(Integer email_receive) {
		this.email_receive = email_receive;
	}

	public Integer getTerms_agree() {
		return terms_agree;
	}

	public void setTerms_agree(Integer terms_agree) {
		this.terms_agree = terms_agree;
	}

	public String getReset_password_token() {
		return reset_password_token;
	}

	public void setReset_password_token(String reset_password_token) {
		this.reset_password_token = reset_password_token;
	}

	public Date getReset_password_sent_at() {
		return reset_password_sent_at;
	}

	public void setReset_password_sent_at(Date reset_password_sent_at) {
		this.reset_password_sent_at = reset_password_sent_at;
	}

	public Date getRemenber_created_at() {
		return remenber_created_at;
	}

	public void setRemenber_created_at(Date remenber_created_at) {
		this.remenber_created_at = remenber_created_at;
	}

	public Integer getSign_in_count() {
		return sign_in_count;
	}

	public void setSign_in_count(Integer sign_in_count) {
		this.sign_in_count = sign_in_count;
	}

	public Date getCurrent_sign_in_at() {
		return current_sign_in_at;
	}

	public void setCurrent_sign_in_at(Date current_sign_in_at) {
		this.current_sign_in_at = current_sign_in_at;
	}

	public Date getLast_sign_in_at() {
		return last_sign_in_at;
	}

	public void setLast_sign_in_at(Date last_sign_in_at) {
		this.last_sign_in_at = last_sign_in_at;
	}

	public String getCurrent_sign_in_ip() {
		return current_sign_in_ip;
	}

	public void setCurrent_sign_in_ip(String current_sign_in_ip) {
		this.current_sign_in_ip = current_sign_in_ip;
	}

	public String getLast_sign_in_ip() {
		return last_sign_in_ip;
	}

	public void setLast_sign_in_ip(String last_sign_in_ip) {
		this.last_sign_in_ip = last_sign_in_ip;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getUrl_xml() {
		return url_xml;
	}

	public void setUrl_xml(String url_xml) {
		this.url_xml = url_xml;
	}

	public Integer getType_xml() {
		return type_xml;
	}

	public void setType_xml(Integer type_xml) {
		this.type_xml = type_xml;
	}

	public Integer getImport_xml() {
		return import_xml;
	}

	public void setImport_xml(Integer import_xml) {
		this.import_xml = import_xml;
	}

	public Integer getPartner() {
		return partner;
	}

	public void setPartner(Integer partner) {
		this.partner = partner;
	}

	public Integer getGross_click() {
		return gross_click;
	}

	public void setGross_click(Integer gross_click) {
		this.gross_click = gross_click;
	}

	public BigDecimal getExtra() {
		return extra;
	}

	public void setExtra(BigDecimal extra) {
		this.extra = extra;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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

	public Integer getCount_stores() {
		return count_stores;
	}

	public void setCount_stores(Integer count_stores) {
		this.count_stores = count_stores;
	}

	public Integer getCount_advertisements() {
		return count_advertisements;
	}

	public void setCount_advertisements(Integer count_advertisements) {
		this.count_advertisements = count_advertisements;
	}

	public Users getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(Users vendor_id) {
		this.vendor_id = vendor_id;
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

	public List<Stores> getListStores() {
		return listStores;
	}

	public void setListStores(List<Stores> listStores) {
		this.listStores = listStores;
	}

//	public List<Advertisements> getListAdvertisements() {
//		return listAdvertisements;
//	}
//
//	public void setListAdvertisements(List<Advertisements> listAdvertisements) {
//		this.listAdvertisements = listAdvertisements;
//	}
//
//	public List<Stores> getListStores() {
//		return listStores;
//	}
//
//	public void setListStores(List<Stores> listStores) {
//		this.listStores = listStores;
//	}

/*
	public Cities getCity_id() {
		return city_id;
	}

	public void setCity_id(Cities city_id) {
		this.city_id = city_id;
	}
*/
//	public Collection<Advertisements> getAdvertisementsCollection() {
//		return advertisementsCollection;
//	}
//
//	public void setAdvertisementsCollection(Collection<Advertisements> advertisementsCollection) {
//		this.advertisementsCollection = advertisementsCollection;
//	}
//
//	public Collection<Stores> getStoresCollection() {
//		return storesCollection;
//	}
//
//	public void setStoresCollection(Collection<Stores> storesCollection) {
//		this.storesCollection = storesCollection;
//	} 

} 

