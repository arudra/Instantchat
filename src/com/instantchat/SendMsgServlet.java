package com.instantchat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class SendMsgServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	  Logger.getAnonymousLogger().log(Level.INFO, "Send message");
	    
    UserService userService = UserServiceFactory.getUserService();
    
    //Get chat ID
    String chatroomId = req.getParameter("chatroomkey");
    
    //Get message
    String msg = req.getParameter("m");
    PersistenceManager pm = PMF.get().getPersistenceManager();
    
    //Get chat room instance
    ChatRoom chatroom = pm.getObjectById(ChatRoom.class, KeyFactory.stringToKey(chatroomId));
   
    //Get user
    String currentUserId = userService.getCurrentUser().getNickname();
    
    //check if control message
    if (msg.startsWith("/"))
    {
    	if (msg.equals("/exit"))
    	{
    		chatroom.removeUser(currentUserId);
    		chatroom.sendMsgToClients(currentUserId + " has left the chatroom");
    	}
    }
    else
    {
    	//Send message
    	chatroom.sendMsg(currentUserId, msg);
    }
    pm.close();
  }
}