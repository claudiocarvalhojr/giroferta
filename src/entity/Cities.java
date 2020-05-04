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
@Table(name = "cities")
@NamedQueries({
	@NamedQuery(
			name = "query_cities_count", 
			query = "SELECT COUNT(citie) FROM Cities citie WHERE citie.name LIKE :name"),
	@NamedQuery(
			name = "query_cities_list", 
			query = "FROM Cities citie ORDER BY citie.name"),
	@NamedQuery(
			name = "query_cities_list_by_name", 
			query = "FROM Cities citie WHERE citie.name LIKE :name ORDER BY citie.name"),
	@NamedQuery(
			name = "query_cities_filtered_by_state", 
			query = "FROM Cities citie WHERE citie.state_id.id = :state_id ORDER BY citie.name"),
	@NamedQuery(
			name = "query_cities_by_citie_and_state", 
			query = "FROM Cities citie "
					+ " WHERE "
					+ " citie.name = :citie_name AND "
					+ " citie.state_id.initials = :state_initials")
})

public final class Cities implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_COUNT              = "query_cities_count";
	public static final String QUERY_LIST               = "query_cities_list";
	public static final String QUERY_LIST_BY_NAME       = "query_cities_list_by_name";
	public static final String QUERY_FILTERED_BY_STATE  = "query_cities_filtered_by_state";
	public static final String QUERY_BY_CITIE_AND_STATE = "query_cities_by_citie_and_state";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 

	@Column(name = "IBGE_CODE") 
	private String ibge_code; 

	@Column(name = "NAME") 
	private String name; 

	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at; 

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at; 

	@JoinColumn(name = "STATE_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private States state_id; 

//	// update: 0	delete: 0 
//	@OneToMany(mappedBy = "city_id") 
//	private Collection<Stores> storesCollection; 
//
//	// update: 0	delete: 0 
//	@OneToMany(mappedBy = "city_id") 
//	private Collection<Users> usersCollection;

	public Integer getId() {
		return id;
	}

	public String getIbge_code() {
		return ibge_code;
	}

	public void setIbge_code(String ibge_code) {
		this.ibge_code = ibge_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public States getState_id() {
		return state_id;
	}

	public void setState_id(States state_id) {
		this.state_id = state_id;
	}

//	public Collection<Stores> getStoresCollection() {
//		return storesCollection;
//	}
//
//	public void setStoresCollection(Collection<Stores> storesCollection) {
//		this.storesCollection = storesCollection;
//	}
//
//	public Collection<Users> getUsersCollection() {
//		return usersCollection;
//	}
//
//	public void setUsersCollection(Collection<Users> usersCollection) {
//		this.usersCollection = usersCollection;
//	} 
	
} 

