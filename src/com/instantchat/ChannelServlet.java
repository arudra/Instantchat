package com.instantchat;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;


@SuppressWarnings("serial")
public class ChannelServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		ChannelPresence presence = channelService.parsePresence(req);
		
		Logger.getAnonymousLogger().log(Level.INFO, "Client: " + presence.clientId() );
		
		/*
		 * //Check if new user recently added
    	Logger.getAnonymousLogger().log(Level.INFO, "New user in " + chatroom);
    	//Populate with all previous messages
    	Key<Chat> thisChat = Key.create(Chat.class, chatroomKey);
    	
    	List<Message> messages = ObjectifyService.begin()
    											.load()
    											.type(Message.class)
    											.ancestor(thisChat)
    											.order("-date")
    											.list();
    	
    	//Most recently added user
    	String userId = chatroom.users.get(chatroom.users.size()-1);
    	Logger.getAnonymousLogger().log(Level.INFO, "Sending History to " + userId);

		for(int i = messages.size() - 1; i >= 0; i--)
		{
			Logger.getAnonymousLogger().log(Level.INFO, "History: " + messages.get(i).user + ": " + messages.get(i).content);
			chatroom.sendUpdateToUser(userId, messages.get(i).user + ": " + messages.get(i).content);
		}
		 * 
		 * 
		 */
	}

}
