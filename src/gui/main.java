package gui;

import java.awt.Font;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import employee.Foreground;

public class main {

//	MainFrame mainFrame = null;
//	LoginFrame loginFrame = null;
	public static DatagramSocket clientSocket = null;
	public static InetAddress ip = null;
	public static int port = 8243;
	public static void main(String[] args){
		try {
			clientSocket = new DatagramSocket();
			ip = InetAddress.getByAddress(new byte[]{(byte) 172,(byte) 18,(byte) 58,(byte)83});	
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
			
		new LoginFrame();
	}
}
