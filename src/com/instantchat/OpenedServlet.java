// Copyright 2010 Google Inc. All Rights Reserved.

package com.instantchat;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.util.List;
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
public class OpenedServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException 
  {
	  Logger.getAnonymousLogger().log(Level.INFO, "Opened Servlet");
	  String chatroomKey = req.getParameter("chatroomkey");
	  PersistenceManager pm = PMF.get().getPersistenceManager();
	  ChatRoom chatroom = pm.getObjectById(ChatRoom.class, KeyFactory.stringToKey(chatroomKey));
	  if (chatroomKey != null && req.getUserPrincipal() != null) {
		  Logger.getAnonymousLogger().log(Level.INFO, "New channel opened in " + chatroom);
		  chatroom.sendUpdateToClients("");
		  
		  //Populate with all previous messages
		  if(chatroom.users.size() > 1)
		  {
			    //Data Store
				Key<Chat> thisChat = Key.create(Chat.class, chatroomKey);
				List<Message> messages = ObjectifyService.begin()
														.load()
														.type(Message.class)
														.ancestor(thisChat)
														.order("date")
														.list();
				
				//Memcache
				MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
				syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
				
				//Most recently added user
				String userId = chatroom.users.get(chatroom.users.size()-1);
				Logger.getAnonymousLogger().log(Level.INFO, "Sending History to " + userId);
       
		        //Most recently added user
		        Object message;
		        
		        for(int i = 1; i <= chatroom.count; i++)
		        {
		        	message = syncCache.get(chatroomKey + "_" + i);
		        	
		        	if(message != null) {
		        		chatroom.sendUpdateToUser(userId, message.toString());
		        	}
		        	else	//Get from data store
		        	{
		        		chatroom.sendUpdateToUser(userId, messages.get(i - 1).user + ": " + messages.get(i - 1).content);
		        	}
		        }
		        
		  }
		  
	      resp.setContentType("text/plain");
	      resp.getWriter().println("ok");
	  }
	  else {
		  resp.setStatus(401);
	  }
  }
}
