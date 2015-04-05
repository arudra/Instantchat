package com.instantchat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class DisplayServlet extends HttpServlet
{
	
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
	    
	    final UserService userService = UserServiceFactory.getUserService();
		
		ArrayList<ChatRoom> list = RoomList.getInstance().getList();
		ChatRoom room;
		
		Logger.getAnonymousLogger().log(Level.INFO, "Displaying Chat Rooms");
		
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Display</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Chat Rooms</h1>");
		
		int i = 0;
		while (i < list.size())
		{
			room = list.get(i);
			out.println("<p> <a href = " + room.getLink() + ">Chat Room " + i + "</a> </p>");
			i++;
		}
		
		out.println("<p> <a href =\"instantchat\">Create New Chat Room</a> </p>");
		out.println("<p> <a href =" + userService.createLogoutURL(getLogoutURL(req)) + ">Logout</a> </p>");
		out.println("</body>");
		out.println("</html>");
		
	}
}
