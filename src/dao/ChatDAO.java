package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connection.Connection;
import entity.Advertisements;
import entity.Chat;
import entity.ChatMessages;
import entity.Users;
import interfaces.IDao;
import mail.MailAlert;
import mail.MailChat;
import mail.MailChatSpecial;
import spinwork.Manager;

public final class ChatDAO implements IDao {

	private static final String FORM = "chat/form.jsp";
	private static final String LIST = "chat/list.jsp";
	private Connection connection = null;
	
	public ChatDAO(Connection connection) {
		this.connection = connection;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String form(HttpServletRequest request, HttpServletResponse response) {
		try {
			connection.beginTransaction();
			Users user = (Users) request.getSession().getAttribute("currentUser");
			List<ChatMessages> listChatMessages = null;
			if (check(request)) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("chat_id", Integer.parseInt(request.getParameter("chat_id")));
				listChatMessages = (ArrayList<ChatMessages>) connection.list(ChatMessages.QUERY_LIST_ALL_BY_CHAT, param);
				param.clear();
				for (ChatMessages chatMessage : listChatMessages) {
					if (chatMessage.getMessage_read() == 0 && (chatMessage.getUser_receiver().getId().compareTo(user.getId()) == 0)) {
						chatMessage.setMessage_read(1);
						chatMessage.setUpdated_at(new Date());
						connection.update(chatMessage);
					}				
				}
				connection.commit();
			}
			request.setAttribute("chat_id", request.getParameter("chat_id"));
			request.setAttribute("current", request.getParameter("current"));
			request.setAttribute("maximum", request.getParameter("maximum"));
			request.setAttribute("valueSearch", request.getParameter("valueSearch"));
			request.setAttribute("listChatMessages", listChatMessages);
		} finally {
			connection.closeTransaction();
		}
		return FORM;
	}

	@Override
	public String save(HttpServletRequest request, HttpServletResponse response) {
		try {
			connection.beginTransaction();
			if (check(request))
				saveMessage((Chat) connection.find(Chat.class, Integer.parseInt(request.getParameter("chat_id"))), null, request, response);
		} finally {
			connection.closeTransaction();
		}
		return form(request, response);
	}
	
	public void create(HttpServletRequest request, HttpServletResponse response) {
		try {
			connection.beginTransaction();
			Users user = (Users) request.getSession().getAttribute("currentUser");
			Users userReceiver = null;
			Advertisements advertisement = null;
			Chat chat = null;
			if (request.getParameter("advertisement_id") != null)
				advertisement = (Advertisements) connection.find(Advertisements.class, Integer.parseInt(request.getParameter("advertisement_id")));
			if (advertisement != null)
				userReceiver = (Users) advertisement.getUser_id();
			else
				userReceiver = (Users) connection.find(Users.class, Integer.parseInt(request.getParameter("contact_id")));
			if (!request.getParameter("chatMessage.message").equals("") && userReceiver != null) {
				Date dataAtual = new Date();
				chat = new Chat();
				chat.setUser_sender(user);
				chat.setUser_receiver(userReceiver);
				chat.setAdvertisement_id(advertisement);
				chat.setStatus(1);
				chat.setCreated_at(dataAtual);
				connection.save(chat);
				connection.commit();
				connection.closeTransaction();
				saveMessage(chat, advertisement, request, response);
			}
		} finally {
			connection.closeTransaction();
		}
	}
	
