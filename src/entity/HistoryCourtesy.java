package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "history_courtesy")

@NamedQueries({
	@NamedQuery(
			name = "query_historyCourtesy_by_user", 
			query = "FROM HistoryCourtesy historyCourtesy "
					+ "WHERE "
					+ "historyCourtesy.user_id.id = :user_id")
})

public final class HistoryCourtesy implements Serializable { 

	private static final long serialVersionUID = 1L; 
	public static final String QUERY_BY_USER = "query_historyCourtesy_by_user";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false)
	private Integer id; 

	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Users vendor_id;
	
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user_id;
	
	@Column(name = "CREDIT")
	private BigDecimal credit;
	
	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at;

	public Integer getId() {
		return id;
	}

	public Users getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(Users vendor_id) {
		this.vendor_id = vendor_id;
	}

	public Users getUser_id() {
		return user_id;
	}

	public void setUser_id(Users user_id) {
		this.user_id = user_id;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

}
