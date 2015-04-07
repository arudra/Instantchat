package com.instantchat;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditUserServlet extends HttpServlet {
	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		
		Logger.getAnonymousLogger().log(Level.INFO, "Updating User info");
		
		String first = req.getParameter("firstname");
		String last = req.getParameter("lastname");
		String bio = req.getParameter("bio");
		String gender = req.getParameter("gender");
		String age = req.getParameter("age");
		String city = req.getParameter("city");
		String country = req.getParameter("country");
		
		final UserService userService = UserServiceFactory.getUserService();
		String userId = userService.getCurrentUser().getNickname();
		
		
		User user = UserList.getInstance().getUser(userId);
		user.setFirstName(first);
		user.setLastName(last);
		user.setBio(bio);
		user.setGender(gender);
		user.setAge(age);
		user.setCity(city);
		user.setCountry(country);
		
		
		resp.setContentType("text/html");
	    resp.getWriter().write("<script>alert('User Information Updated!');");
	    resp.getWriter().write("window.location.assign('/display');</script>");
	    return;
	}

}
