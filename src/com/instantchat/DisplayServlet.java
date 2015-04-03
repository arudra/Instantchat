package com.instantchat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class DisplayServlet extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		resp.setContentType("text/html");
	    PrintWriter out = resp.getWriter();
		
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
		
		out.println("<p> <a href =\"instantchat\">Create New Chat</a> </p>");
		out.println("</body>");
		out.println("</html>");
		
	}
}
