package client;

import java.io.Console;

import employee.Employee;

public class Client {

	public static void main(String args[])
	{
		System.out.println("Welcome");
		while( true )
		{
			Console cons=System.console();
			System.out.print("job_number:");
			String job_number = cons.readLine();
			job_number.trim();
			if( job_number.equals("") )
				continue;
			System.out.print("passwd:");
			char[] passwd = cons.readPassword();
			msg_processor.send("l^"+job_number+"^"+passwd.toString());
			String login_rs[]  = msg_processor.get().split("^"); //l^[10][^type^job_number^name]
			if( login_rs[1].equals("0") )
				System.out.println("login failed");
			try {
				Employee em = (Employee) Class.forName("employee."+login_rs[2]).newInstance();
				em.start(login_rs[3], login_rs[4]);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
