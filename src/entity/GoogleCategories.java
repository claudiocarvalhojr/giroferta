package entity; 

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType; 

@Entity
@Table(name = "google_categories")
@NamedQuery(
		name = "query_google_categories_list_all_with_ordination", 
		query = "FROM GoogleCategories aux WHERE " +
				"aux.first_category LIKE :categoria OR " +
				"aux.second_category LIKE :categoria OR " +
				"aux.third_category LIKE :categoria OR " +
				"aux.fourth_category LIKE :categoria OR " +
				"aux.fifth_category LIKE :categoria OR " +
				"aux.sixth_category LIKE :categoria OR " +
				"aux.seventh_category LIKE :categoria " +
				"ORDER BY aux.first_category ASC")

public final class GoogleCategories implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_LIST_ALL_WITH_ORDINATION = "query_google_categories_list_all_with_ordination";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 

	@Column(name = "FIRST_CATEGORY") 
	private String first_category; 

	@Column(name = "SECOND_CATEGORY") 
	private String second_category; 

	@Column(name = "THIRD_CATEGORY") 
	private String third_category; 

	@Column(name = "FOURTH_CATEGORY") 
	private String fourth_category; 

	@Column(name = "FIFTH_CATEGORY") 
	private String fifth_category; 

	@Column(name = "SIXTH_CATEGORY") 
	private String sixth_category; 

	@Column(name = "SEVENTH_CATEGORY") 
	private String seventh_category; 

	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at; 

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at; 

//	// update: 0	delete: 0 
//	@OneToMany(mappedBy = "google_category_id") 
//	private Collection<Advertisements> advertisementsCollection;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirst_category() {
		return first_category;
	}

	public void setFirst_category(String first_category) {
		this.first_category = first_category;
	}

	public String getSecond_category() {
		return second_category;
	}

	public void setSecond_category(String second_category) {
		this.second_category = second_category;
	}

	public String getThird_category() {
		return third_category;
	}

	public void setThird_category(String third_category) {
		this.third_category = third_category;
	}

	public String getFourth_category() {
		return fourth_category;
	}

	public void setFourth_category(String fourth_category) {
		this.fourth_category = fourth_category;
	}

	public String getFifth_category() {
		return fifth_category;
	}

	public void setFifth_category(String fifth_category) {
		this.fifth_category = fifth_category;
	}

	public String getSixth_category() {
		return sixth_category;
	}

	public void setSixth_category(String sixth_category) {
		this.sixth_category = sixth_category;
	}

	public String getSeventh_category() {
		return seventh_category;
	}

	public void setSeventh_category(String seventh_category) {
		this.seventh_category = seventh_category;
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

//	public Collection<Advertisements> getAdvertisementsCollection() {
//		return advertisementsCollection;
//	}
//
//	public void setAdvertisementsCollection(Collection<Advertisements> advertisementsCollection) {
//		this.advertisementsCollection = advertisementsCollection;
//	} 

} 

