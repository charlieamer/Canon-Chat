package com.charlieamer.cannonchat.client;

public abstract class ChatMessageInterpreter {
	
	ChatClientController chatClientController;
	
	public ChatClientController getChatClientController() {
		return chatClientController;
	}
	public void setChatClientController(ChatClientController chatClientController) {
		this.chatClientController = chatClientController;
	}
	abstract public void OnException(Exception e);
    abstract public void InterpretMessage(String message);
    abstract public void OnConnect();
    abstract public void OnDisconnect();
    
    public void Disconnect() {
    	SendChatData("disconnect");
    }
    
    public void Login(String username, String password) {
    	SendChatData("login", username, password);
    }
    
    public void Message(String room, String message) {
    	SendChatData("message", room, message);
    }
    
    public void JoinRoom(String name) {
    	SendChatData("join_room", name);
    }
    
    public void CreateRoom(String name) {
    	SendChatData("create_room", name);
    }
    
    public void GetRooms() {
    	SendChatData("get_rooms");
    }
    
    public void GetMyInfo() {
    	SendChatData("get_my_info");
    }
    
    public void GetUserInfo(String userID) {
    	SendChatData("get_user_info", userID);
    }
    
    public void GetRoomInfo(String name) {
    	SendChatData("get_room_info", name);
    }
    
    public void LeaveRoom(String name) {
    	SendChatData("leave_room", name);
    }
    
    protected void SendDataLibrary(String command) {
    	chatClientController.SendDataLibrary(command);
	}

    protected void SendChatData(String command) {
    	SendDataLibrary(command);
    }
    
	protected void SendChatData(String command, String data) {
    	SendDataLibrary(command + " " + data);
    }
    
    protected void SendChatData(String command, String firstData, String secondData) {
    	SendDataLibrary(command + " " + firstData + " " + secondData);
    }

}
