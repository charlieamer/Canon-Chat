package com.charlieamer.cannonchat;

import com.charlieamer.cannonchat.client.ChatListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class ChatBroadcastReceiver extends BroadcastReceiver {
	
	ChatListener listener;
	ChatInterpreterForActivity interpreter;
	ChatBroadcastReceiver(ChatListener listener, Context context) {
		this.listener = listener;
		interpreter = new ChatInterpreterForActivity(listener, context);
	}
	
	public ChatInterpreterForActivity getInterpreter() {
		return interpreter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String name = intent.getStringExtra(ChatService.KEY_NAME);
		String value = null;
		Log.d("chat", "Received message from service.");
		Log.d("chat", "  Name: " + name);
		try {
			value = intent.getStringExtra(ChatService.KEY_VALUE);
			Log.d("chat", "  Value: " + value);
		} catch (Exception e) {
			Log.w("chat", "  Couldn't extract value");
		}
		if (name.equals(ChatService.VALUE_MESSAGE)) {
			interpreter.InterpretMessage(value);
		} else if (name.equals(ChatService.VALUE_CONNECTION)) {
			if (value.equals(ChatService.VALUE_CONNECT)) {
				interpreter.OnConnect();
			} else {
				interpreter.OnDisconnect();
			}
		} else if (name.equals(ChatService.VALUE_EXCEPTION)) {
			Exception e = new Exception(value);
			interpreter.OnException(e);
		}
	}
	
	public static void StartService(Context context) {
		StartService(context, null);
	}
	
	public static void StartService(Context context, String message) {
		Intent intent = new Intent(context, ChatService.class);
		if (message != null) {
			intent.setAction(message);
		}
		context.startService(intent);
		
	}
	
	public static ChatBroadcastReceiver GenerateReceiver(ChatListener listener, Context context) {
		ChatBroadcastReceiver receiver = new ChatBroadcastReceiver(listener, context);
		context.registerReceiver(receiver, new IntentFilter(ChatService.CHAT_BROADCAST));
		StartService(context);
		return receiver;
	}
	
	public static void StopService(Context context) {
		Intent intent = new Intent(context, ChatService.class);
		context.stopService(intent);
	}

}
