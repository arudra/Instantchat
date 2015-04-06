package com.instantchat;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

public class Init implements ServletContextListener{
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
 
    //Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Logger.getAnonymousLogger().log(Level.INFO, "ServletContextListener started");
		RoomList.createInstance();
		UserList.createInstance();
		
		ObjectifyService.register(Chat.class);
		ObjectifyService.register(Message.class);
	}

}
