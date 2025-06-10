package Controller.Conversation;

import Model.DAO.auth.UserDao;
import Model.DAO.conversation.ConversationDAO;
import Model.DAO.conversation.MessageDAO;
import Model.DAO.conversation.blockDAO;
import Model.entity.auth.Account;
import Model.entity.auth.User;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat", configurator = HttpSessionConfigurator.class)
public class chat {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
        Account account = (Account) httpSession.getAttribute("user");
        if (account != null) {
            String userId = account.getUserId();
            User user = new UserDao().getUserById(userId);
            sessions.put(userId, session);
            System.out.println("User connected: " + user.getFullName());

            List<String> unreadSenders = MessageDAO.getUsersWithUnreadMessages(userId);

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (String senderId : unreadSenders) {
                arrayBuilder.add(senderId);
            }

            JsonObject unreadNotice = Json.createObjectBuilder()
                    .add("type", "unread_list")
                    .add("senders", arrayBuilder)
                    .build();

            session.getAsyncRemote().sendText(unreadNotice.toString());
        }
    }

    @OnMessage
    public void onMessage(String messageJson, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(messageJson))) {
            JsonObject json = reader.readObject();
            String type = json.containsKey("type") ? json.getString("type") : "message";

            // Xác định user gửi
            String fromUserId = null;
            String fromUsername = null;
            for (Map.Entry<String, Session> entry : sessions.entrySet()) {
                if (entry.getValue().equals(session)) {
                    fromUserId = entry.getKey();
                    fromUsername = new UserDao().getUserById(fromUserId).getFullName();
                    break;
                }
            }
            if (fromUserId == null) {
                return;
            }

            switch (type) {
                case "image": {
                    String toUserId = json.getString("toUserId");
                    String imageUrl = json.getString("imageUrl");
                    String productId = json.getString("productId");

                    if (new blockDAO().isBlocked(toUserId, fromUserId) || new blockDAO().isBlocked(fromUserId, toUserId)) {
                        sendBlockNotice(session);
                        return;
                    }

                    String conversationId = new ConversationDAO().getOrCreateConversation(fromUserId, toUserId, productId);
                    String messageId = MessageDAO.saveMessage(conversationId, fromUserId, imageUrl, "img");
                    String timestamp = LocalDateTime.now().toString();

                    JsonObject imageMessage = Json.createObjectBuilder()
                            .add("type", "image")
                            .add("fromUserId", fromUserId)
                            .add("fromUsername", fromUsername)
                            .add("toUserId", toUserId)
                            .add("content", imageUrl)
                            .add("messageId", messageId)
                            .add("conversationId", conversationId)
                            .add("timestamp", timestamp)
                            .build();

                    Session toSession = sessions.get(toUserId);
                    if (toSession != null && toSession.isOpen()) {
                        toSession.getAsyncRemote().sendText(imageMessage.toString());
                    }
                    session.getAsyncRemote().sendText(imageMessage.toString());
                    break;
                }
                case "message": {
                    String toUserId = json.getString("toUserId");
                    String content = json.getString("content");
                    String productId = json.getString("productId");

                    if (new blockDAO().isBlocked(toUserId, fromUserId) || new blockDAO().isBlocked(fromUserId, toUserId)) {
                        sendBlockNotice(session);
                        return;
                    }

                    String conversationId = new ConversationDAO().getOrCreateConversation(fromUserId, toUserId, productId);
                    String messageId = MessageDAO.saveMessage(conversationId, fromUserId, content);
                    String timestamp = LocalDateTime.now().toString();

                    JsonObject sendMessage = Json.createObjectBuilder()
                            .add("type", "message")
                            .add("fromUserId", fromUserId)
                            .add("fromUsername", fromUsername)
                            .add("toUserId", toUserId)
                            .add("content", content)
                            .add("messageId", messageId)
                            .add("conversationId", conversationId)
                            .add("timestamp", timestamp)
                            .build();

                    Session toSession = sessions.get(toUserId);
                    if (toSession != null && toSession.isOpen()) {
                        toSession.getAsyncRemote().sendText(sendMessage.toString());
                    }
                    session.getAsyncRemote().sendText(sendMessage.toString());
                    break;
                }
                case "recall": {
                    String toUserId = json.getString("toUserId");
                    String messageId = json.getString("messageId");

                    MessageDAO.recallMessage(messageId, fromUserId);

                    JsonObject recallNotify = Json.createObjectBuilder()
                            .add("type", "recall")
                            .add("messageId", messageId)
                            .add("fromUserId", fromUserId)
                            .add("toUserId", toUserId)
                            .add("timestamp", LocalDateTime.now().toString())
                            .build();

                    sendToBoth(fromUserId, toUserId, recallNotify);
                    break;
                }
                case "block": {
                    String blockedId = json.getString("blockedId");
                    new blockDAO().blockUser(fromUserId, blockedId);

                    JsonObject notifyBlocker = Json.createObjectBuilder()
                            .add("type", "block_status")
                            .add("blockedId", blockedId)
                            .add("status", "blocked_by_me")
                            .build();

                    JsonObject notifyBlocked = Json.createObjectBuilder()
                            .add("type", "block_status")
                            .add("blockerId", fromUserId)
                            .add("status", "blocked_me")
                            .build();

                    session.getAsyncRemote().sendText(notifyBlocker.toString());
                    sendTo(blockedId, notifyBlocked);
                    break;
                }
                case "unblock": {
                    String blockedId = json.getString("blockedId");
                    new blockDAO().unblockUser(fromUserId, blockedId);

                    JsonObject notifyBlocker = Json.createObjectBuilder()
                            .add("type", "block_status")
                            .add("blockedId", blockedId)
                            .add("status", "unblocked_by_me")
                            .build();

                    JsonObject notifyBlocked = Json.createObjectBuilder()
                            .add("type", "block_status")
                            .add("blockerId", fromUserId)
                            .add("status", "unblocked_me")
                            .build();

                    session.getAsyncRemote().sendText(notifyBlocker.toString());
                    sendTo(blockedId, notifyBlocked);
                    break;
                }
                case "read": {
                    String senderId = json.getString("fromUserId");
                    String receiverId = fromUserId;
                    MessageDAO.markMessagesAsRead(senderId, receiverId);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.values().remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    // Gửi thông báo "block" khi người dùng bị block
    private void sendBlockNotice(Session session) {
        JsonObject blockNotify = Json.createObjectBuilder()
                .add("type", "block")
                .add("message", "You cannot send messages because one of you has blocked the other.")
                .build();
        session.getAsyncRemote().sendText(blockNotify.toString());
    }

    // Gửi tin nhắn đến cả 2 người
    private void sendToBoth(String userA, String userB, JsonObject message) {
        sendTo(userA, message);
        sendTo(userB, message);
    }

    // Gửi tin nhắn đến 1 người
    private void sendTo(String userId, JsonObject message) {
        Session toSession = sessions.get(userId);
        if (toSession != null && toSession.isOpen()) {
            toSession.getAsyncRemote().sendText(message.toString());
        }
    }
}
