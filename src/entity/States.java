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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType; 

@Entity
@Table(name = "states")
@NamedQuery(
		name = "query_states_list_all_with_ordination", 
		query = "FROM States aux ORDER BY aux.initials")

public final class States implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_LIST_ALL_WITH_ORDINATION = "query_states_list_all_with_ordination";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 

	@Column(name = "NAME") 
	private String name; 

	@Column(name = "INITIALS") 
	private String initials; 

	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at; 

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at; 

	@JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Countries country_id; 

//	// update: 0	delete: 0 
//	@OneToMany(mappedBy = "state_id") 
//	private Collection<Cities> citiesCollection;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
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

	public Countries getCountry_id() {
		return country_id;
	}

	public void setCountry_id(Countries country_id) {
		this.country_id = country_id;
	}

//	public Collection<Cities> getCitiesCollection() {
//		return citiesCollection;
//	}
//
//	public void setCitiesCollection(Collection<Cities> citiesCollection) {
//		this.citiesCollection = citiesCollection;
//	} 

} 

