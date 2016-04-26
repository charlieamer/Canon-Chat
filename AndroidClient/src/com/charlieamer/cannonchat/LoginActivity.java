package com.charlieamer.cannonchat;

import com.charlieamer.cannonchat.client.ChatListener;
import com.charlieamer.cannonchat.client.RoomInfo;
import com.charlieamer.cannonchat.client.UserInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements ChatListener {
	
	ChatBroadcastReceiver receiver;
	TextView lblStatus;
	ProgressBar prgConnecting;
	EditText txtUsername;
	EditText txtPassword;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		receiver = ChatBroadcastReceiver.GenerateReceiver(this, this);
		
		lblStatus = (TextView)findViewById(R.id.lblStatus);
		prgConnecting = (ProgressBar)findViewById(R.id.prgConnecting);
		txtUsername = (EditText)findViewById(R.id.txtUsername);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
	}
	
	public void Login(View v) {
		receiver.getInterpreter().Login(txtUsername.getText().toString(), txtPassword.getText().toString());
	}
	
	public void Reconnect(View v) {
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {}
		ChatBroadcastReceiver.StopService(this);
		receiver = ChatBroadcastReceiver.GenerateReceiver(this, this);
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
		if (receiver != null) {
			unregisterReceiver(receiver);
			ChatBroadcastReceiver.StopService(this);
			receiver = ChatBroadcastReceiver.GenerateReceiver(this, this);
		}
		lblStatus.setVisibility(View.VISIBLE);
		prgConnecting.setVisibility(View.VISIBLE);
		lblStatus.setTextColor(Color.RED);
	}
	
	@Override
	public void OnConnect() {
		lblStatus.setVisibility(View.INVISIBLE);
		prgConnecting.setVisibility(View.INVISIBLE);
		//receiver.getInterpreter().Login("test", "test");
	}

	@Override
	public void OnMessage(String fromID, String room, String message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		try {
			unregisterReceiver(receiver);
		} catch(Exception e) {}
	}

	@Override
	public void OnLoggedIn(String id) {
		Intent i = new Intent(this, ChatActivity.class);
		Bundle b = new Bundle();
		b.putString(ChatService.KEY_VALUE, id);
		i.putExtras(b);
		startActivity(i);
		finish();
	}
	
	@Override
	public void OnLoginFailed() {
		Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnRoomCreated(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnJoinedRoom(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnLeftRoom(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMyInfo(UserInfo userInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUserInfo(UserInfo userInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGotRoomNames(String[] roomNames) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnRoomInfo(RoomInfo roomInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUserEnteredRoom(UserInfo userInfo, String room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUserLeftRoom(UserInfo userInfo, String room) {
		// TODO Auto-generated method stub
		
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
	}
}
