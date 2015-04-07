package com.instantchat;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class InstantchatServlet extends HttpServlet 
{	
  private void setLink(HttpServletRequest req, String chatroomKey) throws IOException 
  {
    try {
    	
      String query;
      if (chatroomKey == null) {
        query = "";
      } else {
        query = "g=" + chatroomKey;
      }
      
      //Make sure to always return instant chat URL which leads to chat room
      URI thisUri = new URI(req.getRequestURL().toString());
      URI uriWithOptionalChatRoomParam = new URI(thisUri.getScheme(),
          thisUri.getUserInfo(),
          thisUri.getHost(),
          thisUri.getPort(),
          thisUri.getPath(),
          query,
          null);
      
      //Store chat room link
      ArrayList <ChatRoom> list = RoomList.getInstance().getList();
      String key;
      for(int i = 0; i < list.size(); i++)
      {
    	  key = KeyFactory.keyToString(list.get(i).getKey());
    	  if(key.equals(chatroomKey))
    	  {
    		  list.get(i).setLink(uriWithOptionalChatRoomParam.toString());
    		  break;
    	  }
      }
      
      Logger.getAnonymousLogger().log(Level.INFO, "Login URL: " + uriWithOptionalChatRoomParam);
    } catch (URISyntaxException e) { throw new IOException(e.getMessage()); }
    
  }
 
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
  {  
    final UserService userService = UserServiceFactory.getUserService();
    String chatroomKey = req.getParameter("g");
    
    final RoomList roomList = RoomList.getInstance();

    PersistenceManager pm = PMF.get().getPersistenceManager();
   
    ChatRoom chatroom = null;
    String userId = userService.getCurrentUser().getNickname();
    Logger.getAnonymousLogger().log(Level.INFO, "User: " + userId.toString());
    boolean newjoined = false;
    boolean newroom = false;
    String chatroomName = "";
	String chatroomPassword = "";
    
    if (chatroomKey != null) 
    {
    	//Chat room exists
    	try {
    		chatroom = pm.getObjectById(ChatRoom.class, KeyFactory.stringToKey(chatroomKey));
    	} catch (Exception e) {
    		resp.setContentType("text/html");
    	    resp.getWriter().write("This chat room does not exist");
    	    return;
    	}
    	chatroomPassword = req.getParameter("password");
    	if (chatroomPassword == null)
    	{
    		chatroomPassword = "";
    	}
    	if (!(chatroomPassword.equals(chatroom.getPassword())))
    	{
    		resp.setContentType("text/html");
    		resp.getWriter().write("<script>alert('Wrong Password!');");
    	    resp.getWriter().write("window.location.assign('/display');</script>");
    	    return;
    	}
    	//Got to add room in the list copy
    	ChatRoom roomFromList = roomList.getRoom(chatroom.getKey());
    	newjoined = chatroom.addUser(userId); 
    	roomFromList.addUser(userId);
    	Logger.getAnonymousLogger().log(Level.INFO, "room.getUsers().toString()=" + chatroom.getUsers().toString());
    	Logger.getAnonymousLogger().log(Level.INFO, "roomList.get(roomListIndex).getUsers().toString()=" + roomFromList.getUsers().toString());
    } 
    else {
    	//Create chat room
    	Logger.getAnonymousLogger().log(Level.INFO, "Create chatroom");
    	chatroomName = req.getParameter("roomname");
    	chatroomPassword = req.getParameter("password");
    	if (chatroomPassword == null)
    	{
    		chatroomPassword = "";
    	}
    	chatroom = new ChatRoom();
    	chatroom.setName(chatroomName);
    	chatroom.setPassword(chatroomPassword);
    	pm.makePersistent(chatroom);
    	chatroomKey = KeyFactory.keyToString(chatroom.getKey());
    	
    	//Add to global list
    	roomList.addRoom(chatroom);
    	//Setlink
    	setLink(req, chatroomKey);
    	newroom = true;
    }
    pm.close();

	//Redirect to chatroom url
    if (newroom == true)
    {
      resp.sendRedirect(resp.encodeRedirectURL("/instantchat?g=" + chatroomKey + "&password=" + chatroomPassword));
      return;
    }
    
    Logger.getAnonymousLogger().log(Level.INFO, "Creating channel");
    ChannelService channelService = ChannelServiceFactory.getChannelService();
    String token = channelService.createChannel(chatroom.getChannelKey(userId));

    FileReader reader = new FileReader("index-template");
    CharBuffer buffer = CharBuffer.allocate(16384);
    reader.read(buffer);
    String index = new String(buffer.array());
    index = index.replaceAll("\\{\\{ chatroom_key \\}\\}", chatroomKey);
    index = index.replaceAll("\\{\\{ token \\}\\}", token);
    index = index.replaceAll("\\{\\{ me \\}\\}", userId);
   
    resp.setContentType("text/html");
    resp.getWriter().write(index);
    reader.close();
    if (newjoined)
    {
    	try {
    	Thread.sleep(1000);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	chatroom.sendMsgToClients("/" + userId + " has joined the chat room.");
    }
  }
}
