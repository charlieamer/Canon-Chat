package com.charlieamer.cannonchat.client;

public interface ChatListener {
	//General
	public void SetReady(boolean ready);
	public boolean GetReady();
	public void OnDisconnect();
	public void OnConnect();
	
	//Normal responses
	public void OnMessage(String fromID, String room, String message);
	public void OnLoggedIn(String id);
	public void OnRoomCreated(String name);
	public void OnJoinedRoom(String name);
	public void OnLeftRoom(String name);
	public void OnMyInfo(UserInfo userInfo);
	public void OnUserInfo(UserInfo userInfo);
	public void OnGotRoomNames(String [] roomNames);
	public void OnRoomInfo(RoomInfo roomInfo);
	public void OnUserEnteredRoom(UserInfo userInfo, String room);
	public void OnUserLeftRoom(UserInfo userInfo, String room);
	
	//Errors
	public void OnRoomNotFound(String name);
	public void OnCantMessageRoom(String name);
	public void OnInvalidCommand(String command);
	public void OnLoginRequired(String command);
	public void OnLoginFailed();
	public void OnAlreadyJoined(String name);
	public void OnNotJoined(String name);
	public void OnUserNotFound(String id);
	public void OnUnknownResponse(String response);
	
	public void OnException(Exception e);
}
