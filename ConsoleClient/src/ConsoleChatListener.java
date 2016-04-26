import java.util.HashMap;

import com.charlieamer.cannonchat.client.ChatListener;
import com.charlieamer.cannonchat.client.RoomInfo;
import com.charlieamer.cannonchat.client.UserInfo;


public class ConsoleChatListener implements ChatListener {
	
	HashMap<String, UserInfo> users;
	RoomInfo globalRoomInfo;
	String myId;
	boolean isReady;
	
	ConsoleChatListener() {
		users = new HashMap<String, UserInfo>();
	}
	
	public boolean IsLoggedIn() {
		return myId != null;
	}

	@Override
	public void OnMessage(String fromID, String room, String message) {
		System.out.println("[" + room + "] " + users.get(fromID).username + ": " + message);
	}

	@Override
	public void OnLoggedIn(String id) {
		myId = id;
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
		users.put(myId, userInfo);
	}

	@Override
	public void OnUserInfo(UserInfo userInfo) {
		users.put(userInfo.ID, userInfo);
	}

	@Override
	public void OnGotRoomNames(String[] roomNames) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnRoomInfo(RoomInfo roomInfo) {
		globalRoomInfo = roomInfo;
	}

	@Override
	public void OnRoomNotFound(String name) {
		System.err.println("Room not found: " + name);
	}

	@Override
	public void OnCantMessageRoom(String name) {
		System.err.println("Cant message room: " + name);
	}

	@Override
	public void OnInvalidCommand(String command) {
		System.err.println("Invalid command: " + command);
	}

	@Override
	public void OnLoginRequired(String command) {
		System.err.println("Login is required for command: " + command);
	}

	@Override
	public void OnLoginFailed() {
		System.err.println("Login failed");
	}

	@Override
	public void OnAlreadyJoined(String name) {
		System.err.println("Already joined: " + name);
	}

	@Override
	public void OnNotJoined(String name) {
		System.err.println("Not joined: " + name);
	}

	@Override
	public void OnUserNotFound(String id) {
		System.err.println("User not found: " + id);
	}

	@Override
	public void OnUnknownResponse(String response) {
		System.err.println("Unparsable response: " + response);
	}

	@Override
	public void OnException(Exception e) {
		e.printStackTrace();
	}

	@Override
	public void OnUserEnteredRoom(UserInfo userInfo, String room) {
		if (room == "global")
			users.put(userInfo.ID, userInfo);
		System.out.println("[" + room + "] " + userInfo.username + " joined room");
	}

	@Override
	public void OnUserLeftRoom(UserInfo userInfo, String room) {
		if (room == "global")
			users.remove(userInfo.ID);
		System.out.println("[" + room + "] " + userInfo.username + " left room");
	}

	@Override
	public void SetReady(boolean ready) {
		isReady = ready;
	}

	@Override
	public boolean GetReady() {
		return isReady;
	}

	@Override
	public void OnDisconnect() {
		System.err.println("Disconnected");
	}

	@Override
	public void OnConnect() {
		// TODO Auto-generated method stub
		
	}

}
