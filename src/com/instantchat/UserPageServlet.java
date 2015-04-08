package com.instantchat;

import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UserPageServlet extends HttpServlet
{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		resp.setContentType("text/html");
	    
	    String userId = req.getParameter("user");
	    
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    User name = UserList.getInstance().getUser(userId);
	    User user = null;
	    
	    if (name != null) {
	    	user = pm.getObjectById(User.class, name.getKey());
	    }
	    else
	    {
	    	resp.getWriter().write("<script>alert('This user does not exist!');");
		    resp.getWriter().write("window.location.assign('/display');</script>");
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
