package com.charlieamer.cannonchat.client;

import java.net.InetAddress;
import java.net.SocketException;

import Extasys.DataFrame;
import Extasys.Network.TCP.Client.ExtasysTCPClient;
import Extasys.Network.TCP.Client.Connectors.TCPConnector;


public class ChatClientController extends ExtasysTCPClient {
	ChatMessageInterpreter interpreter;
	TCPConnector connector;
	
	public ChatClientController(String host, int port, ChatMessageInterpreter interpret) {
		super("client", "description", 4, 8);
		InetAddress addr;
        interpreter = interpret;
		try {
			addr = InetAddress.getByName(host);
			super.AddConnector("chatServer", addr, port, 10248);
		} catch (Exception e) {
			interpreter.OnException(e);
		}
	}

    @Override
    public void OnDataReceive(TCPConnector connector, DataFrame data)
    {
    	String message = new String(data.getBytes());
    	interpreter.InterpretMessage(message);
    }
    

    @Override
    public void OnConnect(TCPConnector connector)
    {
    	interpreter.OnConnect();
    }

    @Override
    public void OnDisconnect(TCPConnector connector)
    {
    	interpreter.OnDisconnect();
        this.Stop();
    }
    
    public void SendDataLibrary(String data) {
    	try {
    		super.SendData(data.concat("\n"));
    	} catch (Exception e) {
    		interpreter.OnException(e);
    	}
    	
    }
    
}
