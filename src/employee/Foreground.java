package employee;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Set;

public class Foreground extends Employee{
	
	public Foreground(){};
	public Foreground(String job_number, String pid, String name, String age, String phone, String address) {
		super( job_number,  pid,  name,  age,  phone,  address);
	}

	public void start(String s1, String s2) {
		this.basic_start(s1, s2);
		this.service();
	}

	protected void service() {
		String cmds = null;
		while( true )
		{
			System.out.print(this.Name + " > ");
			try {
				cmds = this.in.readLine().trim();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if( cmds.equals("") )
				continue;			
			int index = cmds.indexOf(" ");
			String cmd;
			String argss[] = {};
			if( index == -1 )
				cmd = cmds;
			else
			{
				cmd = cmds.substring(0,index);
				argss = cmds.substring(index+1).split(" +");
			}
			if( this.operations.containsKey(cmd) )
			{
				if( cmd.equals("logout") )
				{
					System.out.println("logout...\n");
					return;
				}
				try {
					Method m = this.getClass().getMethod(cmd,String[].class);
					String rs = (String) m.invoke(this, (Object)argss);
					if( !rs.equals("") )
						System.out.println(rs);
				} catch (Exception e) {
					System.out.println(e.getCause().getMessage());
				}
				System.out.println("");
			}
			else
			{
				Set<Entry<String,String>> se = this.operations.entrySet();
				for( Entry<String,String> ess : se )
				{
					String middle1 = "\t";
					String printout = middle1 + ess.getKey() + middle1;
					if( ess.getKey().length() < 8 )
						printout += middle1;
					System.out.println(printout + ess.getValue());
				}
				System.out.println("");
			}
		}	
	}
}
