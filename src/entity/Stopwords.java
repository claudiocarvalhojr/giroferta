package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "stopwords")
@NamedQuery(
		name = "query_stopwords", 
		query = "FROM Stopwords aux WHERE aux.term = :stopword")

public class Stopwords implements Serializable {

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_STOPWORDS = "query_stopwords";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id;
	
	@Column(name = "TERM")
	private String term;

	public Integer getId() {
		return id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

}
