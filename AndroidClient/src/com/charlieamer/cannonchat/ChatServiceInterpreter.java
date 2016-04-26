package com.charlieamer.cannonchat;

import com.charlieamer.cannonchat.client.ChatMessageInterpreter;

public class ChatServiceInterpreter extends ChatMessageInterpreter {

	ChatService service;
	public ChatServiceInterpreter(ChatService service) {
		this.service = service;
	}
	
	public void SendDataToServer(String data) {
		SendDataLibrary(data);
	}

	@Override
	public void OnException(Exception e) {
		service.OnException(e);
	}

	@Override
	public void InterpretMessage(String message) {
		service.MessageFromServer(message);
	}

	@Override
	public void OnConnect() {
		service.OnConnect();
	}

	@Override
	public void OnDisconnect() {
		service.OnDisconnect();
	}

}
