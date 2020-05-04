package entity; 

import java.io.Serializable; 

import javax.persistence.Entity; 
import javax.persistence.Table; 
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Column; 

import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Temporal; 
import javax.persistence.TemporalType; 

import java.util.Date; 

@Entity
@Table(name = "newsletter_lists")
@NamedQuery(
		name = "query_newsletterLists_find_by_email", 
		query = "FROM NewsletterLists aux WHERE aux.email = :email")

public final class NewsletterLists implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_FIND_BY_EMAIL = "query_newsletterLists_find_by_email";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 

	@Column(name = "NAME") 
	private String name; 

	@Column(name = "EMAIL") 
	private String email; 
	
	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at; 

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

} 

