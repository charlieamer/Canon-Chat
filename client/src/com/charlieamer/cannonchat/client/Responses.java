package com.charlieamer.cannonchat.client;

public enum Responses {
	ERROR_ROOM_NOT_FOUND("room_not_found"),
	ERROR_CANT_MESSAGE_ROOM("cannot_message_room"),
	ERROR_INVALID_COMMAND("invalid_command"),
	ERROR_LOGIN_REQUIRED("login_required"),
	ERROR_LOGIN_FAILED("login_failed"),
	ERROR_ALREADY_JOINED("already_joined"),
	ERROR_NOT_JOINED("not_joined"),
	ERROR_USER_NOT_FOUND("user_not_found"),
	
	RESPONSE_LOGIN_SUCCESSFUL("login_successful"),
	RESPONSE_ROOM_CREATED_SUCCESSFULY("room_created_successfuly"),
	RESPONSE_JOINED_ROOM_SUCCESSFULY("joined_room_successfuly"),
	RESPONSE_LEFT_ROOM_SUCCESSFULY("left_room_successfuly"),
	RESPONSE_MY_INFO("my_info"),
	RESPONSE_USER_INFO("user_info"),
	RESPONSE_GET_ROOMS("get_rooms"),
	RESPONSE_ROOM_INFO("room_info"),
	RESPONSE_MESSAGE("message"),
	RESPONSE_USER_ENTERED_ROOM("user_enter_room"),
	RESPONSE_USER_LEFT_ROOM("user_left_room")
	;
	public final String serverString;
	Responses(String str) {
		serverString = str;
	}
	public boolean isEqual (String name) {
		return serverString.equals(name);
	}
}
