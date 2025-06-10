/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Conversation;

import Model.DAO.ConversationDAO;
import Model.DAO.MessageDAO;
import Model.Entity.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author Thanh Loc
 */
@WebServlet("/GetChatHistory")
public class GetChatHistory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int user1 = Integer.parseInt(request.getParameter("user1"));
    int user2 = Integer.parseInt(request.getParameter("user2"));
    int conID = ConversationDAO.getOrCreateConversation(user1, user2);
    List<Message> messages = MessageDAO.getMessagesByconversationId(conID);

    response.setContentType("application/json;charset=UTF-8");
    PrintWriter out = response.getWriter();

    Gson gson = new Gson();

    String json = gson.toJson(messages);
    out.print(json);
    out.flush();
}
}
