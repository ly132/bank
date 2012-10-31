package client;

public class userInfo {

	String persional_id;
	String name;
	userType ut;
	
	public userInfo(String persional_id, String name, userType ut)
	{
		this.persional_id = persional_id;
		this.name = name;
		this.ut = ut;
	}
	
	public userInfo(String persional_id, String name, String utS)
	{
		this.persional_id = persional_id;
		this.name = name;
		if( utS.equals(userType.NORMAL.toString()) )
			this.ut = userType.NORMAL;
		else if( utS.equals(userType.VIP.toString()) )
			this.ut = userType.VIP;
		else if( utS.equals(userType.ENTERPRISE.toString()) )
			this.ut = userType.ENTERPRISE;
		else
			this.ut = null;
	}
	
	public String getPersionalId(){ return this.persional_id; }
	public String getName(){ return this.name; }
	public userType getUserType(){ return this.ut; }
}
