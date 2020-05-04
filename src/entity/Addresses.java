package entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "addresses")
@NamedQueries({
	@NamedQuery(
			name = "query_addresses_by_lat_and_long", 
			query = "FROM Addresses addresses "
					+ " WHERE "
					+ " addresses.latitude = :latitude AND "
					+ " addresses.longitude = :longitude"),
	@NamedQuery(
			name = "query_addresses_by_updated", 
			query = "FROM Addresses addresses "
					+ " WHERE "
					+ " addresses.updated = 0 "),
})

public final class Addresses implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String QUERY_ADDRESSES_BY_LAT_LONG = "query_addresses_by_lat_and_long";
	public static final String QUERY_ADDRESSES_BY_UPDATED  = "query_addresses_by_updated";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 
	
	@Column(name = "LATITUDE") 
	private String latitude;
	
	@Column(name = "LONGITUDE") 
	private String longitude;
	
	@Column(name = "CITIE") 
	private String citie;
	
	@Column(name = "STATE") 
	private String state;
	
	@Column(name = "COUNTRY") 
	private String country;
	
	@Type(type="text")
	@Column(name = "FORMATTED") 
	private String formatted;
	
	@Column(name = "UPDATED")
	private Integer updated;
	
	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at; 

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at;

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

	public String getCitie() {
		return citie;
	}

	public void setCitie(String citie) {
		this.citie = citie;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFormatted() {
		return formatted;
	}

	public void setFormatted(String formatted) {
		this.formatted = formatted;
	}

	public Integer getUpdated() {
		return updated;
	}

	public void setUpdated(Integer updated) {
		this.updated = updated;
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

	public Integer getId() {
		return id;
	} 
	
}
