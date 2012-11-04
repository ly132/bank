package client;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import employee.Employee;

public class Client {

	static DatagramSocket clientSocket = null;
	static InetAddress ip = null;
	static int port = 8243;
	
	public static void main(String args[])
	{
		try {
		clientSocket = new DatagramSocket();
		ip = InetAddress.getByAddress(new byte[]{(byte) 127,(byte) 18,(byte) 216,(byte)170});	
		System.out.println("Welcome");
		BufferedReader console_in = new BufferedReader(new InputStreamReader(System.in));
		while( true )
		{
//			Console cons=System.console();
			
			System.out.print("job_number:");
			String job_number = "";
	///		job_number = console_in.readLine();
			job_number.trim();
			if( job_number.equals("quit") )
			{
				System.out.println("goodbye...");
				return;
			}
			job_number = "400001";
			if( job_number.equals("") )
				continue;
			System.out.print("passwd:");
	///		String passwd = console_in.readLine();
	//		String passwd = "root";
			
			//----
	//		job_number = "400001";
			String passwd = "root";
			//-------
			
			msg_processor.send("0^login^"+job_number+"^"+passwd.toString());
			String login_rs[]  = msg_processor.get().split("\\^"); //l^[10][^job_number^name]
			
			
			if( login_rs[1].equals("0") )
				System.out.println("login failed");
			else
			{
				job_number = login_rs[2];
				String name = login_rs[3];
				String job_type = "";
				switch( job_number.charAt(0) )
				{
				case '1':
					job_type = "Foreground";
					break;
				case '2':
					job_type = "BankingManager";
					break;
				case '3':
					job_type = "BankingDirector";
					break;
				case '4':
					job_type = "SystemManager";
				}
				Employee em = (Employee) Class.forName("employee."+job_type).newInstance();
				em.start(job_number,name);
			}
		}
		} catch (Exception e) {e.printStackTrace();}
	}
}

