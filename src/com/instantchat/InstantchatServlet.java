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
  private String getChatRoomUriWithChatRoomParam(HttpServletRequest req,
      String chatroomKey) throws IOException 
  {
    try {
    	
      String query;
      if (chatroomKey == null) {
        query = "";
      } else {
        query = "g=" + chatroomKey;
      }
      
      Logger.getAnonymousLogger().log(Level.INFO, "Getting link, query: " + query);
      
      //Make sure to always return instant chat URL which leads to chat room
      //URI thisUri = new URI(URL.substring(0, URL.lastIndexOf('/') + 1) + "instantchat" );
      URI thisUri = new URI(req.getRequestURL().toString());
      URI uriWithOptionalChatRoomParam = new URI(thisUri.getScheme(),
          thisUri.getUserInfo(),
          thisUri.getHost(),
          thisUri.getPort(),
          thisUri.getPath(),
          query,
          "");
      
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
      
      
      return uriWithOptionalChatRoomParam.toString();
    } catch (URISyntaxException e) {
      throw new IOException(e.getMessage());
    }
   
  }
 
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
  {  
    final UserService userService = UserServiceFactory.getUserService();
    String chatroomKey = req.getParameter("g");
    
    final RoomList roomList = RoomList.getInstance();
    
    if (userService.getCurrentUser() == null) 
    {
    	Logger.getAnonymousLogger().log(Level.INFO, "User is NULL");      
    	resp.getWriter().println("<p>Please <a href=\"" + 
    			userService.createLoginURL(getChatRoomUriWithChatRoomParam(req, chatroomKey)) + "\">sign in</a>.</p>");
     
      return;
    }
   
    PersistenceManager pm = PMF.get().getPersistenceManager();
   
    ChatRoom chatroom = null;
    String userId = userService.getCurrentUser().getNickname();
    Logger.getAnonymousLogger().log(Level.INFO, "User: " + userId.toString());
    boolean newjoined = false;
    boolean newroom = false;
    
    if (chatroomKey != null) 
    {
    	//Chat room exists
    	chatroom = pm.getObjectById(ChatRoom.class, KeyFactory.stringToKey(chatroomKey));
    	newjoined = chatroom.addUser(userId);
    	Logger.getAnonymousLogger().log(Level.INFO, "NEW USER JOINED");  
    } 
    else {
    	//Create chat room
    	Logger.getAnonymousLogger().log(Level.INFO, "Create chatroom");
    	//chatroom = new ChatRoom(userId);
    	chatroom = new ChatRoom();
    	pm.makePersistent(chatroom);
    	chatroomKey = KeyFactory.keyToString(chatroom.getKey());
    	
    	//Add to global list
    	roomList.addRoom(chatroom);
    	//Setlink
    	getChatRoomUriWithChatRoomParam(req, chatroomKey);
    	newroom = true;
    }
    pm.close();

	//Redirect to chatroom url
    if (newroom == true)
    {
      resp.sendRedirect(resp.encodeRedirectURL("/instantchat?g=" + chatroomKey + "#"));
      return;
    }
    
    ChannelService channelService = ChannelServiceFactory.getChannelService();
    String token = channelService.createChannel(chatroom.getChannelKey(userId));
    
    Logger.getAnonymousLogger().log(Level.INFO, "Creating channel");  
    
    FileReader reader = new FileReader("index-template");
    CharBuffer buffer = CharBuffer.allocate(16384);
    reader.read(buffer);
    String index = new String(buffer.array());
    index = index.replaceAll("\\{\\{ chatroom_key \\}\\}", chatroomKey);
    index = index.replaceAll("\\{\\{ token \\}\\}", token);
   
    resp.setContentType("text/html");
    resp.getWriter().write(index);
    reader.close();
    if (newjoined)
    {
    	Logger.getAnonymousLogger().log(Level.INFO, "NEW USER JOINED - SENDING MSG TO EVERYONE");
    	chatroom.sendMsgToClients(userId + " has joined the chatroom");
    }
  }
}
