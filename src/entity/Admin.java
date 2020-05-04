package entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 
	
	@Column(name = "LOG_URL") 
	private Integer log_url;

	@Column(name = "HOME_VIEW") 
	private Integer home_view;

	@Column(name = "MAX_COURTESY")
	private BigDecimal max_courtesy;

	@Column(name = "RESEARCHED_VIEW") 
	private Integer researched_view;

	@Column(name = "RADIUS") 
	private Integer radius;

	@Column(name = "ADD_RADIUS") 
	private Integer add_radius;

	@Column(name = "LIMIT_RADIUS") 
	private Integer limit_radius;

	@Column(name = "LIMIT_MOST_ACCESSED") 
	private Integer limit_most_accessed;

	@Column(name = "LIMIT_NEW_ADS") 
	private Integer limit_new_ads;

	@Column(name = "TOTAL_ADV") 
	private Integer total_adv;

	@Column(name = "TOTAL_ADV_ACTIVES") 
	private Integer total_adv_actives;

	@Column(name = "TOTAL_STORES") 
	private Integer total_stores;

	@Column(name = "URL_LOCAL") 
	private String url_local;

	@Column(name = "URL_SERVER") 
	private String url_server;

	@Column(name = "IP_SERVER") 
	private String ip_server;

	@Column(name = "PATH_IMAGES_LOCAL") 
	private String path_images_local;

	@Column(name = "PATH_IMAGES_SERVER") 
	private String path_images_server;

	@Column(name = "PATH_SITEMAP_LOCAL") 
	private String path_sitemap_local;

	@Column(name = "PATH_SITEMAP_SERVER") 
	private String path_sitemap_server;

	public Integer getId() {
		return id;
	}

	public Integer getLog_url() {
		return log_url;
	}

	public void setLog_url(Integer log_url) {
		this.log_url = log_url;
	}

	public BigDecimal getMax_courtesy() {
		return max_courtesy;
	}

	public void setMax_courtesy(BigDecimal max_courtesy) {
		this.max_courtesy = max_courtesy;
	}

	public Integer getHome_view() {
		return home_view;
	}

	public void setHome_view(Integer home_view) {
		this.home_view = home_view;
	}

	public Integer getResearched_view() {
		return researched_view;
	}

	public void setResearched_view(Integer researched_view) {
		this.researched_view = researched_view;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Integer getAdd_radius() {
		return add_radius;
	}

	public void setAdd_radius(Integer add_radius) {
		this.add_radius = add_radius;
	}

	public Integer getLimit_radius() {
		return limit_radius;
	}

	public void setLimit_radius(Integer limit_radius) {
		this.limit_radius = limit_radius;
	}

	public Integer getLimit_most_accessed() {
		return limit_most_accessed;
	}

	public void setLimit_most_accessed(Integer limit_most_accessed) {
		this.limit_most_accessed = limit_most_accessed;
	}

	public Integer getLimit_new_ads() {
		return limit_new_ads;
	}

	public void setLimit_new_ads(Integer limit_new_ads) {
		this.limit_new_ads = limit_new_ads;
	}

	public Integer getTotal_adv() {
		return total_adv;
	}

	public void setTotal_adv(Integer total_adv) {
		this.total_adv = total_adv;
	}

	public Integer getTotal_adv_actives() {
		return total_adv_actives;
	}

	public void setTotal_adv_actives(Integer total_adv_actives) {
		this.total_adv_actives = total_adv_actives;
	}

	public Integer getTotal_stores() {
		return total_stores;
	}

	public void setTotal_stores(Integer total_stores) {
		this.total_stores = total_stores;
	}

	public String getUrl_local() {
		return url_local;
	}

	public void setUrl_local(String url_local) {
		this.url_local = url_local;
	}

	public String getUrl_server() {
		return url_server;
	}

	public void setUrl_server(String url_server) {
		this.url_server = url_server;
	}

	public String getIp_server() {
		return ip_server;
	}

	public void setIp_server(String ip_server) {
		this.ip_server = ip_server;
	}

	public String getPath_images_local() {
		return path_images_local;
	}

	public void setPath_images_local(String path_images_local) {
		this.path_images_local = path_images_local;
	}

	public String getPath_images_server() {
		return path_images_server;
	}

	public void setPath_images_server(String path_images_server) {
		this.path_images_server = path_images_server;
	}

	public String getPath_sitemap_local() {
		return path_sitemap_local;
	}

	public void setPath_sitemap_local(String path_sitemap_local) {
		this.path_sitemap_local = path_sitemap_local;
	}

	public String getPath_sitemap_server() {
		return path_sitemap_server;
	}

	public void setPath_sitemap_server(String path_sitemap_server) {
		this.path_sitemap_server = path_sitemap_server;
	}

}
