package employee;

import java.util.Set;

public class SystemManager extends BankingDirector{

	public SystemManager(){};
	public SystemManager(String string, String string2) {
		this.Job_Number = string;
		this.Name = string2;
	}
	
	public void start(String s1, String s2) {
		this.basic_start(s1, s2);
		Set<String> keys = this.operations.keySet();
		for( String key : keys )
		{
			if(!( 	keys.equals("login") ||  
					keys.equals("addem") ||  
					keys.equals("delem") ))
				this.operations.remove(key);
		}
		this.service();
	}
}