	private void saveMessage(Chat chat, Advertisements advertisement, HttpServletRequest request, HttpServletResponse response) {
		try {
			connection.beginTransaction();
			Users user = (Users) request.getSession().getAttribute("currentUser");
			ChatMessages chatMessage = null;
			Date dataAtual = new Date();
			ArrayList<String> messages = new ArrayList<String>();
			if (request.getParameter("chatMessage.message").equals("")) {
				messages.add("chatMessages.fillFieldMessage");
				request.setAttribute("messages", messages);
			}
			else {
				chatMessage = new ChatMessages();
				chatMessage = (ChatMessages) Manager.setObject(chatMessage, request, connection);
				chatMessage.setUser_sender(user);
				if (chat.getUser_sender().getId().compareTo(user.getId()) == 0)
					chatMessage.setUser_receiver(chat.getUser_receiver());
				else
					chatMessage.setUser_receiver(chat.getUser_sender());
				chatMessage.setChat_id(chat);
				chatMessage.setMessage_read(0);
				chatMessage.setMessage_text(request.getParameter("chatMessage.message"));
				chatMessage.setStatus(1);
				chatMessage.setCreated_at(dataAtual);
				chatMessage.setUpdated_at(dataAtual);
				if ((boolean) request.getAttribute("isValid")) {
					connection.save(chatMessage);
					connection.commit();
					String senderName = "";
					String receiverName = ""; 
					String receiverEmail = "";
					if (user.getType_person().equals("J"))
						senderName = user.getCompany_name();
					else
						senderName = user.getName();
					if (user.getId().compareTo(chat.getUser_sender().getId()) == 0) {
						if (chat.getUser_receiver().getType_person().equals("J"))
							receiverName = chat.getUser_receiver().getCompany_name();
						else
							receiverName = chat.getUser_receiver().getName();
						receiverEmail = chat.getUser_receiver().getEmail();
					}
					else {
						if (chat.getUser_sender().getType_person().equals("J"))
							receiverName = chat.getUser_sender().getCompany_name();
						else
							receiverName = chat.getUser_sender().getName();
						receiverEmail = chat.getUser_sender().getEmail();
					}
					
					if (chat.getUser_receiver().getId() == 1000032 || chat.getUser_receiver().getId() == 1000033) {
						MailChatSpecial mailChat = new MailChatSpecial(
								(String) request.getSession().getAttribute("giroUrl"),
								senderName, 
								chat.getUser_sender().getEmail(), 
								chatMessage.getMessage_text(),
								receiverName, 
								receiverEmail,
								advertisement.getReference(),
								(String) request.getSession().getAttribute("giroUrlServerImages")
							);
						Thread threadMailChat = new Thread(mailChat);
						threadMailChat.start();
					}
					else {
						MailChat mailChat = new MailChat(
								(String) request.getSession().getAttribute("giroUrl"),
								senderName, 
								receiverName, 
								receiverEmail,
								(String) request.getSession().getAttribute("giroUrlServerImages")
							);
						Thread threadMailChat = new Thread(mailChat);
						threadMailChat.start();
					}
					
					MailAlert mailAlert = new MailAlert(
							"Giroferta - Nova mensagem chat", 
							"\n\nID: " + chatMessage.getId() + 
							"\nDe: " + chat.getUser_sender().getId() + 
							"\nPara: " + chat.getUser_receiver().getId() + 
							"\nMensagem: " + chatMessage.getMessage_text() + 
							"\n\n",
							new String[] {"golive@giroferta.com"},
							(String) request.getSession().getAttribute("giroUrl")
						);
					Thread threadMailAlert = new Thread(mailAlert);
					threadMailAlert.start();
				}
			}
		} finally {
			connection.closeTransaction();
		}
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		return FORM;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public String list(HttpServletRequest request, HttpServletResponse response) {
//		Util util = new Util();
//		util.printNameValue(request.getParameterMap(), true);
		try {
			connection.beginTransaction();
			Users user = (Users) request.getSession().getAttribute("currentUser");
			String valueSearch = request.getParameter("valueSearch");
			if (valueSearch == null)
				valueSearch = "";
			int current = Integer.parseInt(request.getParameter("current"));
			int maximum = 15;
			int totalPages = 0;
			int totalResults = 0;
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("user_id", user.getId());
			param.put("message_text", "%" + valueSearch + "%");
			List<ChatMessages> listChatMessages = (List<ChatMessages>) connection.list(ChatMessages.QUERY_LIST_ALL, param, current, maximum);
			param.clear();
			int cont = 0;
			ArrayList<ChatMessages> aux = new ArrayList<ChatMessages>();
			for (int i=0; i < listChatMessages.size(); i++) {
				for (int j=i+1; j < listChatMessages.size(); j++) {
					if (listChatMessages.get(i).getChat_id().getId().compareTo(listChatMessages.get(j).getChat_id().getId()) == 0) {
						cont++;
						aux.add(listChatMessages.get(j));
					}
				}
			}
			for (int i=0; i < cont; i++)
				listChatMessages.remove(aux.get(i));
			totalResults = listChatMessages.size();
			if (totalResults > 0)
				totalPages = totalResults / maximum;
			request.setAttribute("current", current);
			request.setAttribute("maximum", maximum);
			request.setAttribute("valueSearch", valueSearch);
			request.setAttribute("totalResults", totalResults);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("listChatMessages", listChatMessages);
		} finally {
			connection.closeTransaction();
		}
		return LIST;
	}

	@Override
	public boolean check(HttpServletRequest request) {
		boolean isCheck;
		if (request.getParameter("chat_id") != null && !request.getParameter("chat_id").equals("") && !request.getParameter("chat_id").equals("0"))
			isCheck = true;
		else 
			isCheck = false;
		return isCheck;
	}

}
