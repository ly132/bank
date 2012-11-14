package employee;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import client.CommonMethods;
import client.accountType;
import client.msg_processor;
import client.userInfo;
import client.userType;

public abstract class Employee {

	String Job_Number;
	Employee Superior;
	LinkedList<Employee> Subordinate = null;
	
	String Personal_ID;
	String passwd;
	String Name;
	String Age;
	String Tel_Number;
	String Address;
	
	BufferedReader in = null;
	
	LinkedHashMap<String,String> operations = new LinkedHashMap<String,String>();

	String msg_token = "^";
	String msg_split_token = "\\^";
	
	public Employee(){}
	
	public Employee(String job_number, String pid, String name, String age, String phone, String address)
	{
		this.Job_Number = job_number;
		this.Personal_ID = pid;
		this.Name = name;
		this.Age = age;
		this.Tel_Number = phone;
		this.Address = address;
	}
	
	public String getJobNumber(){ return this.Job_Number; 	}
	public String getPid()		{ return this.Personal_ID;	}
	public String getName()		{ return this.Name;		  	}
	public String getAge()		{ return this.Age;			}
	public String getTel()		{ return this.Tel_Number;	}
	public String getAddress()	{ return this.Address;		}
	public LinkedList<Employee> getSub(String passwd){ 
		if( !this.isPasswdCorrect(passwd) )
			return null;
		if( this.Subordinate == null )
			this.getSubordinate(new String[]{});
		return this.Subordinate;	
	}
	
	public Employee getSub(String passwd, String jobNum)
	{
		if( !this.isPasswdCorrect(passwd) )
			return null;
		getSub(passwd);
		for( Employee e : this.Subordinate )
		{
			if( e.getJobNumber().equals(jobNum) )
				return e;
		}
		return null;
	}
	
	public String getSubordinate(String[] s) {
		this.getSubordinateToServer();
		String rs = "Subordinate:\n";
		for( Employee e : this.Subordinate )
		{
			rs = rs + "\t" + e.getJobNumber() + "\t" + e.getName() + "\n";
		}
		return rs;
	}
	
	protected void basic_start(String jobno, String name)
	{
		this.Job_Number = jobno;
		this.Name = name;
		in = new BufferedReader(new InputStreamReader(System.in));
		String keys[] = new String[]{
				"logout","open","deposit","withdrawal","inquire",
				"transfer","changepasswd","cancel","addcustomer","addattorney","chem"
		};
		String values[] = new String[]{
				"Logout from the system.",
				"Open an account.",
				"Deposit to an existing account.",
				"Withdrawal from an existing account.",
				"Inquire an account for balance or operations.",
				"Transfer from one account to another.",
				"Change passwd.",
				"Cancel an account.",
				"Add a new customer to the bank system.",
				"Add a new attorney to an exsisting emterprise account.",
				"Change specific information of an employee."
		};
		for( int i = 0 ; i < keys.length; i++ )
		{
			operations.put(keys[i], values[i]);
		}
	}
	
	private void print(String s) {
		if( !operations.containsKey(s) )
		{
			return;
		}
		String tmp = "usage:";
		String spid = "\tpsn_id\t\tpersional id, six figures\n";
		String saccount_type = "\taccount_type\t's' for saving accout, 't' for time account\n";
		String ssum = "\tsum\t\tpositive number\n";
		String spasswd = "\tpasswd\t\tsix figures\n";
		String said = "\taccount_id\taccount id, six figures\n";
		String sname = "\tname\t\tpersional name\n";
		if( s.equals("open") )
		{
			tmp = tmp + "\topen persional_id account_type sum passwd\n";
			tmp = tmp + spid + saccount_type + ssum + spasswd;
		}
		if( s.equals("deposit") )
		{
			tmp = tmp + "\tdeposit account_id passwd sum\n";
			tmp = tmp + said + spasswd + ssum;
		}
		if( s.equals("withdrawal") )
		{
			tmp = tmp + "\twithdrawal account_id passwd sum\n";
			tmp = tmp + said + spasswd + ssum;
		}
		if( s.equals("inquire") )
		{
			tmp = tmp + "\tinquire persional_id account_id passwd [start] [end]\n";
			tmp = tmp + spid + said + spasswd;
			tmp = tmp + "\tstart\t\tstart time, YYYYMMDD, optional\n";
			tmp = tmp + "\tend\t\tend time, YYYYMMDD, optional, must later than start time\n";
		}
		if( s.equals("transfer") )
		{
			tmp = tmp + "\ttransfer persional_id account_id passwd name sum target_account_id target_name\n";
			tmp = tmp + spid + sname + said + spasswd + ssum + said + sname;
		}
		if( s.equals("changepasswd") )
		{
			tmp = tmp + "\tchpasswd persional_id account_id old_passwd new_passwd new_passwd_confirm\n";
			tmp = tmp + spid + said + spasswd;
		}
		if( s.equals("cancel") )
		{
			tmp = tmp + "\tcancel persional_id account_id passwd\n";
			tmp = tmp + spid + said + spasswd;
		}
		if( s.equals("addcustomer") )
		{
			tmp = tmp + "\taddcustomer persional_id name user_type\n";
			tmp = tmp + spid + sname;
			tmp = tmp + "\tuser_type\t'n' for normal user, 'v' for VIP, 'e' for enterprise user\n";
		}
		if( s.equals("addem") )
		{
			tmp = tmp + "\taddem persional_id passwd name age phone address\n";
			tmp = tmp + spid + spasswd + sname;
			tmp = tmp + "\t.....\t.....\n";
		}
		if( s.equals("delem") )
		{
			tmp = tmp + "\tdelem persional_id\n";
		}
		if( s.equals("addattorney"))
		{
			tmp = tmp + "\taddattorney persional_id account_id passwd att_pid att_passwd\n";
			tmp = tmp + spid + said + spasswd;
			tmp = tmp + "\tatt_pid\t\tpersional_id of attorney\n";
			tmp = tmp + "\tatt_passwd\tpasswd of attorney\n";
		}
		if( s.equals("chem") )
		{
			tmp = tmp + "\tchem passwd info_type new_info new_info_con\n";
			tmp = tmp + spasswd;
			tmp = tmp + "\tinfo_type\tshould be one of the following\n\t\tpsw age phone addr\n";
			tmp = tmp + "\tnew_info\tnew value for the specific info_type\n";
			tmp = tmp + "\tnew_info_con\tshould be the same as new_info\n";
		}
		System.out.print(tmp);
	}

