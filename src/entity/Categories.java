package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "categories")

@NamedQueries({
	@NamedQuery(
			name = "query_categories_all", 
			query = " FROM Categories categories ")
})
public class Categories implements Serializable {

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_ALL = "query_categories_all";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "KEYWORDS")
	@Type(type="text")
	private String keywords;

	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}
