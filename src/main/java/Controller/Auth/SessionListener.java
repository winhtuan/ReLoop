/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/ServletListener.java to edit this template
 */
package Controller.Auth;

import Model.DAO.auth.AccountDao;
import Model.entity.auth.User;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author Thanh Loc
 */
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        
        User u=(User)se.getSession().getAttribute("cus");
        new AccountDao().updateOfflineAt(u.getUserId());
    }
}
