package com.instantchat;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	public void deleteRoom (ChatRoom room) {
		Logger.getAnonymousLogger().log(Level.INFO, "Removing room");
		rooms.remove(room); 
	}
	
}
