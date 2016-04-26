package com.charlieamer.cannonchat;

import java.util.ArrayList;
import java.util.HashMap;

import com.charlieamer.cannonchat.client.ChatListener;
import com.charlieamer.cannonchat.client.RoomInfo;
import com.charlieamer.cannonchat.client.UserInfo;
import com.google.gson.Gson;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends ActionBarActivity implements
NavigationDrawerFragment.NavigationDrawerCallbacks, ChatListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private ChatFragment chatFragment;
	private HashMap<String, UserInfo> users;
	private HashMap<String, ArrayList<Message>> messages;
	private HashMap<String, RoomInfo> rooms;
	private ChatBroadcastReceiver receiver;

	private String currentRoom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		receiver = ChatBroadcastReceiver.GenerateReceiver(this, this);
		
		if (savedInstanceState == null || !savedInstanceState.containsKey("messages")) {
			messages = new HashMap<>();
			messages.put("global", new ArrayList<Message>());
			rooms = new HashMap<>();
			users = new HashMap<>();
			mNavigationDrawerFragment.getAdapter().AddRoom("global");
		} else {
			messages = (HashMap<String, ArrayList<Message>>) savedInstanceState.getSerializable("messages");
			users = (HashMap<String, UserInfo>) savedInstanceState.getSerializable("users");
			rooms = (HashMap<String, RoomInfo>) savedInstanceState.getSerializable("rooms");
			currentRoom = savedInstanceState.getString("currentRoom");
			mNavigationDrawerFragment.setAdapter((RoomAdapter) savedInstanceState.getSerializable("roomStatus"));
		}
		ChangeRoom("global");
		receiver.getInterpreter().GetRoomInfo(currentRoom);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Gson gson = new Gson();
		outState.putSerializable("messages", messages);
		outState.putSerializable("users", users);
		outState.putSerializable("rooms", rooms);
		outState.putSerializable("roomStatus", mNavigationDrawerFragment.getAdapter());
		outState.putString("currentRoom", currentRoom);
		super.onSaveInstanceState(outState);
	}
	
	private void AddMessage(String room, Message m) {
		messages.get(room).add(m);
		if (!room.equals(room)) {
			mNavigationDrawerFragment.getAdapter().AddMessage(room);
		} else {
			if (chatFragment != null)
				chatFragment.AddMessage(m);
		}
	}

	private void AddMessage(String room, String string) {
		AddMessage(room, new Message(null, string));
	}
	
	public void ChangeRoom(String roomName) {
		currentRoom = roomName;
		ArrayList<Message> roomMessages = messages.get(roomName);

		chatFragment = new ChatFragment();
		Bundle b = new Bundle();
		ChatFragment.MessagesToBundle(roomMessages, b);
		chatFragment.setArguments(b);

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
		.beginTransaction()
		.replace(R.id.container, chatFragment).commit();
		
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		if (mNavigationDrawerFragment != null) {
			String roomName = mNavigationDrawerFragment.getAdapter().getItem(position);
			ChangeRoom(roomName);
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.chat, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void SendMessage(String message) {
		receiver.getInterpreter().Message(currentRoom, message);
	}

	@Override
	public void SetReady(boolean ready) {
	}

	@Override
	public boolean GetReady() {
		return false;
	}

	@Override
	public void OnDisconnect() {
		Intent i = new Intent();
		i.setClass(this, LoginActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public void OnConnect() {
	}

	@Override
	public void OnMessage(String fromID, String room, String message) {
		if (users.containsKey(fromID)) {
			AddMessage(room, new Message(users.get(fromID), message));
		}
	}

	@Override
	public void OnLoggedIn(String id) {
	}

	@Override
	public void OnRoomCreated(String name) {
		OnJoinedRoom(name);
	}

	@Override
	public void OnJoinedRoom(String name) {
		mNavigationDrawerFragment.getAdapter().AddRoom(name);
	}

	@Override
	public void OnLeftRoom(String name) {
		mNavigationDrawerFragment.getAdapter().RemoveRoom(name);
	}

	@Override
	public void OnMyInfo(UserInfo userInfo) {
	}

	@Override
	public void OnUserInfo(UserInfo userInfo) {
		users.put(userInfo.ID, userInfo);
	}

	@Override
	public void OnGotRoomNames(String[] roomNames) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnRoomInfo(RoomInfo roomInfo) {
		rooms.put(roomInfo.name, roomInfo);
		for (String userID : roomInfo.users) {
			if (!users.containsKey(userID)) {
				receiver.getInterpreter().GetUserInfo(userID);
			}
		}
	}

	@Override
	public void OnUserEnteredRoom(UserInfo userInfo, String room) {
		AddMessage(room, userInfo.username + " entered the room");
		users.put(userInfo.ID, userInfo);
	}

	@Override
	public void OnUserLeftRoom(UserInfo userInfo, String room) {
		AddMessage(room, userInfo.username + " left the room");
	}

	@Override
	public void OnRoomNotFound(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnCantMessageRoom(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnInvalidCommand(String command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnLoginRequired(String command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnLoginFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnAlreadyJoined(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnNotJoined(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnUserNotFound(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnUnknownResponse(String response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnException(Exception e) {
		Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
	}

	public void Disconnect() {
		receiver.getInterpreter().Disconnect();
	}

}
