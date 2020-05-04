package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "chat")

public final class Chat implements Serializable { 

	private static final long serialVersionUID = 1L; 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at;

	@JoinColumn(name = "USER_SENDER", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user_sender; 

	@JoinColumn(name = "USER_RECEIVER", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user_receiver;

	@JoinColumn(name = "ADVERTISEMENT_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Advertisements advertisement_id;

	@OneToMany(mappedBy = "chat_id")
	private List<ChatMessages> chatMessages;

	public Integer getId() {
		return id;
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

	public Users getUser_sender() {
		return user_sender;
	}

	public void setUser_sender(Users user_sender) {
		this.user_sender = user_sender;
	}

	public Users getUser_receiver() {
		return user_receiver;
	}

	public void setUser_receiver(Users user_receiver) {
		this.user_receiver = user_receiver;
	}

	public Advertisements getAdvertisement_id() {
		return advertisement_id;
	}

	public void setAdvertisement_id(Advertisements advertisement_id) {
		this.advertisement_id = advertisement_id;
	}

	public List<ChatMessages> getChatMessages() {
		return chatMessages;
	}

	public void setChatMessages(List<ChatMessages> chatMessages) {
		this.chatMessages = chatMessages;
	}

}
