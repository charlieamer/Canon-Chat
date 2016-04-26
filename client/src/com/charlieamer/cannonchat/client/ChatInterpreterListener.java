package com.charlieamer.cannonchat.client;

import com.google.gson.Gson;

public class ChatInterpreterListener extends ChatMessageInterpreter {
	ChatListener chatListener;
	Gson gson;
	boolean ready;
	boolean loggedIn;
	
	public boolean IsReady() {
		return ready;
	}
	
	public boolean IsLoggedIn() {
		return loggedIn;
	}
	
	public ChatInterpreterListener(ChatListener listener) {
		chatListener = listener;
        gson = new Gson();
	}
	@Override
	public void OnException(Exception e) {
		chatListener.OnException(e);
	}

	@Override
    public void InterpretMessage(String message) {
    	// System.err.println("Recieved message: " + message);
    	
    	ChatResponse cr = null;

		try {
			cr = gson.fromJson(message, ChatResponse.class);
		} catch (Exception e) {
			System.err.println("Error parsing JSON: " + message);
			System.err.println(e.getMessage());
			OnException(e);
			return;
		}
    	String infoMessage = null;
    	if (cr.info != null && cr.info.containsKey("message"))
    		infoMessage = cr.info.get("message");
    	//
    	// Normal responses
    	//
    	if (Responses.RESPONSE_GET_ROOMS.isEqual(cr.type)) {
    		chatListener.OnGotRoomNames(cr.room_names);
    	} else if (Responses.RESPONSE_JOINED_ROOM_SUCCESSFULY.isEqual(cr.type)) {
    		chatListener.OnJoinedRoom(infoMessage);
    	} else if (Responses.RESPONSE_LEFT_ROOM_SUCCESSFULY.isEqual(cr.type)) {
    		chatListener.OnLeftRoom(infoMessage);
    	} else if (Responses.RESPONSE_LOGIN_SUCCESSFUL.isEqual(cr.type)) {
    		loggedIn = true;
    		chatListener.OnLoggedIn(infoMessage);
    	} else if (Responses.RESPONSE_MESSAGE.isEqual(cr.type)) {
    		chatListener.OnMessage(cr.info.get("from"),
    							   cr.info.get("room"),
    							   cr.info.get("message"));
    	} else if (Responses.RESPONSE_MY_INFO.isEqual(cr.type)) {
    		chatListener.OnMyInfo(cr.user_info);
    	} else if (Responses.RESPONSE_ROOM_CREATED_SUCCESSFULY.isEqual(cr.type)) {
    		chatListener.OnRoomCreated(infoMessage);
    	} else if (Responses.RESPONSE_ROOM_INFO.isEqual(cr.type)) {
    		chatListener.OnRoomInfo(cr.room_info);
    	} else if (Responses.RESPONSE_USER_INFO.isEqual(cr.type)) {
    		chatListener.OnUserInfo(cr.user_info);
    	} else if (Responses.RESPONSE_USER_ENTERED_ROOM.isEqual(cr.type)) {
    		chatListener.OnUserEnteredRoom(cr.user_info, infoMessage);
    	} else if (Responses.RESPONSE_USER_LEFT_ROOM.isEqual(cr.type)) {
    		chatListener.OnUserLeftRoom(cr.user_info, infoMessage);
    	}
    	//
    	// ERRORS
    	//
    	else if (Responses.ERROR_ALREADY_JOINED.isEqual(cr.error_code)) {
    		chatListener.OnAlreadyJoined(infoMessage);
    	} else if (Responses.ERROR_CANT_MESSAGE_ROOM.isEqual(cr.error_code)) {
    		chatListener.OnCantMessageRoom(infoMessage);
    	} else if (Responses.ERROR_INVALID_COMMAND.isEqual(cr.error_code)) {
    		chatListener.OnInvalidCommand(infoMessage);
    	} else if (Responses.ERROR_LOGIN_FAILED.isEqual(cr.error_code)) {
    		chatListener.OnLoginFailed();
    	} else if (Responses.ERROR_LOGIN_REQUIRED.isEqual(cr.error_code)) {
    		chatListener.OnLoginRequired(infoMessage);
    	} else if (Responses.ERROR_NOT_JOINED.isEqual(cr.error_code)) {
    		chatListener.OnNotJoined(infoMessage);
    	} else if (Responses.ERROR_ROOM_NOT_FOUND.isEqual(cr.error_code)) {
    		chatListener.OnRoomNotFound(infoMessage);
    	} else if (Responses.ERROR_USER_NOT_FOUND.isEqual(cr.error_code)) {
    		chatListener.OnUserNotFound(infoMessage);
    	} else {
    		chatListener.OnUnknownResponse(message);
    	}
    	ready = true;
    	chatListener.SetReady(true);
    }
	@Override
	public void OnConnect() {
		chatListener.OnConnect();
	}
	@Override
	public void OnDisconnect() {
		chatListener.OnDisconnect();
	}

}
