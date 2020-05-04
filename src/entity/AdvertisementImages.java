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
@Table(name = "advertisement_images")
@NamedQueries({
	@NamedQuery(
			name = "query_advertisementImages_count_all", 
			query = " SELECT COUNT(images) "
					+ " FROM AdvertisementImages images "
					+ " WHERE "
					+ " images.link <> '' AND "
					+ " images.status = 1 "),
	@NamedQuery(
			name = "query_advertisementImages_count_all_by_user", 
			query = " SELECT COUNT(images) "
					+ " FROM AdvertisementImages images "
					+ " WHERE "
					+ "	images.advertisement_id.user_id.id = :user_id AND "
					+ " images.link <> '' AND "
					+ " images.status = 1 "),
	@NamedQuery(
			name = "query_advertisementImages_count_all_distinct_by_advertisement", 
			query = " SELECT COUNT(DISTINCT images.advertisement_id.id) "
					+ " FROM AdvertisementImages images "
					+ " WHERE "
					+ " images.link <> '' AND "
					+ " images.status = 1 "),
	@NamedQuery(
			name = "query_count_advertisementImages_by_advertisement", 
			query = " SELECT COUNT(images) "
					+ " FROM AdvertisementImages images "
					+ " WHERE "
					+ " images.advertisement_id.id = :advertisement_id AND "
					+ " images.status = 1 "),
	@NamedQuery(
			name = "query_advertisementImages_all", 
			query = " FROM AdvertisementImages images "
					+ " WHERE "
					+ " images.link <> '' AND "
					+ " images.status = 1 "),
	@NamedQuery(
			name = "query_advertisementImages_all_by_user", 
			query = " FROM AdvertisementImages images "
					+ " WHERE "
					+ "	images.advertisement_id.user_id.id = :user_id AND "
					+ " images.link <> '' AND "
					+ " images.status = 1 "),
	@NamedQuery(
			name = "query_advertisementImages_all_distinct_by_advertisement", 
			query = " SELECT DISTINCT(images.advertisement_id.id) "
					+ "FROM AdvertisementImages images "
					+ " WHERE "
					+ " images.link <> '' AND "
					+ " images.status = 1 "
					+ " ORDER BY advertisement_id.id "),
	@NamedQuery(
			name = "query_advertisementImages_by_advertisement", 
			query = " FROM AdvertisementImages images "
					+ " WHERE "
					+ " images.advertisement_id.id = :advertisement_id AND "
					+ " images.status = 1 "),
	@NamedQuery(
			name = "query_advertisementImages_by_link", 
			query = " FROM AdvertisementImages images "
					+ " WHERE "
					+ " images.link = :link AND "
					+ " images.status = 1"),
	@NamedQuery(
			name = "query_advertisementImages_by_file_name", 
			query = " FROM AdvertisementImages images "
					+ " WHERE "
					+ " images.image_file_name = :file_name AND "
					+ " images.status = 1")
})

public final class AdvertisementImages implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_COUNT_ALL                           = "query_advertisementImages_count_all";
	public static final String QUERY_COUNT_ALL_BY_USER                   = "query_advertisementImages_count_all_by_user";
	public static final String QUERY_COUNT_ALL_DISTINCT_BY_ADVERTISEMENT = "query_advertisementImages_count_all_distinct_by_advertisement";
	public static final String QUERY_COUNT_BY_ADVERTISEMENT              = "query_count_advertisementImages_by_advertisement";
	public static final String QUERY_ALL                                 = "query_advertisementImages_all";
	public static final String QUERY_ALL_BY_USER                         = "query_advertisementImages_all_by_user";
	public static final String QUERY_ALL_DISTINCT_BY_ADVERTISEMENT       = "query_advertisementImages_all_distinct_by_advertisement";
	public static final String QUERY_BY_ADVERTISEMENT                    = "query_advertisementImages_by_advertisement";
	public static final String QUERY_BY_LINK                             = "query_advertisementImages_by_link";
	public static final String QUERY_BY_FILE_NAME                        = "query_advertisementImages_by_file_name";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 

	@Column(name = "IMAGE_FILE_NAME") 
	private String image_file_name; 

	@Column(name = "IMAGE_FILE_TYPE") 
	private String image_file_type; 

	@Column(name = "LINK")
	private String link;
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at; 

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at; 

	@JoinColumn(name = "ADVERTISEMENT_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Advertisements advertisement_id;

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

	public Advertisements getAdvertisement_id() {
		return advertisement_id;
	}

	public void setAdvertisement_id(Advertisements advertisement_id) {
		this.advertisement_id = advertisement_id;
	} 

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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

