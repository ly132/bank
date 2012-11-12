package client;

public class Account {

	String id,passwd,name,type;
	double balance;
	
	private Account(String id,String passwd)
	{
		this.id = id;
		this.passwd = passwd;
	}
	public static Account getAccountInstance(String id, String passwd){
		if( account_login(id,passwd) )
			return new Account(id,passwd);
		else return null;
	}
	private static boolean account_login(String id2, String passwd2) {
		// TODO Auto-generated method stub
		return false;
	}
	public String getId() {
		return this.id;
	}
	public String deposit(String sum) {
		return null;
	}
}
