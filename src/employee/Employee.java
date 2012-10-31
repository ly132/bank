package employee;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import client.accountType;
import client.msg_processor;
import client.userInfo;
import client.userType;

public abstract class Employee {

	String Job_Number;
	Employee Superior;
	LinkedList<Employee> Subordinate;
	
	String Personal_ID;
	String Name;
	String Age;
	String Tel_Number;
	String Address;
	
	BufferedReader in = null;
	
	LinkedHashMap<String,String> operations = new LinkedHashMap<String,String>();
	
	String tips[] = new String[]{
			"Please enter the following information.", //0
			"Persional ID:", //1
			"Name:", //2
			"Account ID:", //3
			"Passwd:", //4
			"Balance:", //5
	};
		
	String er_msg[] = new String[]{
			"Illegal input.", //0
			"Illegal persional ID.", //1
			"Illegal name.", //2
			"Illegal passwd.", //3
			"Not enough balance.", //4
	};
	
	String oper_type[] = new String[]{
			"1", //open account		0
			"2", //deposit			1	
			"3", //withdrawal		2
			"4", //inquire			3
			"5", //transfer			4
			"6", //change passwd	5
			"7", //cancel account	6
			"a", //add user			7
			"b", //get user info	8
	};
	
	String msg_token = "^";
	
	protected void basic_start(String jobno, String name)
	{
		this.Job_Number = jobno;
		this.Name = name;
		in = new BufferedReader(new InputStreamReader(System.in));
		String keys[] = new String[]{
				"logout","open","deposit","withdrawal","inquire",
				"transfer","changepasswd","cancel","addcustomer"
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
				"Add a new customer to the bank system."
		};
		for( int i = 0 ; i < keys.length; i++ )
		{
			operations.put(keys[i], values[i]);
		}
	}
	
	private String open(String s[]) throws Exception
	{
		if( s.length < 4 )
			print("open");
		checkPid(s[0]);
		checkSum(s[2]);
		checkPasswd(s[3]);
		return createToServer(s[0],checkAtype(s[1]),s[2],s[3]);
	}
	
