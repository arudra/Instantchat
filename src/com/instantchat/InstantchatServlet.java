package com.instantchat;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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
    resp.setContentType("text/html");
	PrintWriter out = resp.getWriter();
    
    if (userService.getCurrentUser() == null) 
    {
    	Logger.getAnonymousLogger().log(Level.INFO, "User is NULL");
    	
    	out.println("<html>");
    	out.println("  <head>");
    	out.println("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
    	out.println("    <title>Sign In</title>");
    	out.println("    <style>");
    	out.println("        body {");
    	out.println("        	font-family: 'WebSymbolsRegular', cursive;");
    	out.println("        }");
    	out.println("		body { ");
    	out.println("		  background: #f0f0f0; ");
    	out.println("		  color: #313131;");
    	out.println("		  line-height: 1; ");
    	out.println("		}");
    	out.println("		/** page structure **/");
    	out.println("		#w {");
    	out.println("		  display: block;");
    	out.println("		  width: 750px;");
    	out.println("		  margin: 0 auto;");
    	out.println("		  padding-top: 30px;");
    	out.println("		  padding-bottom: 45px;");
    	out.println("		}");
    	out.println("		#content {");
    	out.println("		  display: block;");
    	out.println("		  width: 100%;");
    	out.println("		  background: #fff;");
    	out.println("		  padding: 25px 20px;");
    	out.println("		  padding-bottom: 35px;");
    	out.println("		  -webkit-box-shadow: rgba(0, 0, 0, 0.1) 0px 1px 2px 0px;");
    	out.println("		  -moz-box-shadow: rgba(0, 0, 0, 0.1) 0px 1px 2px 0px;");
    	out.println("		  box-shadow: rgba(0, 0, 0, 0.1) 0px 1px 2px 0px;");
    	out.println("		}");
    	out.println("		.clearfix:after { content: \".\"; display: block; clear: both; visibility: hidden; line-height: 0; height: 0; }");
    	out.println("		.clearfix { display: inline-block; }");
    	out.println("		 ");
    	out.println("		html[xmlns] .clearfix { display: block; }");
    	out.println("		* html .clearfix { height: 1%; }");
    	out.println("        h1{");
    	out.println("        	margin:0px;");
    	out.println("        	padding:20px;");
    	out.println("        	font-size:45px;");
    	out.println("        	color:#000;");
    	out.println("            text-shadow:1px 1px 1px rgba(255,255,255,0.9);");
    	out.println("        	text-align:center;");
    	out.println("        	font-weight:400;");
    	out.println("        }");
    	out.println("        #buttonwrapper {");
    	out.println("        	text-align: center;");
    	out.println("        }");
    	out.println("        #signIn{");
    	out.println("        	border:1px solid #48d03e; -webkit-border-radius: 3px; -moz-border-radius: 3px;border-radius: 3px;font-size:15px;font-family:arial, helvetica, sans-serif; padding: 9px 10px 10px 10px; text-decoration:none; display:inline-block;font-weight:bold; color: #FFFFFF;");
    	out.println("        	background-color: #73DB6B; background-image: -webkit-gradient(linear, left top, left bottom, from(#73DB6B), to(#21C51C));");
    	out.println("        	background-image: -webkit-linear-gradient(top, #73DB6B, #21C51C);");
    	out.println("        	background-image: -moz-linear-gradient(top, #73DB6B, #21C51C);");
    	out.println("        	background-image: -ms-linear-gradient(top, #73DB6B, #21C51C);");
    	out.println("        	background-image: -o-linear-gradient(top, #73DB6B, #21C51C);");
    	out.println("        	background-image: linear-gradient(to bottom, #73DB6B, #21C51C);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#73DB6B, endColorstr=#21C51C);");
    	out.println("        	cursor:pointer;");
    	out.println("        }");
    	out.println("        #signIn:hover{");
    	out.println("        	border:1px solid #36b42c;");
    	out.println("        	background-color: #4cd142; background-image: -webkit-gradient(linear, left top, left bottom, from(#4cd142), to(#1a9816));");
    	out.println("        	background-image: -webkit-linear-gradient(top, #4cd142, #1a9816);");
    	out.println("        	background-image: -moz-linear-gradient(top, #4cd142, #1a9816);");
    	out.println("        	background-image: -ms-linear-gradient(top, #4cd142, #1a9816);");
    	out.println("        	background-image: -o-linear-gradient(top, #4cd142, #1a9816);");
    	out.println("        	background-image: linear-gradient(to bottom, #4cd142, #1a9816);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#4cd142, endColorstr=#1a9816);");
    	out.println("        }");
    	out.println("    </style>");
    	out.println("  </head>");
    	out.println("<body>");
    	out.println("  <div id=\"w\">");
    	out.println("  <div id=\"content\" class=\"clearfix\">");
    	out.println("    <h1>Please Sign In</h1>");
    	out.println("    <table>");
    	out.println("      <tr>");
    	out.println("      <div id='buttonwrapper'>");
    	out.println("	  <p><button id='signIn' onclick=\"window.location.assign('" + userService.createLoginURL("/display") +"')\">Sign In</button></p>");
    	out.println("	  </div>");
    	out.println("      </tr>");
    	out.println("    </table>");
    	out.println("  </div>");
    	out.println("  </div>");
    	out.println("  </body>");
    	out.println("</html>");
     
    	return;
    }
    
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
    	    resp.getWriter().write("<script>alert('This chat room does not exist!');");
		    resp.getWriter().write("window.location.assign('/display');</script>");
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
    	if (chatroomName == null)
    	{
    		resp.setContentType("text/html");
    		resp.getWriter().write("<script>alert('Chat room name can not be empty!');");
    	    resp.getWriter().write("window.location.assign('/display');</script>");
    	    return;
    	}
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
