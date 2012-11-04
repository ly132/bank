package client;

import java.io.IOException;
import java.net.DatagramPacket;

public class msg_processor {
	
	static byte[] rcvData = new byte[1024];
	static DatagramPacket rcvPacket;
	
	static public void send(String msg)
	{
		byte[] data = msg.getBytes();
		try {
			Client.clientSocket.send(new DatagramPacket(data,data.length,Client.ip,Client.port));
			rcvData = new byte[1024];
			rcvPacket = new DatagramPacket(rcvData, rcvData.length);
			Client.clientSocket.receive(rcvPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public String get()
	{
		return new String(rcvPacket.getData()).trim();
	}
}
