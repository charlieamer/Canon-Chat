package com.charlieamer.cannonchat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.widget.ArrayAdapter;

public class RoomAdapter extends ArrayAdapter<String> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1783037807882105349L;
	HashMap<String, Integer> rooms;

	RoomAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1, new ArrayList<String>());
		this.rooms = new HashMap<String, Integer> ();
	}
	
	private void RefreshMessages() {
		clear();
		for (String name : rooms.keySet()) {
			int value = rooms.get(name);
			if (value == 0)
				add(name);
			else
				add("(" + value + ") " + name);
		}
		notifyDataSetChanged();
	}
	
	public void AddMessage(String roomName) {
		int value = rooms.get(roomName);
		rooms.put(roomName, value + 1);
		RefreshMessages();
	}
	
	public void RemoveMessages(String roomName) {
		rooms.put(roomName, 0);
		RefreshMessages();
	}
	
	public void RemoveRoom(String roomName) {
		rooms.remove(roomName);
		RefreshMessages();
	}
	
	public void AddRoom(String roomName) {
		rooms.put(roomName, 0);
		RefreshMessages();
	}

}
