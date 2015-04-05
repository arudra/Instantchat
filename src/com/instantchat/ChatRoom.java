// Copyright 2010 Google Inc. All Rights Reserved.

package com.instantchat;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
/**
 * 
 *
 */
@PersistenceCapable
public class ChatRoom {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key key;	//Uniquely generated key for each chat (generated on create)
  
  @Persistent
  private String link;
  
  @Persistent
  private String name;
  
  @Persistent
  private String password;

  public ArrayList<String> users;
  
  public int count;
  
  ChatRoom() {
	    this.users = new ArrayList<String>();
	    this.link = "";
	    this.count = 0;
	    this.name = "";
	  }
  
  public void increment () { this.count += 1; }
  
  public void setLink(String link) {
	  Logger.getAnonymousLogger().log(Level.INFO, "Adding link: " + link);
	  this.link = link; 
  
  }
  
  public void setName(String name) {
	  Logger.getAnonymousLogger().log(Level.INFO, "Adding name: " + name);
	  this.name = name; 
  }
  
  public void setPassword(String password) {
	  this.password = password; 
  }
  
  public String getLink () { return this.link; }

  public Key getKey() {
    return key;
  }

  public String getUser(String userId) {
	for (int i = 0; i < users.size(); i++)
	{
		if (users.get(i).equals(userId))
		{
			return users.get(i);
		}
	}
    return null;
  }
  
  public String getName() {
		return this.name;
	  }
  public String getPassword() {
		return this.password;
	  }

  public int getUsersSize() {
		return this.users.size();
	  }

  public boolean addUser(String user) {
	  for (int i = 0; i < users.size(); i++)
		{
		  if (users.get(i).equals(user))
		  {
			return false;
		  }
		}
    this.users.add(user);
    return true;
  }
  
  public boolean removeUser(String user) {
	  return this.users.remove(user);
  }

  public String getMessageString(String user, String msg) {
    Map<String, String> state = new HashMap<String, String>();
    state.put("user", user);
    state.put("msg", msg);
    JSONObject message = new JSONObject(state);
    return message.toString();
  }

  public String getChannelKey(String user) {
    return user + KeyFactory.keyToString(key);
  }

  public void sendUpdateToUser(String user, String msg) {
    if (user != null) {
      ChannelService channelService = ChannelServiceFactory.getChannelService();
      String channelKey = getChannelKey(user);
      channelService.sendMessage(new ChannelMessage(channelKey, getMessageString(user, msg)));
    }
  }

  public void sendUpdateToClients(String msg) {
	  for (int i = 0; i < users.size(); i++)
		{
		  sendUpdateToUser(users.get(i), msg);
		}
  }
  
  private void sendMsgToUser(String user, String msg) {
	    if (user != null) {
	      ChannelService channelService = ChannelServiceFactory.getChannelService();
	      String channelKey = getChannelKey(user);
	      channelService.sendMessage(new ChannelMessage(channelKey, getMessageString(user, msg)));
	    }
	  }

	  public void sendMsgToClients(String msg) {
		  for (int i = 0; i < users.size(); i++)
			{
			  sendMsgToUser(users.get(i), msg);
			}
	  }
  
  public void sendMsg(String user, String msg) {
	  sendUpdateToClients(user + ": " + msg);
	  }
}