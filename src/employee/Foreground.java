package employee;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Set;

public class Foreground extends Employee{
	
	public void start(String s1, String s2) {
		this.basic_start(s1, s2);
		this.service();
	}

	protected void service() {
		String cmds = null;
		while( true )
		{
			System.out.print("\n" + this.Name + " > ");			
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
			String argss[] = null;
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
					System.out.println("logout...");
					return;
				}
				try {
					Method m = this.getClass().getMethod(cmd,Class.forName("String[]"));
					String rs = (String) m.invoke(this, (Object[])argss);
					System.out.println(rs);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else
			{
				Set<Entry<String,String>> se = this.operations.entrySet();
				for( Entry<String,String> ess : se )
				{
					System.out.println("\t" + ess.getKey() + "\t\t" + ess.getValue());
				}
			}
		}	
	}
}
