package com.charlieamer.cannonchat;

import java.io.Serializable;

import com.charlieamer.cannonchat.client.UserInfo;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4963911135311045949L;
	UserInfo from;
	String message;
	public Message(UserInfo user, String message) {
		this.from = user;
		this.message = message;
	}
	public UserInfo getFrom() {
		return from;
	}
	public String getMessage() {
		return message;
	}
	public String getNickname() {
		return from.username;
	}
}
