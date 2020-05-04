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

import org.hibernate.annotations.Type;

import spinwork.validations.NotNull;

@Entity
@Table(name = "chat_messages")
@NamedQueries({
	@NamedQuery(
			name = "query_list_all", 
			query = "FROM ChatMessages aux WHERE "
					+ "aux.message_text LIKE :message_text AND "
					+ "(aux.user_sender.id = :user_id OR "
					+ "aux.user_receiver.id = :user_id) AND "
					+ "aux.chat_id.status = 1 AND "
					+ "aux.status = 1 "
					+ "ORDER BY aux.id DESC"),
	@NamedQuery(
			name = "query_chat_messages_by_chat", 
			query = "FROM ChatMessages aux WHERE aux.chat_id.id = :chat_id AND aux.status = 1")
})

public final class ChatMessages implements Serializable { 

	private static final long serialVersionUID = 1L; 
//	public static final String QUERY_LIST_ALL_BY_SENDER = "query_chat_messages_by_sender";
//	public static final String QUERY_LIST_ALL_BY_RECEIVER = "query_chat_messages_by_receiver";
	public static final String QUERY_LIST_ALL = "query_list_all";
	public static final String QUERY_LIST_ALL_BY_CHAT = "query_chat_messages_by_chat";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "ID", nullable = false) 
	private Integer id; 
	
	@Column(name = "MESSAGE_READ")
	private Integer message_read;

	@NotNull(message = "chatMessages.fillFieldMessage")
	@Type(type="text")
	@Column(name = "MESSAGE_TEXT") 
	private String message_text; 
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name = "CREATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created_at;

	@Column(name = "UPDATED_AT") 
	@Temporal(TemporalType.TIMESTAMP) 
	private Date updated_at;

	@JoinColumn(name = "USER_SENDER", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user_sender; 

	@JoinColumn(name = "USER_RECEIVER", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user_receiver; 

	@JoinColumn(name = "CHAT_ID", referencedColumnName = "ID") 
	@ManyToOne(fetch = FetchType.LAZY)
	private Chat chat_id;

	public Integer getId() {
		return id;
	}

	public Integer getMessage_read() {
		return message_read;
	}

	public void setMessage_read(Integer message_read) {
		this.message_read = message_read;
	}

	public String getMessage_text() {
		return message_text;
	}

	public void setMessage_text(String message_text) {
		this.message_text = message_text;
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

	public Chat getChat_id() {
		return chat_id;
	}

	public void setChat_id(Chat chat_id) {
		this.chat_id = chat_id;
	}

}
