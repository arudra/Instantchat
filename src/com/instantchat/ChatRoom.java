// Copyright 2010 Google Inc. All Rights Reserved.

package com.instantchat;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

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
  private String userX;

  @Persistent
  private String userO;

  @Persistent
  private String msg;
  
  @Persistent
  private String link;

  ChatRoom(String userX, String userO, String msg) 
  {
	  Logger.getAnonymousLogger().log(Level.INFO, "Chatroom created");
    this.userX = userX;
    this.userO = userO;
    this.msg = msg;
    this.link = "";
  }
  
  public void setLink(String link) {
	  Logger.getAnonymousLogger().log(Level.INFO, "Adding link: " + link);
	  this.link = link; 
  }
  
  public String getLink () { return this.link; }

  public Key getKey() {
    return key;
  }

  public String getUserX() {
    return userX;
  }

  public String getUserO() {
    return userO;
  }

  public void setUserO(String userO) {
    this.userO = userO;
  }
  
  public void setMsg(String msg) {
	    this.msg = msg;
	  }

  public String getMessageString() {
    Map<String, String> state = new HashMap<String, String>();
    state.put("userX", userX);
    if (userO == null) {
      state.put("userO", "");
    } else {
      state.put("userO", userO);
    }
    state.put("msg", msg);
    JSONObject message = new JSONObject(state);
    return message.toString();
  }

  public String getChannelKey(String user) {
    return user + KeyFactory.keyToString(key);
  }

  private void sendUpdateToUser(String user) {
    if (user != null) {
      ChannelService channelService = ChannelServiceFactory.getChannelService();
      String channelKey = getChannelKey(user);
      channelService.sendMessage(new ChannelMessage(channelKey, getMessageString()));
    }
  }

  public void sendUpdateToClients() {
    sendUpdateToUser(userX);
    sendUpdateToUser(userO);
  }
  
  private void sendMsgToUser(String user, String msg) {
	    if (user != null) {
	      ChannelService channelService = ChannelServiceFactory.getChannelService();
	      String channelKey = getChannelKey(user);
	      channelService.sendMessage(new ChannelMessage(channelKey, msg));
	    }
	  }

	  public void sendMsgToClients(String msg) {
	    sendMsgToUser(userX, msg);
	    sendMsgToUser(userO, msg);
	  }
  
  public void sendMsg(String user, String msg) {
	  setMsg(user + ": " + msg);
	  sendUpdateToClients();
	  }
}