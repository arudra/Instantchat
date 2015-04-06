package com.instantchat;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class UserPageServlet extends HttpServlet
{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		resp.setContentType("text/html");
	    
	    String userId = req.getParameter("user");
	    User user = UserList.getInstance().getUser(userId);
	    
	    if (user == null)
	    {
    	    resp.getWriter().write("This user does not exist");
    	    return;
	    }
		
		Logger.getAnonymousLogger().log(Level.INFO, "Displaying Chat Rooms");
		
		FileReader reader = new FileReader("user-template");
	    CharBuffer buffer = CharBuffer.allocate(16384);
	    reader.read(buffer);
	    String index = new String(buffer.array());
	    index = index.replaceAll("\\{\\{ userId \\}\\}", userId);
	    index = index.replaceAll("\\{\\{ bio \\}\\}", user.getBio());
	    index = index.replaceAll("\\{\\{ picurl \\}\\}", user.getPicurl());
	    index = index.replaceAll("\\{\\{ firstname \\}\\}", user.getFirstName());
	    index = index.replaceAll("\\{\\{ lastname \\}\\}", user.getLastName());
	    index = index.replaceAll("\\{\\{ gender \\}\\}", user.getGender());
	    index = index.replaceAll("\\{\\{ age \\}\\}", user.getAge());
	    index = index.replaceAll("\\{\\{ city \\}\\}", user.getCity());
	    index = index.replaceAll("\\{\\{ country \\}\\}", user.getCountry());
	    index = index.replaceAll("\\{\\{ email \\}\\}", user.getEmail());
	   
	    resp.getWriter().write(index);
	    reader.close();
		
	}
}
