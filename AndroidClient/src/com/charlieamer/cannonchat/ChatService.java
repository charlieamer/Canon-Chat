package com.charlieamer.cannonchat;

import java.io.Serializable;

import com.charlieamer.cannonchat.client.*;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

// 7 - 1, 10
// 8 - 1, 2, 3, 5, 18, 19, 20, 21, 28
// 9 - 2, 9, 10, 16, 17, 18, 24, 29, 30, 37
// 10 - 1, 2, 6, 7, 12
// 11 - 1, 2, 3, 5, 6, 17

public class ChatService extends Service implements Runnable {
	
	public static final String CHAT_BROADCAST = "com.charlieamer.cannonchat.BROADCAST";
	public static final String VALUE_MESSAGE = "MESSAGE";
	public static final String VALUE_CONNECTION = "CONNECTION";
	public static final String VALUE_CONNECT = "CONNECTED";
	public static final String VALUE_DISCONNECT = "DISCONNECTED";
	public static final String VALUE_EXCEPTION = "EXCEPTION";
	public static final String KEY_NAME = "EVENT";
	public static final String KEY_VALUE = "VALUE";
	
	
	ChatClientController chatController;
	ChatServiceInterpreter chatInterpreter;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (chatController == null) {
			new Thread(this).start();
		} else {
			String val = intent.getAction();
			if (val != null)
				chatInterpreter.SendDataToServer(val);
			else
				Log.w("service", "Empty value received for start command");
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		if (chatController != null)
			chatController.Stop();
		chatController = null;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Intent createIntent(String key, Serializable value) {
		Intent intent = new Intent(CHAT_BROADCAST);
		intent.putExtra(KEY_NAME, key);
		intent.putExtra(KEY_VALUE, value);
		return intent;
	}
	
	public void MessageFromServer(String message) {
		sendBroadcast(createIntent(VALUE_MESSAGE, message));
	}
	
	public void OnConnect() {
		Log.i("service", "Connected");
		sendBroadcast(createIntent(VALUE_CONNECTION, VALUE_CONNECT));
	}
	
	public void OnDisconnect() {
		Log.i("service", "Disconnected");
		chatController = null;
		sendBroadcast(createIntent(VALUE_CONNECTION, VALUE_DISCONNECT));
	}
	
	public void OnException(Exception e) {
		Log.e("service", "Exception: " + e.getMessage());
		sendBroadcast(createIntent(VALUE_EXCEPTION, e.getMessage()));
	}
	@Override
	public void run() {
		Log.d("service", "Chat service started");
		chatInterpreter = new ChatServiceInterpreter(this);
		chatController = new ChatClientController("172.16.19.98", 8000, chatInterpreter);
		chatInterpreter.setChatClientController(chatController);
		try {
			chatController.Start();
		} catch (Exception e) {
			Log.e("service", "Error connecting to chat: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
