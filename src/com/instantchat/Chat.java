package com.instantchat;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Chat {
	@Id public String chat;
}