	private void print(String s) {
		if( !operations.containsKey(s) )
		{
			return;
		}
		String tmp = "usage:";
		String spid = "\t\tpersional_id\tpersional id, six figures\n";
		String saccount_type = "\t\taccount_type\t's' for saving accout, 't' for time account\n";
		String ssum = "\t\tsum\tpositive number\n";
		String spasswd = "\t\tpasswd\tsix figures\n";
		String said = "\t\taccount_id\taccount id, six figures\n";
		String sname = "\t\tname\tpersional name\n";
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
			tmp = tmp + "\t\tstart\tstart time, YYYYMMDD, optional\n";
			tmp = tmp + "\t\tend\tend time, YYYYMMDD, optional, must later than start time\n";
		}
		if( s.equals("transfer") )
		{
			tmp = tmp + "\ttransfer persional_id name account_id passwd target_account_id target_name\n";
			tmp = tmp + spid + sname + said + spasswd + said + sname;
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
			tmp = tmp + "\t\tuser_type\t'n' for normal user, 'v' for VIP, 'e' for enterprise user\n";
		}
		
	}

	private accountType checkAtype(String s) throws Exception {
		Exception e = new Exception("Illegal account type.");
		if( s.equals("s") )
			return accountType.SAVINGS_ACCOUNT;
		else if( s.equals("t") )
			return accountType.TIME_ACCOUNT;
		else
			throw e;
	}

	private String deposit(String s[]) throws Exception
	{
		if( s.length < 3 )
			print("deposit");
		checkAid(s[0]);
		checkPasswd(s[1]);
		checkSum(s[2]);
		return depositToServer(s[0],s[1],s[2]);
	}
	
	private String withdrawal(String s[]) throws Exception
	{
		if( s.length < 3 )
			print("withdrawal");
		checkAid(s[0]);
		checkPasswd(s[1]);
		checkSum(s[2]);
		return withdrawalToServer(s[0],s[1],s[2]);
	}
	
	private String inquire(String s[]) throws Exception
	{
		if( s.length < 3 )
			print("inquire");
		checkPid(s[0]);
		checkAid(s[1]);
		checkPasswd(s[2]);
		String start,end;
		if( s.length == 3 )
			start = end = "*";
		if( s.length == 4 )
		{
			checkTimeRange(s[3],null);
			start = s[3];
		}
		if( s.length > 4 )
		{
			checkTimeRange(s[3],s[4]);
			start = s[3];
			end = s[4];
		}
		return inquireToServer(s[0],s[1],s[2],s[3],s[4]);
	}
	
	private String transfer(String s[]) throws Exception
	{
		if( s.length < 6 )
			print("transfer");
		checkPid(s[0]);
		checkName(s[1]);
		checkAid(s[2]);
		checkPasswd(s[3]);
		checkAid(s[4]);
		checkName(s[5]);
		return transferToServer( s[0],s[1],s[2],s[3],s[4],s[5] );
	}
	
	//	pid,aid,oldpasswd,newpasswd
	private String changepasswd(String s[]) throws Exception
	{
		if( s.length < 5 )
			print("chpasswd");
		checkPid(s[0]);
		checkAid(s[1]);
		checkPasswd(s[2]);
		checkPasswd(s[3]);
		checkPasswd(s[4]);
		if( !s[3].equals(s[4]) )
			throw new Exception("new passwd not confirm");
		return changePasswdToServer(s[0],s[1],s[2],s[3]);
	}
	
	private String cancel(String s[]) throws Exception
	{
		if( s.length < 3 )
			print("cancel");
		checkPid(s[0]);
		checkAid(s[1]);
		checkPasswd(s[2]);
		return cancelAccountToServer(s[0],s[1],s[2]);
	}
	
	private String addcustomer(String s[]) throws Exception{
		if( s.length < 3 )
			print("addcustomer");
		checkPid(s[0]);
		checkName(s[1]);
		return addUserToServer(s[0],s[1],checkUserType(s[2]));
	}
	
	private userType checkUserType(String s) throws Exception{
		if( s.equals("n") )
			return userType.NORMAL;
		else if( s.equals("v") )
			return userType.VIP;
		else if( s.equals("e") )
			return userType.ENTERPRISE;
		else
			throw new Exception("Illegal user type.");
	}

	//   a^id^name^userType
	private String addUserToServer(String pid, String name, userType ut) {
		msg_processor.send(stringBuilder(oper_type[7],pid,
				name,ut.toString()));
		return msg_processor.get().split(msg_token)[1];
	}

	//		b^id	b^id^name^userType
	private userInfo getUserInfoFromServer(String persional_id) {
		msg_processor.send(stringBuilder(oper_type[8],persional_id));
		String rs[] = msg_processor.get().split(msg_token);
		return new userInfo(rs[1],rs[2],rs[3]);
	}

	private boolean checkName(String name2) {

		if( name2.length() > 20 || name2.length() == 0 )
			return false;
		return true;
	}

	private void checkPid(String persional_id) throws Exception {
		Exception e = new Exception("Persional ID not correct");
		if( persional_id.length() != 18 )
			throw e;
		try{
			Integer.parseInt(persional_id.substring(0,persional_id.length()-1));
		}catch(NumberFormatException nfe){
			throw e;
		}
		String last = persional_id.substring(persional_id.length()-1);
		try{
			Integer.parseInt(last);
		}catch(NumberFormatException nfe){
			if( !last.equals("X") )
				throw e;
		}
	}

	private String createToServer( String p_id, accountType at, String init_balance, String init_passwd )
	{
		msg_processor.send(stringBuilder(oper_type[1],p_id,at.toString(),init_passwd,init_balance));
		String rs = msg_processor.get();
		return rs.split(msg_token)[1];
	}
	
	private void checkPasswd(String init_passwd) throws Exception {
		Exception es = new Exception("Illegal passwd");
		if( init_passwd.length() != 6 )
			throw es;
		try{
			Integer.parseInt(init_passwd);
		}catch( Exception e ){
			throw es;}
	}

	private void checkSum(String init_balance) throws Exception {
		Exception e = new Exception("Illegal balance");
		double tmp = -1;
		try{
			tmp = Double.parseDouble(init_balance);
		}catch(Exception ef){
			throw e;
		}
		if( tmp < 0 )
			throw e;
	}


	
	// send 4^p_id^a_id^passwd^start^end
	// rcv  4^balance^xxxxxxxx^xxxxxxx^...
	private String inquireToServer(String persional_id, String account_id,
			String passwd, String start, String end) {
		msg_processor.send(stringBuilder(oper_type[3],persional_id,account_id,passwd,start,end));
		String rs[] = msg_processor.get().split(msg_token);
		StringBuilder sb = new StringBuilder("Balance:");
		sb.append(rs[1]).append("\n");
		for( int i = 2; i < rs.length; i++ )
		{
			sb.append(rs[i]).append("\n");
		}
		return sb.toString();
	}
	

	private void checkTimeRange(String start, String end) throws Exception{
		Exception es = new Exception("Illegal time.");
		String dateFormatString = "yyyyMMdd";
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		dateFormat.setLenient(false);
		if( end != null ){
		try{
			if( dateFormat.parse(start).after(dateFormat.parse(end)) )
				throw es;
		}catch( Exception e ){
			throw es;
		}}
		else{
			try{
				dateFormat.parse(start);
			}catch( Exception e ){
				throw es;
			}
		}
	}

	private void checkAid(String account_id) throws Exception {
		checkPasswd(account_id);
	}
	
	//	send	2^a_id^passwd^balance
	//	rcv		2^balance or 2^failed
	private String depositToServer(String account_id, String passwd,
			String balance) {
		msg_processor.send(stringBuilder(oper_type[1],account_id,passwd,balance));
		return msg_processor.get().split(msg_token)[1];
	}


	
	//	send	3^a_id^passwd^balance
	//	rcvd	3^balance or 3^failed
	private String withdrawalToServer(String account_id, String passwd,
			String balance) {
		msg_processor.send(stringBuilder(oper_type[3],account_id,passwd,balance));
		return msg_processor.get().split(msg_token)[1];
	}


	
	//	send	4^xx^..
	//	rcvd	4^balance or 4^failed
	private String transferToServer(String p_id, String a_id, String passwd,
			String name2, String target_a_id, String target_name) {
		msg_processor.send(stringBuilder(oper_type[4],stringBuilder(p_id,a_id,passwd,name2,target_a_id,target_name)));
		return msg_processor.get().split(msg_token)[1];
	}


	
	//	rcvd	5^succeeded or 5^failed
	private String changePasswdToServer(String persional_id, String account_id,
			String passwd, String newPasswd) {
		msg_processor.send(stringBuilder(oper_type[5],persional_id,account_id,passwd,newPasswd));
		return msg_processor.get().split(msg_token)[1];
	}


	
	private String cancelAccountToServer(String persional_id,
			String account_id, String passwd) {
		msg_processor.send(stringBuilder(oper_type[6],persional_id,account_id,passwd));
		return msg_processor.get().split(msg_token)[1];
	}

	private String stringBuilder(String s1, String s2)
	{
		return s1 + msg_token + s2;
	}
	private String stringBuilder(String s1, String s2, String s3)
	{
		return stringBuilder( stringBuilder(s1,s2),s3 );
	}
	private String stringBuilder(String s1, String s2, String s3, String s4)
	{
		return stringBuilder( stringBuilder(s1,s2,s3),s4 );
	}
	private String stringBuilder(String s1, String s2, String s3, String s4, String s5)
	{
		return stringBuilder( stringBuilder(s1,s2,s3,s4),s5 );
	}
	private String stringBuilder(String s1, String s2, String s3, String s4, String s5, String s6)
	{
		return stringBuilder( stringBuilder(s1,s2,s3,s4,s5), s6);
	}

	protected abstract void service();
	public abstract void start(String s1, String s2);
}
