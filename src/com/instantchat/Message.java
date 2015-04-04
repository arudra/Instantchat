package com.instantchat;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;


@Entity
public class Message {
	@Parent Key<Chat> thisChat;
	@Id public Long id;
	@Index public Date date;
	
	public String content;
	public String user;
	
	public Message () {
		date = new Date();
	}

	public Message (String chat, String user, String msg)
	{
		this();
		
		if(chat != null)
		{
			thisChat = Key.create(Chat.class, chat);	//Creating Ancestor key
		}
		
		this.user = user;
		this.content = msg;
	}

	
	
	
}
