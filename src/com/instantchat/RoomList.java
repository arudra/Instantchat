package com.instantchat;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Key;

/*
 * This global class holds the current
 * list of available chat rooms.
 */
public class RoomList {
	
	private ArrayList<ChatRoom> rooms;
	
	private static RoomList instance;
	
	public static void createInstance() { instance = new RoomList(); }
	
	private RoomList () {
		rooms = new ArrayList<>();
		Logger.getAnonymousLogger().log(Level.INFO, "rooms was initialised!");
	}
	
	public static RoomList getInstance() { return instance; }
	
	public ArrayList<ChatRoom> getList () { return rooms; }
	
	public void addRoom (ChatRoom room) {
		Logger.getAnonymousLogger().log(Level.INFO, "Adding new room");
		rooms.add(room); 
	}
	
	public void deleteRoom (Key chatroomkey) {
		Logger.getAnonymousLogger().log(Level.INFO, "Removing room");
		boolean retval = false;
		for (int i = 0; i < rooms.size(); i++)
		{
			if (rooms.get(i).getKey().equals(chatroomkey))
			{
				//TODO delete room
				rooms.remove(i);
			}
		}
		Logger.getAnonymousLogger().log(Level.INFO, "Retval = " + retval);
	}
	
	public ChatRoom getRoom(Key chatroomkey) {
		for (int i = 0; i < rooms.size(); i++)
		{
			if (rooms.get(i).getKey().equals(chatroomkey))
			{
				return rooms.get(i);
			}
		}
		return null;
	}
	
}