	public String chem(String s[] ) throws Exception
	{
		if( !this.isPasswdCorrect(s[1]) )
			return "Failed";
		if( s[1].equals("Passwd") )
			checkPasswd(s[2]);
		else if( s[1].equals("Age") )
			checkAge(s[2]);
		else if( s[1].equals("Phone") )
			this.checkPhone(s[2]);
		else if( s[1].equals("Address") )
			checkAddress(s[2]);
		else return "info_type not correct";
		if( !s[2].equals(s[3]) )
			return "New value must be the same";
		//		send	chem^passwd^info_type^new_info
		//	rcvd	chem^success/fail
		msg_processor.send(stringBuilder("chem",s[0],s[1],s[2]));
		return msg_processor.get().split(msg_split_token)[1];
	}
	
	
	public String addem(String s[]) throws Exception{
		checkPid(s[0]);
		checkPasswd(s[1]);
		checkName(s[2]);
		checkAge(s[3]);
		checkPhone(s[4]);
		checkAddress(s[5]);
		//  addem^pid^passwd^name^age^phone^address
		//	addem^Success/Failed
		msg_processor.send(stringBuilder( "addem", s[0],s[1],s[2],s[3],s[4],s[5] ));
		return msg_processor.get().split(msg_split_token)[1];
	}	
	
	public String delem(String s[]) throws Exception{		
		if( !this.isPasswdCorrect(s[1]) )
			return "Failed";
		checkPid(s[0]);
		//	delem^pid
		//	delem^success/failed
		msg_processor.send(stringBuilder("delem",s[1]));
		return msg_processor.get().split(msg_split_token)[1];
	}

/*	//   a^id^name^n/v/e
	private String addUserToServer(String pid, String name, userType ut) {
		String user_type = ut.toString().toLowerCase().substring(0,1);
		msg_processor.send(stringBuilder(oper_type[7],pid,
				name,user_type));
		return msg_processor.get().split(msg_split_token)[1];
	}
*/
	public static void checkPid(String persional_id) throws Exception {
		Exception e = new Exception("Persional_ID not correct, must be six figures");
		if( persional_id == null || persional_id.length() != 6 )
			throw e;
	}


	public static void checkPasswd(String psw) throws Exception {
		Exception e = new Exception("Passwd not correct, must be six figures");
		if( psw == null ||psw.length() != 6 )
			throw e;
	}	

	public static void checkName(String name) throws Exception {
		Exception e = new Exception("Name not correct");
		if( name == null || name.length() == 0 )
			throw e;
	}	

	private void checkAddress(String address) throws Exception {
		Exception e = new Exception("Address not correct");
		if( address == null || address.length() == 0 )
			throw e;
	}

	private void checkPhone(String phone) throws Exception {
		Exception e = new Exception("Phone not correct, must be eleven figures");
		if( phone == null || phone.length() != 11 )
			throw e;
	}

	private void checkAge(String age) throws Exception{
		Exception e = new Exception("Age not correct, must between 18 and 60");
		if( age == null || Integer.parseInt(age) < 18 || Integer.parseInt(age) > 60 )
			throw e;
	}
	
	//	send getSubordinate^
	//	rcvd getSubordinate^job_n:name^job_n:name^...
	protected String getSubordinateToServer(){
		msg_processor.send(stringBuilder("getSubordinate","a"));
		String rss[] = msg_processor.get().split(msg_split_token);
		this.Subordinate = new LinkedList<Employee>();
		for( int i =1 ; i < rss.length; i++ )
		{
			String[] infos = rss[i].split(":");
			Employee em = new Foreground(infos[0],infos[1],infos[2],infos[3],infos[4],infos[5]);
			this.Subordinate.add(em);
		}
		return "success";
	}
	
	private String stringBuilder(String s1, String s2)
	{
		return this.Job_Number + msg_token + s1 + msg_token + s2;
	}
	private String stringBuilder(String s1, String s2, String s3)
	{
		return stringBuilder(s1,s2) + msg_token + s3 ;
	}
	private String stringBuilder(String s1, String s2, String s3, String s4)
	{
		return stringBuilder(s1,s2,s3) + msg_token + s4;
	}
	private String stringBuilder(String s1, String s2, String s3, String s4, String s5)
	{
		return stringBuilder(s1,s2,s3,s4)+msg_token +s5;
	}
	private String stringBuilder(String s1, String s2, String s3, String s4, String s5, String s6)
	{
		return stringBuilder(s1,s2,s3,s4,s5)+msg_token +s6;
	}
	private String stringBuilder(String s1, String s2, String s3, String s4, String s5, String s6, String s7)
	{
		return stringBuilder(s1,s2,s3,s4,s5,s6) + msg_token +s7;
	}
	
	public boolean isPasswdCorrect(String passwd)
	{
		return this.passwd.equals(passwd);
	}
	
	protected abstract void service();
	public abstract void start(String s1, String s2);
}
