package com.charlieamer.cannonchat.client;
import java.util.HashMap;

public class ChatResponse {
	public String code;
	public String type;
	public String error_code;
	public HashMap<String, String> info;
	public String[] room_names;
	public RoomInfo room_info;
	public UserInfo user_info;
}
