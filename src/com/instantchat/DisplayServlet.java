package com.instantchat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class DisplayServlet extends HttpServlet
{
	private String getLoginURL (HttpServletRequest req, String chatroomKey) throws IOException 
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
	  
	  Logger.getAnonymousLogger().log(Level.INFO, "Login URL: " + uriWithOptionalChatRoomParam);
	      return uriWithOptionalChatRoomParam.toString();
	    } catch (URISyntaxException e) {
	      throw new IOException(e.getMessage());
	    }
	   
	}
	
	private String getLogoutURL (HttpServletRequest req) throws IOException
	{
		try {
			URI thisUri = new URI(req.getRequestURL().toString());
			URI logout = new URI(thisUri.getScheme(),
			    thisUri.getUserInfo(),
			    thisUri.getHost(),
			    thisUri.getPort(),
			    "",
			    "",
				"");

			return logout.toString();
			
		} catch (Exception e)
		{
			throw new IOException(e.getMessage());
		}
		
		
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		resp.setContentType("text/html");
	    PrintWriter out = resp.getWriter();
	    
	    String chatroomKey = req.getParameter("g");
	    final UserService userService = UserServiceFactory.getUserService();
	    
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    
	    //User Login
	    if (userService.getCurrentUser() == null) 
	    {
	    	Logger.getAnonymousLogger().log(Level.INFO, "User is NULL");
	    	
	    	resp.getWriter().println("<html>");
	    	resp.getWriter().println("  <head>");
	    	resp.getWriter().println("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
	    	resp.getWriter().println("    <title>Sign In</title>");
	    	resp.getWriter().println("    <style>");
	    	resp.getWriter().println("        body {");
	    	resp.getWriter().println("        	font-family: 'WebSymbolsRegular', cursive;");
	    	resp.getWriter().println("        }");
	    	resp.getWriter().println("        h1{");
	    	resp.getWriter().println("        	margin:0px;");
	    	resp.getWriter().println("        	padding:20px;");
	    	resp.getWriter().println("        	font-size:45px;");
	    	resp.getWriter().println("        	color:#000;");
	    	resp.getWriter().println("            text-shadow:1px 1px 1px rgba(255,255,255,0.9);");
	    	resp.getWriter().println("        	text-align:center;");
	    	resp.getWriter().println("        	font-weight:400;");
	    	resp.getWriter().println("        }");
	    	resp.getWriter().println("        #buttonwrapper {");
	    	resp.getWriter().println("        	text-align: center;");
	    	resp.getWriter().println("        }");
	    	resp.getWriter().println("        #signIn{");
	    	resp.getWriter().println("        	border:1px solid #48d03e; -webkit-border-radius: 3px; -moz-border-radius: 3px;border-radius: 3px;font-size:15px;font-family:arial, helvetica, sans-serif; padding: 9px 10px 10px 10px; text-decoration:none; display:inline-block;font-weight:bold; color: #FFFFFF;");
	    	resp.getWriter().println("        	background-color: #73DB6B; background-image: -webkit-gradient(linear, left top, left bottom, from(#73DB6B), to(#21C51C));");
	    	resp.getWriter().println("        	background-image: -webkit-linear-gradient(top, #73DB6B, #21C51C);");
	    	resp.getWriter().println("        	background-image: -moz-linear-gradient(top, #73DB6B, #21C51C);");
	    	resp.getWriter().println("        	background-image: -ms-linear-gradient(top, #73DB6B, #21C51C);");
	    	resp.getWriter().println("        	background-image: -o-linear-gradient(top, #73DB6B, #21C51C);");
	    	resp.getWriter().println("        	background-image: linear-gradient(to bottom, #73DB6B, #21C51C);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#73DB6B, endColorstr=#21C51C);");
	    	resp.getWriter().println("        	cursor:pointer;");
	    	resp.getWriter().println("        }");
	    	resp.getWriter().println("        #signIn:hover{");
	    	resp.getWriter().println("        	border:1px solid #36b42c;");
	    	resp.getWriter().println("        	background-color: #4cd142; background-image: -webkit-gradient(linear, left top, left bottom, from(#4cd142), to(#1a9816));");
	    	resp.getWriter().println("        	background-image: -webkit-linear-gradient(top, #4cd142, #1a9816);");
	    	resp.getWriter().println("        	background-image: -moz-linear-gradient(top, #4cd142, #1a9816);");
	    	resp.getWriter().println("        	background-image: -ms-linear-gradient(top, #4cd142, #1a9816);");
	    	resp.getWriter().println("        	background-image: -o-linear-gradient(top, #4cd142, #1a9816);");
	    	resp.getWriter().println("        	background-image: linear-gradient(to bottom, #4cd142, #1a9816);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#4cd142, endColorstr=#1a9816);");
	    	resp.getWriter().println("        }");
	    	resp.getWriter().println("    </style>");
	    	resp.getWriter().println("  </head>");
	    	resp.getWriter().println("<body>");
	    	resp.getWriter().println("    <h1>Please Sign In</h1>");
	    	resp.getWriter().println("    <table>");
	    	resp.getWriter().println("      <tr>");
	    	resp.getWriter().println("      <div id='buttonwrapper'>");
	    	resp.getWriter().println("	  <p><button id='signIn' onclick=\"window.location.assign('" + userService.createLoginURL(getLoginURL(req, chatroomKey)) +"')\">Sign In</button></p>");
	    	resp.getWriter().println("	  </div>");
	    	resp.getWriter().println("      </tr>");
	    	resp.getWriter().println("    </table>");
	    	resp.getWriter().println("  </body>");
	    	resp.getWriter().println("</html>");
	     
	    	return;
	    }
	    else
	    {
	    	String userId = userService.getCurrentUser().getNickname();
		    User user = UserList.getInstance().getUser(userId);
		    
		    if (user == null)
		    {
	    	    //Add User to Datastore
		    	user = new User(userId);
		    	user.setEmail(userService.getCurrentUser().getEmail());
		    	pm.makePersistent(user);
		    	
		    	//Add to global list
		    	UserList.getInstance().addUser(user);
		    	
		    }
	    }
		
		ArrayList<ChatRoom> list = RoomList.getInstance().getList();
		ChatRoom room;
		
		Logger.getAnonymousLogger().log(Level.INFO, "Displaying Chat Rooms");
		
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Display</title>");			
		out.println("<style>");
		out.println("body{");
		out.println("    font-family: 'WebSymbolsRegular', cursive;");
		out.println("}");
		out.println(".container{");
		out.println("	position:relative;");
		out.println("}");
		out.println(".content{");
		out.println("	position:relative;");
		out.println("}");
		out.println("h1{");
		out.println("	margin:0px;");
		out.println("	padding:20px;");
		out.println("	font-size:45px;");
		out.println("	color:#000;");
		out.println("    text-shadow:1px 1px 1px rgba(255,255,255,0.9);");
		out.println("	text-align:center;");
		out.println("	font-weight:400;");
		out.println("}");
		out.println(".ca-menu{");
		out.println("    padding: 0;");
		out.println("    margin: 20px auto;");
		out.println("    width: 300px;");
		out.println("}");
		out.println(".ca-menu li{");
		out.println("    width: 300px;");
		out.println("    height: 50px;");
		out.println("    overflow: hidden;");
		out.println("    display: block;");
		out.println("    background: #fff;");
		out.println("    -webkit-box-shadow: 1px 1px 2px rgba(0,0,0,0.2);");
		out.println("    -moz-box-shadow: 1px 1px 2px rgba(0,0,0,0.2);");
		out.println("    box-shadow: 1px 1px 2px rgba(0,0,0,0.2);");
		out.println("    margin-bottom: 4px;");
		out.println("    border-left: 5px solid #000;");
		out.println("    -webkit-transition: all 200ms ease-in-out;");
		out.println("	-moz-transition: all 200ms ease-in-out;");
		out.println("	-o-transition: all 200ms ease-in-out;");
		out.println("	-ms-transition: all 200ms ease-in-out;");
		out.println("	transition: all 200ms ease-in-out;");
		out.println("}");
		out.println(".ca-menu li:last-child{");
		out.println("    margin-bottom: 0px;");
		out.println("}");
		out.println(".ca-menu li a{");
		out.println("    text-align: left;");
		out.println("    display: block;");
		out.println("    width: 100%;");
		out.println("    height: 100%;");
		out.println("    color: #333;");
		out.println("    position:relative;");
		out.println("    text-decoration:none;");
		out.println("}");
		out.println(".ca-icon{");
		out.println("    font-family: 'WebSymbolsRegular', cursive;");
		out.println("    font-size: 30px;");
		out.println("    line-height: 45px;");
		out.println("    position: absolute;");
		out.println("    width: 200px;");
		out.println("    left: 50px;");
		out.println("    text-align: center;");
		out.println("    -webkit-transition: all 200ms linear;");
		out.println("    -moz-transition: all 200ms linear;");
		out.println("    -o-transition: all 200ms linear;");
		out.println("    -ms-transition: all 200ms linear;");
		out.println("    transition: all 200ms linear;");
		out.println("}");
		out.println(".ca-menu li:hover{");
		out.println("    border-color: #FF9900;");
		out.println("    background: #000;");
		out.println("}");
		out.println(".ca-menu li:hover .ca-icon{");
		out.println("    color: #FF9900;");
		out.println("    text-shadow: 0px 0px 1px #FF9900;");
		out.println("    font-size: 40px;");
		out.println("}");
		out.println(".ca-menu li:hover {");
		out.println("    color: #FF9900;");
		out.println("    font-size: 14px;");
		out.println("}");
		out.println(".ca-menu li:hover {");
		out.println("    color: #fff;");
		out.println("    font-size: 30px;");
		out.println("}");
		out.println("#newroom, #roompassword, #searchuser #edituser{");
		out.println("  width:100%;");
		out.println("  height:100%;");
		out.println("  opacity:.99;");
		out.println("  top:0;");
		out.println("  left:0;");
		out.println("  display:none;");
		out.println("  position:fixed;");
		out.println("  background-color:#313131;");
		out.println("  overflow:auto");
		out.println("}");
		out.println("img#close {");
		out.println("  position:absolute;");
		out.println("  right:-14px;");
		out.println("  top:-14px;");
		out.println("  cursor:pointer");
		out.println("}");
		out.println("div#popupContact {");
		out.println("  position:absolute;");
		out.println("  left:50%;");
		out.println("  top:17%;");
		out.println("  margin-left:-202px;");
		out.println("  font-family:'Raleway',sans-serif");
		out.println("}");
		out.println("form {");
		out.println("  max-width:300px;");
		out.println("  min-width:250px;");
		out.println("  padding:10px 50px;");
		out.println("  border:2px solid gray;");
		out.println("  border-radius:10px;");
		out.println("  font-family:raleway;");
		out.println("  background-color:#fff");
		out.println("}");
		out.println("p {");
		out.println("  margin-top:30px");
		out.println("}");
		out.println("h2 {");
		out.println("  background-color:#FFFFB1;");
		out.println("  padding:20px 35px;");
		out.println("  margin:-10px -50px;");
		out.println("  text-align:center;");
		out.println("  border-radius:10px 10px 0 0");
		out.println("}");
		out.println("hr {");
		out.println("  margin:10px -50px;");
		out.println("  border:0;");
		out.println("  border-top:1px solid #ccc");
		out.println("}");
		out.println("input[type=text] {");
		out.println("  width:82%;");
		out.println("  padding:10px;");
		out.println("  margin-top:30px;");
		out.println("  border:1px solid #ccc;");
		out.println("  padding-left:30px;");
		out.println("  font-size:16px;");
		out.println("  font-family:raleway");
		out.println("}");
		out.println("#roomname, #password, #roompass, #searchuserinput #Firstname #Lastname #Bio #Gender #Age #City #Country{");
		out.println("  width:100%;");
		out.println("  background-repeat:no-repeat;");
		out.println("  background-position:5px 7px");
		out.println("}");
		out.println("#submit, #join, #search {");
		out.println("  text-decoration:none;");
		out.println("  width:100%;");
		out.println("  text-align:center;");
		out.println("  display:block;");
		out.println("  background-color:#FFBC00;");
		out.println("  color:#3F2B08;");
		out.println("  border:1px solid #FFCB00;");
		out.println("  padding:10px 0;");
		out.println("  font-size:19px;");
		out.println("  cursor:pointer;");
		out.println("  border-radius:5px;");
		out.println("  font-family:arial, helvetica, sans-serif;");
		out.println("}");
		out.println("#edituserpopup{");
		out.println("  width:200px;");
		out.println("  height:45px;");
		out.println("  border:1px solid #ffad41; -webkit-border-radius: 3px; -moz-border-radius: 3px;border-radius: 3px;font-size:15px;font-family:arial, helvetica, sans-serif; padding: 9px 10px 10px 10px; text-decoration:none; display:inline-block;font-weight:bold; color: #FFFFFF;");
		out.println("  background-color: #ffc579; background-image: -webkit-gradient(linear, left top, left bottom, from(#ffc579), to(#fb9d23));");
		out.println("  background-image: -webkit-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: -moz-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: -ms-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: -o-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: linear-gradient(to bottom, #ffc579, #fb9d23);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#ffc579, endColorstr=#fb9d23);");
		out.println("  cursor:pointer");
		out.println("}");
		out.println("#edituserpopup:hover{");
		out.println("  border:1px solid #ff9913;");
		out.println("  background-color: #ffaf46; background-image: -webkit-gradient(linear, left top, left bottom, from(#ffaf46), to(#e78404));");
		out.println("  background-image: -webkit-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: -moz-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: -ms-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: -o-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: linear-gradient(to bottom, #ffaf46, #e78404);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#ffaf46, endColorstr=#e78404);");
		out.println("}");
		out.println("#searchuserpopup{");
		out.println("  width:200px;");
		out.println("  height:45px;");
		out.println("  border:1px solid #ffad41; -webkit-border-radius: 3px; -moz-border-radius: 3px;border-radius: 3px;font-size:15px;font-family:arial, helvetica, sans-serif; padding: 9px 10px 10px 10px; text-decoration:none; display:inline-block;font-weight:bold; color: #FFFFFF;");
		out.println("  background-color: #ffc579; background-image: -webkit-gradient(linear, left top, left bottom, from(#ffc579), to(#fb9d23));");
		out.println("  background-image: -webkit-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: -moz-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: -ms-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: -o-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: linear-gradient(to bottom, #ffc579, #fb9d23);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#ffc579, endColorstr=#fb9d23);");
		out.println("  cursor:pointer");
		out.println("}");
		out.println("#searchuserpopup:hover{");
		out.println("  border:1px solid #ff9913;");
		out.println("  background-color: #ffaf46; background-image: -webkit-gradient(linear, left top, left bottom, from(#ffaf46), to(#e78404));");
		out.println("  background-image: -webkit-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: -moz-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: -ms-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: -o-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: linear-gradient(to bottom, #ffaf46, #e78404);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#ffaf46, endColorstr=#e78404);");
		out.println("}");
		out.println("#newroompopup{");
		out.println("  width:200px;");
		out.println("  height:45px;");
		out.println("  border:1px solid #ffad41; -webkit-border-radius: 3px; -moz-border-radius: 3px;border-radius: 3px;font-size:15px;font-family:arial, helvetica, sans-serif; padding: 9px 10px 10px 10px; text-decoration:none; display:inline-block;font-weight:bold; color: #FFFFFF;");
		out.println("  background-color: #ffc579; background-image: -webkit-gradient(linear, left top, left bottom, from(#ffc579), to(#fb9d23));");
		out.println("  background-image: -webkit-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: -moz-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: -ms-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: -o-linear-gradient(top, #ffc579, #fb9d23);");
		out.println("  background-image: linear-gradient(to bottom, #ffc579, #fb9d23);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#ffc579, endColorstr=#fb9d23);");
		out.println("  cursor:pointer");
		out.println("}");
		out.println("#newroompopup:hover{");
		out.println("  border:1px solid #ff9913;");
		out.println("  background-color: #ffaf46; background-image: -webkit-gradient(linear, left top, left bottom, from(#ffaf46), to(#e78404));");
		out.println("  background-image: -webkit-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: -moz-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: -ms-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: -o-linear-gradient(top, #ffaf46, #e78404);");
		out.println("  background-image: linear-gradient(to bottom, #ffaf46, #e78404);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#ffaf46, endColorstr=#e78404);");
		out.println("}");
		out.println("#usersbutton{");
		out.println("  width:100%;");
		out.println("  height:45px;");
		out.println("  border:1px solid #616261; -webkit-border-radius: 3px; -moz-border-radius: 3px;border-radius: 3px;font-size:17px;font-family:arial, helvetica, sans-serif; padding: 9px 10px 10px 10px; text-decoration:none; display:inline-block;color: #FFFFFF;");
		out.println("  background-color: #7d7e7d; background-image: -webkit-gradient(linear, left top, left bottom, from(#7d7e7d), to(#0e0e0e));");
		out.println("  background-image: -webkit-linear-gradient(top, #7d7e7d, #0e0e0e);");
		out.println("  background-image: -moz-linear-gradient(top, #7d7e7d, #0e0e0e);");
		out.println("  background-image: -ms-linear-gradient(top, #7d7e7d, #0e0e0e);");
		out.println("  background-image: -o-linear-gradient(top, #7d7e7d, #0e0e0e);");
		out.println("  background-image: linear-gradient(to bottom, #7d7e7d, #0e0e0e);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#7d7e7d, endColorstr=#0e0e0e);");
		out.println("  cursor:pointer;");
		out.println("}");
		out.println("");
		out.println("#usersbutton:hover{");
		out.println("  border:1px solid #4a4b4a;");
		out.println("  background-color: #646464; background-image: -webkit-gradient(linear, left top, left bottom, from(#646464), to(#282828));");
		out.println("  background-image: -webkit-linear-gradient(top, #646464, #282828);");
		out.println("  background-image: -moz-linear-gradient(top, #646464, #282828);");
		out.println("  background-image: -ms-linear-gradient(top, #646464, #282828);");
		out.println("  background-image: -o-linear-gradient(top, #646464, #282828);");
		out.println("  background-image: linear-gradient(to bottom, #646464, #282828);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#646464, endColorstr=#282828);");
		out.println("}");
		out.println("#logout{");
		out.println(" width:200px;");
		out.println(" height:45px;");
		out.println(" border:1px solid #ff6262; -webkit-border-radius: 3px; -moz-border-radius: 3px;border-radius: 3px;font-size:15px;font-family:arial, helvetica, sans-serif; padding: 9px 10px 10px 10px; text-decoration:none; display:inline-block;font-weight:bold; color: #FFFFFF;");
		out.println(" background-color: #ff9a9a; background-image: -webkit-gradient(linear, left top, left bottom, from(#ff9a9a), to(#ff4040));");
		out.println(" background-image: -webkit-linear-gradient(top, #ff9a9a, #ff4040);");
		out.println(" background-image: -moz-linear-gradient(top, #ff9a9a, #ff4040);");
		out.println(" background-image: -ms-linear-gradient(top, #ff9a9a, #ff4040);");
		out.println(" background-image: -o-linear-gradient(top, #ff9a9a, #ff4040);");
		out.println(" background-image: linear-gradient(to bottom, #ff9a9a, #ff4040);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#ff9a9a, endColorstr=#ff4040);");
		out.println(" cursor:pointer;");
		out.println("}");
		out.println("");
		out.println("#logout:hover{");
		out.println(" border:1px solid #ff3434;");
		out.println(" background-color: #ff6767; background-image: -webkit-gradient(linear, left top, left bottom, from(#ff6767), to(#ff0d0d));");
		out.println(" background-image: -webkit-linear-gradient(top, #ff6767, #ff0d0d);");
		out.println(" background-image: -moz-linear-gradient(top, #ff6767, #ff0d0d);");
		out.println(" background-image: -ms-linear-gradient(top, #ff6767, #ff0d0d);");
		out.println(" background-image: -o-linear-gradient(top, #ff6767, #ff0d0d);");
		out.println(" background-image: linear-gradient(to bottom, #ff6767, #ff0d0d);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#ff6767, endColorstr=#ff0d0d);");
		out.println("}");
		out.println("#buttonwrapper{");
		out.println(" text-align:center;");
		out.println("}");
		out.println("</style>");
		out.println("</head>");
		
		out.println("<body id='body' style='overflow:hidden;'>");
		out.println("<h1>Chat Rooms</h1>");
		
		out.println("        <div class='container'>");
		out.println("            <div class='content'>");
		out.println("                <ul class='ca-menu'>");
		
		int i = 0;
		while (i < list.size())
		{
			room = list.get(i);
			out.println("                    <li>");
			Logger.getAnonymousLogger().log(Level.INFO, "room.getLink()" + room.getLink());
			Logger.getAnonymousLogger().log(Level.INFO, "room.getUsersSize()=" + room.getUsersSize());
			Logger.getAnonymousLogger().log(Level.INFO, "room.getUsers().toString()=" + room.getUsers().toString());
			out.println("                        <a href='#' onclick=\"div_roompassword_show('" + room.getLink() + "', '" + room.getUsers().toString() + "'); return false;\">");
			out.println("                            <span class='ca-icon'>" + room.getName() + "</span>");
			out.println("                        </a>");
			out.println("                    </li>");					
			i++;
		}
		out.println("                </ul>");
		out.println("            </div>");
		out.println("        </div>");
		
		out.println("<div id='newroom'>");
		out.println("<div id='popupContact'>");
		out.println("<form action=\"\\instantchat\" id='newroomform' method='get' name='form'>");
		out.println("<img id='close' src='/images/buttonClose.png' onclick ='div_newroom_hide()' height='30' width='30'>");
		out.println("<h2>Create New Chat Room</h2>");
		out.println("<hr>");
		out.println("<input id='roomname' name='roomname' placeholder='Chat Room Name' type='text'>");
		out.println("<input id='password' name='password' placeholder='Chat Room Password' type='text'>");
		out.println("<p></p>");
		out.println("<a href='javascript:%20check_empty()' id='submit'>Submit</a>");
		out.println("</form>");
		out.println("</div>");
		out.println("</div>");
		
		out.println("<div id='roompassword'>");
		out.println("<div id='popupContact'>");
		out.println("<form action='\' id='passwordform' method='get' name='passwordform'>");
		out.println("<img id='close' src='/images/buttonClose.png' onclick ='div_roompassword_hide()' height='30' width='30'>");
		out.println("<h2>Enter Password</h2>");
		out.println("<hr>");
		out.println("<p><button id='usersbutton' onclick='show_users()'></button></p>");
		out.println("<input id='roompass' name='password' placeholder='Chat Room Password' type='text'>");
		out.println("<p></p>");
		out.println("<a href='javascript:%20submit_password()' id='join'>Join</a>");
		out.println("</form>");
		out.println("</div>");
		out.println("</div>");	
		
		out.println("<div id='searchuser'>");
		out.println("<div id='popupContact'>");
		out.println("<form action='/userpage' id='searchuserform' method='get' name='searchuserform'>");
		out.println("<img id='close' src='/images/buttonClose.png' onclick ='div_searchuser_hide()' height='30' width='30'>");
		out.println("<h2>Enter User ID</h2>");
		out.println("<hr>");
		out.println("<input id='searchuserinput' name='user' placeholder='Search User ID' type='text'>");
		out.println("<p></p>");
		out.println("<a href='javascript:%20submit_searchuser()' id='search'>Search</a>");
		out.println("</form>");
		out.println("</div>");
		out.println("</div>");
		
		out.println("<div id='edituser'>");
		out.println("<div id='popupContact'>");
		out.println("<form action='/edituser' id='edituserform' method='get' name='edituserform'>");
		out.println("<img id='close' src='/images/buttonClose.png' onclick ='div_edituser_hide()' height='30' width='30'>");
		out.println("<h2>Enter Profile</h2>");
		out.println("<hr>");
		out.println("<input id='Firstname' name='firstname' placeholder='Enter First Name' type='text'>");
		out.println("<input id='Lastname' name='lastname' placeholder='Enter Last Name' type='text'>");
		out.println("<input id='Bio' name='bio' placeholder='Enter Bio' type='text'>");
		out.println("<input id='Gender' name='gender' placeholder='Enter Gender (M/F)' type='text'>");
		out.println("<input id='Age' name='age' placeholder='Enter Age' type='text'>");
		out.println("<input id='City' name='city' placeholder='Enter City' type='text'>");
		out.println("<input id='Country' name='country' placeholder='Enter Country' type='text'>");
		out.println("<p></p>");
		out.println("<a href='javascript:%20submit_edituser()' id='edit'>Update</a>");
		out.println("</form>");
		out.println("</div>");
		out.println("</div>");	
		
		
		out.println("<div id='buttonwrapper'>");
		out.println("<p><button id='newroompopup' onclick='div_newroom_show()'>Create New Room</button></p>");
		out.println("<p><button id='searchuserpopup' onclick='div_searchuser_show()'>Search User</button></p>");
		out.println("<p><button id='edituserpopup' onclick='div_edituser_show()'>Edit Profile</button></p>");
		out.println("<p><button id='logout' onclick=\"window.location.assign('" + userService.createLogoutURL(getLogoutURL(req)) + "')\">Logout</button></p>");
		//out.println("<p> <a href =" + userService.createLogoutURL(getLogoutURL(req)) + ">Logout</a> </p>");
		out.println("</div>");
		out.println("<script>");
		out.println("var clickedOnce = 0; ");
		out.println("var roomlink = ''; ");
		out.println("var roomusers = ''; ");
		out.println("function submit_edituser() {");
		out.println("  document.getElementById('edituserform').submit();");
		out.println("}");
		out.println("function submit_searchuser() {");
		//out.println("  setTimeout(function(){ window.location.assign('/userpage' + '?user=' + document.getElementById('searchuserinput').value); }, 100);");
		out.println("  document.getElementById('searchuserform').submit();");
		out.println("}");
		out.println("function submit_password() {");
		out.println("  setTimeout(function(){ window.location.assign(roomlink + '&password=' + document.getElementById('roompass').value); }, 100);");
		out.println("}");
		out.println("function check_empty() {");
		out.println("    if (document.getElementById('roomname').value == '') {");
		out.println("    alert('Chat room name can not be empty!');");
		out.println("    } else {");
		out.println("    clickedOnce=1;");
		out.println("    setTimeout(function(){ window.location.assign('\\instantchat?roomname=' + document.getElementById('roomname').value + '&password=' + document.getElementById('password').value); }, 100);");
		out.println("  }");
		out.println("}");
		out.println("function div_edituser_show() {");
		out.println("  document.getElementById('edituser').style.display = 'block';");
		out.println("}");
		out.println("function div_edituser_hide() {");
		out.println("  document.getElementById('edituser').style.display = 'none';");
		out.println("}");
		out.println("function div_searchuser_show() {");
		out.println("  document.getElementById('searchuser').style.display = 'block';");
		out.println("}");
		out.println("function div_searchuser_hide(){");
		out.println("  document.getElementById('searchuser').style.display = 'none';");
		out.println("}");
		out.println("function div_newroom_show() {");
		out.println("  document.getElementById('newroom').style.display = 'block';");
		out.println("}");
		out.println("function div_newroom_hide(){");
		out.println("  document.getElementById('newroom').style.display = 'none';");
		out.println("}");
		out.println("function div_roompassword_show(roomlinkpassed, roomuserspassed) {");
		out.println("  roomusers = roomuserspassed;");
		out.println("  roomlink = roomlinkpassed;");
		out.println("  var usercount = roomusers.split(',').length;");
		out.println("  document.getElementById('usersbutton').innerHTML = 'Users: ' + usercount;");
		out.println("  document.getElementById('roompassword').style.display = 'block';");
		out.println("}");
		out.println("function show_users() {");
		out.println("  alert(roomusers);");
		out.println("  setTimeout(function(){ window.location.assign('/display'); }, 100);");
		out.println("}");
		out.println("function div_roompassword_hide(){");
		out.println("  document.getElementById('roompassword').style.display = 'none';");
		out.println("}");
		out.println("function attachClicks () { ");
		out.println("  if (document.getElementById) { ");
		out.println("    document.getElementById('submit').onclick = function() {  ");
		out.println("      if (clickedOnce==1) {  ");
		out.println("        alert('Already Submitted'); return false;  ");
		out.println("      }    ");
		out.println("    };  ");
		out.println("  } ");
		out.println("} ");
		out.println("window.onload = function() { attachClicks(); };");
		out.println("</script>");
		out.println("</body>");
		out.println("</html>");
		
	}
}
