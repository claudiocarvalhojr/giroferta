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
@Table(name = "orders")
@NamedQueries({
	@NamedQuery(
			name = "query_orders_find_by_id", 
			query = "FROM Orders aux WHERE aux.id = :id"),
	@NamedQuery(
			name = "query_orders_find_by_reference", 
			query = "FROM Orders aux WHERE aux.reference LIKE :reference"),
	@NamedQuery(
			name = "query_orders_find_by_checkoutCode", 
			query = "FROM Orders aux WHERE aux.checkout_code LIKE :checkout_code"),
	@NamedQuery(
			name = "query_orders_list_all_by_user", 
			query = "FROM Orders aux WHERE aux.user_id.id = :user_id AND aux.transaction_code != '' ORDER BY aux.created_at DESC")
})

public final class Orders implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String QUERY_FIND_BY_ID = "query_orders_find_by_id";
	public static final String QUERY_FIND_BY_REFERENCE = "query_orders_find_by_reference";
	public static final String QUERY_FIND_BY_CHECKOUT_CODE = "query_orders_find_by_checkoutCode";
	public static final String QUERY_LIST_ALL_BY_USER = "query_orders_list_all_by_user";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 
	
	@Column(name = "REFERENCE")
	private String reference;
	
	@Column(name = "CHECKOUT_CODE")
	private String checkout_code;
	
	@Column(name = "TRANSACTION_CODE")
	private String transaction_code;
	
	@Column(name = "DATE") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date date; 
	
	@Column(name = "LAST_EVENT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date last_event;
	
	@Column(name = "ESCROW_END_DATE") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date escrow_end_date; 
	
	@Column(name = "CREDIT")
	private BigDecimal credit;
	
	@Column(name = "QUANTITY")
	private Integer quantity;
	
	@Column(name = "GROSS_AMOUNT")
	private BigDecimal gross_amount;
	
	@Column(name = "NET_AMOUNT")
	private BigDecimal net_amount;
	
	@Column(name = "DISCOUNT_AMOUNT")
	private BigDecimal discount_amount;
	
	@Column(name = "EXTRA_AMOUNT")
	private BigDecimal extra_amount;
	
	@Column(name = "FEE_AMOUNT")
	private BigDecimal fee_amount;
	
	@Column(name = "COD_METHOD")
	private Integer cod_method;
	
	@Column(name = "METHOD")
	private String method;
	
	@Column(name = "COD_STATUS")
	private Integer cod_status;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "CHECKED")
	private Integer checked;
	
	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at; 

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at; 

	@JoinColumn(name = "USER_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user_id; 
	
	public Integer getId() {
		return id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCheckout_code() {
		return checkout_code;
	}

	public void setCheckout_code(String checkout_code) {
		this.checkout_code = checkout_code;
	}

	public String getTransaction_code() {
		return transaction_code;
	}

	public void setTransaction_code(String transaction_code) {
		this.transaction_code = transaction_code;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getLast_event() {
		return last_event;
	}

	public void setLast_event(Date last_event) {
		this.last_event = last_event;
	}

	public Date getEscrow_end_date() {
		return escrow_end_date;
	}

	public void setEscrow_end_date(Date escrow_end_date) {
		this.escrow_end_date = escrow_end_date;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public BigDecimal getGross_amount() {
		return gross_amount;
	}

	public void setGross_amount(BigDecimal gross_amount) {
		this.gross_amount = gross_amount;
	}

	public BigDecimal getNet_amount() {
		return net_amount;
	}

	public void setNet_amount(BigDecimal net_amount) {
		this.net_amount = net_amount;
	}

	public BigDecimal getDiscount_amount() {
		return discount_amount;
	}

	public void setDiscount_amount(BigDecimal discount_amount) {
		this.discount_amount = discount_amount;
	}

	public BigDecimal getExtra_amount() {
		return extra_amount;
	}

	public void setExtra_amount(BigDecimal extra_amount) {
		this.extra_amount = extra_amount;
	}

	public BigDecimal getFee_amount() {
		return fee_amount;
	}

	public void setFee_amount(BigDecimal fee_amount) {
		this.fee_amount = fee_amount;
	}

	public Integer getCod_method() {
		return cod_method;
	}

	public void setCod_method(Integer cod_method) {
		this.cod_method = cod_method;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Integer getCod_status() {
		return cod_status;
	}

	public void setCod_status(Integer cod_status) {
		this.cod_status = cod_status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
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

	public Users getUser_id() {
		return user_id;
	}

	public void setUser_id(Users user_id) {
		this.user_id = user_id;
	}

}
