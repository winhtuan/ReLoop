package Controller;

import Utils.HttpSessionConfigurator;
import Model.entity.auth.Account;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/notificate", configurator = HttpSessionConfigurator.class)
public class NotificateSocket {

    // Map lưu các session theo userId
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
        Account account = (Account) httpSession.getAttribute("user");
        if (account != null) {
            String userId = account.getUserId();
            sessions.put(userId, session);
            System.out.println("User " + userId + " connected to notification socket");
        }
    }

    @OnClose
    public void onClose(Session session) {
        // Xoá session user nào đã disconnect
        sessions.values().remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    // Phương thức tĩnh để push notification từ backend cho đúng user
    public static void pushNotification(String userId, String title, String content, String link) {
        Session session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            String message = String.format(
                "{\"type\":\"notification\", \"title\":\"%s\", \"content\":\"%s\", \"link\":\"%s\"}",
                escapeJson(title), escapeJson(content), escapeJson(link)
            );
            session.getAsyncRemote().sendText(message);
        }
    }

    private static String escapeJson(String str) {
        return str == null ? "" : str.replace("\"", "\\\"");
    }
}

