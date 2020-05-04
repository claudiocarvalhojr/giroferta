package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ads_stores_most_accessed")
public final class AdsStoresMostAccessed implements Serializable { 

	private static final long serialVersionUID = 1L; 

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
