package com.instantchat;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This global class holds the current
 * list of all Users.
 */
public class UserList {
	
	private ArrayList<User> users;
	
	private static UserList instance;
	
	public static void createInstance() { instance = new UserList(); }
	
	private UserList () {
		users = new ArrayList<>();
		Logger.getAnonymousLogger().log(Level.INFO, "users was initialised!");
	}
	
	public static UserList getInstance() { return instance; }
	
	public ArrayList<User> getList () { return users; }
	
	public void addUser (User user) {
		Logger.getAnonymousLogger().log(Level.INFO, "Adding new user");
		users.add(user); 
	}
	
	
	public User getUser(String userId) {
		for (int i = 0; i < users.size(); i++)
		{
			if (users.get(i).getUserId().equals(userId))
			{
				return users.get(i);
			}
		}
		return null;
	}
	
}
