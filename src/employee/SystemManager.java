package employee;

import java.util.LinkedHashMap;
import java.util.Set;

public class SystemManager extends BankingDirector{

	public SystemManager(){
		super();
	};
	public SystemManager(String string, String string2) {
		this.Job_Number = string;
		this.Name = string2;
	}
	
	public void start(String s1, String s2, String passwd) {
		this.basic_start(s1, s2, passwd);
		Set<String> keys = this.operations.keySet();
		LinkedHashMap<String,String> tmp = (LinkedHashMap<String,String>)this.operations.clone();
		for( String key : keys )
		{
			if(!( 	key.equals("login") ||  
					key.equals("logout")||
					key.equals("addem") ||  
					key.equals("delem") ))
				tmp.remove(key);
		}
		this.operations = tmp;
//		this.service();
	}
}
