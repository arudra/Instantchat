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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class InstantchatServlet extends HttpServlet 
{	
  public static String getChatRoomUriWithChatRoomParam(HttpServletRequest req,
      String chatroomKey) throws IOException 
  {
    try {
      String query;
      if (chatroomKey == null) {
        query = "";
      } else {
        query = "g=" + chatroomKey;
      }
      
      String URL = req.getRequestURL().toString();
      
      //Make sure to always return instant chat URL which leads to chat room
      URI thisUri = new URI(URL.substring(0, URL.lastIndexOf('/') + 1) + "instantchat" );
      URI uriWithOptionalChatRoomParam = new URI(thisUri.getScheme(),
          thisUri.getUserInfo(),
          thisUri.getHost(),
          thisUri.getPort(),
          thisUri.getPath(),
          query,
          "");
      return uriWithOptionalChatRoomParam.toString();
    } catch (URISyntaxException e) {
      throw new IOException(e.getMessage());
    }
   
  }
 
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
  {  
    final UserService userService = UserServiceFactory.getUserService();
    final URI uriWithOptionalChatRoomParam;
    String chatroomKey = req.getParameter("g");
    
    final RoomList roomList = RoomList.getInstance();
    
    
    if (userService.getCurrentUser() == null) 
    {
    	Logger.getAnonymousLogger().log(Level.INFO, "User is NULL");
    	String thisURL = req.getRequestURL().toString();      
    	resp.getWriter().println("<p>Please <a href=\"" + 
    			userService.createLoginURL(getChatRoomUriWithChatRoomParam(req, chatroomKey)) + "\">sign in</a>.</p>");
     
      return;
    }
   
    PersistenceManager pm = PMF.get().getPersistenceManager();
   
    ChatRoom chatroom = null;
    String userId = userService.getCurrentUser().getUserId();
    Logger.getAnonymousLogger().log(Level.INFO, "User: " + userId.toString());
    
    if (chatroomKey != null) 
    {
    	//Chat room exists
    	chatroom = pm.getObjectById(ChatRoom.class, KeyFactory.stringToKey(chatroomKey));
    	if (chatroom.getUserO() == null && !userId.equals(chatroom.getUserX())) {
    		chatroom.setUserO(userId);
    	}
    } 
    else {
    	//Create chat room
    	Logger.getAnonymousLogger().log(Level.INFO, "Create chatroom");
    	chatroom = new ChatRoom(userId, null, "");
    	pm.makePersistent(chatroom);
    	chatroomKey = KeyFactory.keyToString(chatroom.getKey());
    	
    	//Add to global list
    	roomList.addRoom(chatroom);
    }
    pm.close();
   
    ChannelService channelService = ChannelServiceFactory.getChannelService();
    String token = channelService.createChannel(chatroom.getChannelKey(userId));

    FileReader reader = new FileReader("index-template");
    CharBuffer buffer = CharBuffer.allocate(16384);
    reader.read(buffer);
    String index = new String(buffer.array());
    index = index.replaceAll("\\{\\{ chatroom_key \\}\\}", chatroomKey);
    index = index.replaceAll("\\{\\{ me \\}\\}", userId);
    index = index.replaceAll("\\{\\{ token \\}\\}", token);
    index = index.replaceAll("\\{\\{ initial_message \\}\\}", chatroom.getMessageString());
    index = index.replaceAll("\\{\\{ chatroom_link \\}\\}", getChatRoomUriWithChatRoomParam(req, chatroomKey));
   
    resp.setContentType("text/html");
    resp.getWriter().write(index);
    reader.close();
  }
}
