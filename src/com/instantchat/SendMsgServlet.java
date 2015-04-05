package com.instantchat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.*;

@SuppressWarnings("serial")
public class SendMsgServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	  Logger.getAnonymousLogger().log(Level.INFO, "Send message");
	    
    UserService userService = UserServiceFactory.getUserService();
    
    Message message;
    
    //Get chat ID
    String chatroomKey = req.getParameter("chatroomkey");
    
    //Get message
    String msg = req.getParameter("m");
    PersistenceManager pm = PMF.get().getPersistenceManager();
    
    //Get chat room instance
    ChatRoom chatroom = pm.getObjectById(ChatRoom.class, KeyFactory.stringToKey(chatroomKey));
   
    //Get user
    String currentUserId = userService.getCurrentUser().getNickname();
    
    //check if control message
    Logger.getAnonymousLogger().log(Level.INFO, "msg = " + msg);
    if (msg.startsWith("/"))
    {
    	if (msg.equals("/exit"))
    	{
    		chatroom.sendMsgToClients("/" + currentUserId + " has left the chat room.");
    		chatroom.removeUser(currentUserId);
    		if (chatroom.getUsersSize() == 0)
    		{
    			//Destory chatroom
    			RoomList.getInstance().deleteRoom(KeyFactory.stringToKey(chatroomKey));
    			pm.deletePersistent(chatroom);
    		}
    	}
    	else if (msg.equals("/help"))
    	{
    		//Send help message
    		chatroom.sendUpdateToUser(currentUserId, "/Type '/exit' to exit the chat room.");
    	}
    	else
    	{
    		chatroom.sendUpdateToUser(currentUserId, "/Unrecognized Command.");
    	}
    }
    else
    {
    	//Send message
    	chatroom.sendMsg(currentUserId, msg);
    	chatroom.increment();
    	
    	//Save to datastore
    	message = new Message(chatroomKey, currentUserId, msg);
    	ObjectifyService.begin().save().entity(message).now();
    	
    	//Save to Memcache
    	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
        Logger.getAnonymousLogger().log(Level.INFO, "Saving to memcache. KEY-> " + chatroomKey + "_" + chatroom.count 
        												+ " VALUE-> " + currentUserId + ": " + msg);
        
        // Key = chatroomKey_# --------- Value = userID: msg
        syncCache.put(chatroomKey + "_" + chatroom.count, currentUserId + ": " + msg);
        
    }
    pm.close();
  }
}