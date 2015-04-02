// Copyright 2010 Google Inc. All Rights Reserved.

package com.instantchat;

import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 *
 */

@SuppressWarnings("serial")
public class OpenedServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException 
  {
	  Logger.getAnonymousLogger().log(Level.INFO, "Opened Servlet");
	  String chatroomId = req.getParameter("chatroomkey");
	  PersistenceManager pm = PMF.get().getPersistenceManager();
	  ChatRoom chatroom = pm.getObjectById(ChatRoom.class, KeyFactory.stringToKey(chatroomId));
	  if (chatroomId != null && req.getUserPrincipal() != null) {
		  chatroom.sendUpdateToClients();
	      resp.setContentType("text/plain");
	      resp.getWriter().println("ok");
	  }
	  else {
		  resp.setStatus(401);
	  }
  }
}
