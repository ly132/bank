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
			"open", //open account		0
			"deposit", //deposit			1	
			"withdrawal", //withdrawal		2
			"inquire", //inquire			3
			"transfer", //transfer			4
			"changepasswd", //change passwd	5
			"cancel", //cancel account	6
			"addcustomer" , //add user			7
			"getuserinfo", //get user info	8
			"addem",	//		9
			"delem",	//		10
			"getSubordinate",//	11
			"addattorney",	// 12
	};
	
	String msg_token = "^";
	String msg_split_token = "\\^";
	
	public Employee(){}
	
	public Employee(String job_number, String name)
	{
		this.Job_Number = job_number;
		this.Name = name;
	}
	
	public String getJobNumber(){ return this.Job_Number; }
	public String getName()		{ return this.Name;		  }
	public String getSubordinate(String[] s) {
		this.getSubordinateToServer();
		String rs = "Subordinate:\n";
		for( Employee e : this.Subordinate )
		{
			rs = rs + "\t" + e.getJobNumber() + "\t" + e.getName() + "\n";
		}
		return rs;
	//	return this.Subordinate.toString();
	}
	
	protected void basic_start(String jobno, String name)
	{
		this.Job_Number = jobno;
		this.Name = name;
		in = new BufferedReader(new InputStreamReader(System.in));
		String keys[] = new String[]{
				"logout","open","deposit","withdrawal","inquire",
				"transfer","changepasswd","cancel","addcustomer","addattorney"
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
				"Add a new attorney to an exsisting emterprise account."
		};
		for( int i = 0 ; i < keys.length; i++ )
		{
			operations.put(keys[i], values[i]);
		}
	}
	
	public String open(String s[]) throws Exception
	{
		if( s.length < 4 ){
			print("open");return "";}
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
		System.out.print(tmp);
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

	public String addattorney(String s[]) throws Exception
	{
		if( s.length < 5 ){
			print("addattorney");return "";
		}
		checkPid(s[0]);
		checkAid(s[1]);
		checkPasswd(s[2]);
		checkPid(s[3]);
		checkPasswd(s[4]);
		return addattorneyToServer(s[0],s[1],s[2],s[3],s[4]);
	}
	
	private String addattorneyToServer(String string, String string2,
			String string3, String string4, String string5) {
		//	addattorney^pid^aid^passwd^pid^passwd
		msg_processor.send(stringBuilder(oper_type[12],string,string2,string3,string4,string5));
		return msg_processor.get().split(msg_split_token)[1];
	}

	public String deposit(String s[]) throws Exception
	{
		if( s.length < 3 ){
			print("deposit");return "";}
		checkAid(s[0]);
		checkPasswd(s[1]);
		checkSum(s[2]);
		return depositToServer(s[0],s[1],s[2]);
	}
	
	public String withdrawal(String s[]) throws Exception
	{
		if( s.length < 3 ){
			print("withdrawal");return "";}
		checkAid(s[0]);
		checkPasswd(s[1]);
		checkSum(s[2]);
		return withdrawalToServer(s[0],s[1],s[2]);
	}
	
	public String inquire(String s[]) throws Exception
	{
		String start,end;
		if( s.length == 3 )
		{
			start = "20000102";
			end = "20000101";
		}
		else if( s.length == 5 )
		{
			checkTimeRange(s[3],s[4]);
			start = s[3];
			end = s[4];
		}
		else{
			print("inquire");return "";}
		checkPid(s[0]);
		checkAid(s[1]);
		checkPasswd(s[2]);		
		return inquireToServer(s[0],s[1],s[2],start,end);
	}
	
	public String transfer(String s[]) throws Exception
	{
		if( s.length < 7 ){
			print("transfer");return "";}
		checkPid(s[0]);		
		checkAid(s[1]);
		checkPasswd(s[2]);
		checkName(s[3]);
		checkSum(s[4]);
		checkAid(s[5]);
		checkName(s[6]);
		return transferToServer( s[0],s[1],s[2],s[3],s[4],s[5],s[6] );
	}
	
	//	pid,aid,oldpasswd,newpasswd
	public String changepasswd(String s[]) throws Exception
	{
		if( s.length < 5 ){
			print("changepasswd");return "";}
		checkPid(s[0]);
		checkAid(s[1]);
		checkPasswd(s[2]);
		checkPasswd(s[3]);
		checkPasswd(s[4]);
		if( !s[3].equals(s[4]) )
			throw new Exception("new passwd not confirm");
		return changePasswdToServer(s[0],s[1],s[2],s[3]);
	}
	
	public String cancel(String s[]) throws Exception
	{
		if( s.length < 3 ){
			print("cancel");return "";}
		checkPid(s[0]);
		checkAid(s[1]);
		checkPasswd(s[2]);
		return cancelAccountToServer(s[0],s[1],s[2]);
	}
	
	public String addcustomer(String s[]) throws Exception{
		if( s.length < 3 ){
			print("addcustomer");return "";}
		checkPid(s[0]);
		checkName(s[1]);
		return addUserToServer(s[0],s[1],checkUserType(s[2]));
	}
	
	public String addem(String s[]) throws Exception{
		if( s.length < 6 ){
			print("addem");return "";}
		checkPid(s[0]);
		checkPasswd(s[1]);
		checkName(s[2]);
		checkAge(s[3]);
		checkPhone(s[4]);
		checkAddress(s[5]);
		return addEmToServer(s[0],s[1],s[2],s[3],s[4],s[5]);
	}
	
	//  addem^pid^passwd^name^age^phone^address
	//	addem^success/failed
	private String addEmToServer(String string, String string2, String string3,
			String string4, String string5, String string6) {
		msg_processor.send(stringBuilder( oper_type[9], string,string2,string3,string4,string5,string6 
				));
		return msg_processor.get().split(msg_split_token)[1];
	}

	
	public String delem(String s[]) throws Exception{
		if( s.length < 1 ){
			print("delem");return "";}
		checkPid(s[0]);
		return delEmToServer(s[0]);
	}
	
	//	delem^pid
	//	delem^success/failed
	private String delEmToServer(String string) {
		msg_processor.send(stringBuilder(oper_type[10],string));
		return msg_processor.get().split(msg_split_token)[1];
	}
	

	private void checkAddress(String string) {
		
	}

	private void checkPhone(String string) throws Exception {
		Exception e = new Exception("Illegal phone number, must be 11 figures");
		try{
		if( string.length() != 11 )
			throw e;
		Long.parseLong(string);
		}catch( Exception ef )
		{
			throw e;
		}
	}

	private void checkAge(String string) throws Exception{
		Exception e = new Exception("Illegal age");
		try{
		int age = Integer.parseInt(string);
		if( age < 16 || age > 60 )
			throw e;
		}catch( Exception ef )
		{
			throw e;
		}
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

	//   a^id^name^n/v/e
	private String addUserToServer(String pid, String name, userType ut) {
		String user_type = ut.toString().toLowerCase().substring(0,1);
		msg_processor.send(stringBuilder(oper_type[7],pid,
				name,user_type));
		return msg_processor.get().split(msg_split_token)[1];
	}

	//		b^id	b^id^name^userType
	private userInfo getUserInfoFromServer(String persional_id) {
		msg_processor.send(stringBuilder(oper_type[8],persional_id));
		String rs[] = msg_processor.get().split(msg_split_token);
		return new userInfo(rs[1],rs[2],rs[3]);
	}

	private boolean checkName(String name2) throws Exception{
		Exception e = new Exception("Illegal name, must less than 20 charactors");
		if( name2.length() > 20 || name2.length() == 0 )
			throw e;
		return true;
	}

	private void checkPid(String persional_id) throws Exception {
		Exception e = new Exception("Persional ID not correct, must be six figures");
		if( persional_id.length() != 6 )
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
		msg_processor.send(stringBuilder(oper_type[0],p_id,at.toString().substring(0,1),init_passwd,init_balance));
		String rs = msg_processor.get();
		return rs.split(msg_split_token)[1];
	}
	
	private void checkPasswd(String init_passwd) throws Exception {
		Exception es = new Exception("Illegal passwd, must be six figures");
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
		String rs[] = msg_processor.get().split(msg_split_token);
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
		try{
		checkPasswd(account_id);
		}catch( Exception e )
		{
			throw new Exception("Illegal account id, must be six figures");
		}
	}
	
	//	send	2^a_id^passwd^balance
	//	rcv		2^balance or 2^failed
	private String depositToServer(String account_id, String passwd,
			String balance) {
		msg_processor.send(stringBuilder(oper_type[1],account_id,passwd,balance));
		return msg_processor.get().split(msg_split_token)[1];
	}


	
	//	send	3^a_id^passwd^balance
	//	rcvd	3^balance or 3^failed
	private String withdrawalToServer(String account_id, String passwd,
			String balance) {
		msg_processor.send(stringBuilder(oper_type[2],account_id,passwd,balance));
		return msg_processor.get().split(msg_split_token)[1];
	}


	
	//	send	4^xx^..
	//	rcvd	4^balance or 4^failed
	private String transferToServer(String p_id, String a_id, String passwd,
			String name2, String sum, String target_a_id, String target_name) {
		msg_processor.send(stringBuilder(oper_type[4],p_id,a_id,passwd,name2,sum,target_a_id)+msg_token +target_name);
		return msg_processor.get().split(msg_split_token)[1];
	}


	
	//	rcvd	5^succeeded or 5^failed
	private String changePasswdToServer(String persional_id, String account_id,
			String passwd, String newPasswd) {
		msg_processor.send(stringBuilder(oper_type[5],persional_id,account_id,passwd,newPasswd));
		return msg_processor.get().split(msg_split_token)[1];
	}


	
	private String cancelAccountToServer(String persional_id,
			String account_id, String passwd) {
		msg_processor.send(stringBuilder(oper_type[6],persional_id,account_id,passwd));
		return msg_processor.get().split(msg_split_token)[1];
	}

	//	send getSubordinate^
	//	rcvd getSubordinate^job_n:name^job_n:name^...
	protected String getSubordinateToServer(){
		msg_processor.send(stringBuilder(oper_type[11],"a"));
		String rss[] = msg_processor.get().split(msg_split_token);
		this.Subordinate = new LinkedList<Employee>();
		for( int i =1 ; i < rss.length; i++ )
		{
			Employee em = new Foreground(rss[i].split(":")[0],rss[i].split(":")[1]);
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
	protected abstract void service();
	public abstract void start(String s1, String s2);
}
