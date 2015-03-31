package com.instantchat;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class SendMsgServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String chatroomId = req.getParameter("chatroomkey");
    String msg = req.getParameter("m");
    PersistenceManager pm = PMF.get().getPersistenceManager();
    ChatRoom chatroom = pm.getObjectById(ChatRoom.class, KeyFactory.stringToKey(chatroomId));
   
    String currentUserId = userService.getCurrentUser().getUserId();
    chatroom.sendMsg(currentUserId, msg);
    pm.close();
  }
}