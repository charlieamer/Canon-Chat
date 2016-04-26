package com.charlieamer.cannonchat;

import android.content.Context;
import android.util.Log;

import com.charlieamer.cannonchat.client.ChatInterpreterListener;
import com.charlieamer.cannonchat.client.ChatListener;

public class ChatInterpreterForActivity extends ChatInterpreterListener {

	Context context;
	public ChatInterpreterForActivity(ChatListener listener, Context context) {
		super(listener);
		this.context = context;
	}
	
	@Override
	protected void SendDataLibrary(String command) {
		Log.d("chat", "Sending command: " + command);
		ChatBroadcastReceiver.StartService(context, command);
	}

}
