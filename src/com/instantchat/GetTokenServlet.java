// Copyright 2010 Google Inc. All Rights Reserved.

package com.instantchat;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 *
 */
@SuppressWarnings("serial")
public class GetTokenServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	  
	  Logger.getAnonymousLogger().log(Level.INFO, "Get Token");
	
    UserService userService = UserServiceFactory.getUserService();
    
    //Get chat ID
    String chatroomId = req.getParameter("chatroomkey");
    PersistenceManager pm = PMF.get().getPersistenceManager();
    
    //Get chat room instance
    ChatRoom chatroom = pm.getObjectById(ChatRoom.class, KeyFactory.stringToKey(chatroomId));
    
    //Get user
    String currentUserId = userService.getCurrentUser().getUserId();
    
    //Check this user is one of participants
    if (currentUserId.equals(chatroom.getUserX()) ||
        currentUserId.equals(chatroom.getUserO())) 
    {
      String channelKey = chatroom.getChannelKey(currentUserId);
      ChannelService channelService = ChannelServiceFactory.getChannelService();
      resp.setContentType("text/plain");
      resp.getWriter().println(channelService.createChannel(channelKey));
      return;
    }
    resp.setStatus(401);
  }
}