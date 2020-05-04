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
@Table(name = "store_images")
@NamedQuery(
		name = "query_storeImages_by_store", 
		query = "FROM StoreImages aux WHERE aux.store_id.id = :store_id AND aux.status = 1")

public final class StoreImages implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_BY_STORE = "query_storeImages_by_store";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 

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

	@JoinColumn(name = "STORE_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Stores store_id;

	public Integer getId() {
		return id;
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

	public Stores getStore_id() {
		return store_id;
	}

	public void setStore_id(Stores store_id) {
		this.store_id = store_id;
	}

}
