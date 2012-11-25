package client;

import java.text.SimpleDateFormat;

public class Account {

	String id,passwd,name,type,persional_id,Em_JobNum;
	double balance;
	
	static String msg_token = "^";
	static String msg_split_token = "\\^";
	
	private Account(String id,String passwd, String jobNum)
	{
		this.id = id;
		this.passwd = passwd;
		this.Em_JobNum = jobNum;
		this.persional_id = null;
	}
	
	public static Account getAccountInstance(String id, String passwd, String jobNum) throws Exception{
		if( account_login(id,passwd,jobNum) )
			return new Account(id,passwd,jobNum);
		else return null;
	}
	
	// jobnum^acclogin^aid^pwd
	private static boolean account_login(String id2, String passwd2, String jobNum) throws Exception{
		checkAid(id2);
		checkPasswd(passwd2);
		String to = "^";
		msg_processor.send(
				new StringBuilder()
				.append(jobNum).append(to)
				.append("acclogin").append(to)
				.append(id2).append(to)
				.append(passwd2).toString()
				);
		if( msg_processor.get().split("\\^")[1].equals("1") )
			return true;
		else return false;
	}
	
	public String getId() {
		return this.id;
	}
	
	
	public String deposit(String sum) throws Exception{
		checkSum(sum);
		msg_processor.send(stringBuilder("deposit",this.id,this.passwd,sum));
		return msg_processor.get().split(msg_split_token)[1];
	}
	
	
	// stringBuilder
	private String stringBuilder(String s1, String s2)
	{
		return this.Em_JobNum + msg_token + s1 + msg_token + s2;
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
	// stringBuilder
	
	
	public String withdrawal(String sum) throws Exception{
		checkSum(sum);
		msg_processor.send(stringBuilder("withdrawal",this.id,this.passwd,sum));
		return msg_processor.get().split(msg_split_token)[1];
	}
	
	// yyyy-mm-dd to yyyymmdd
	public Object[][] inquire(String start, String end) throws Exception{
		start = start.replaceAll("-", "");
		end = end.replaceAll("-", "");
		if( !checkTimeRange(start,end) )
			end = start;
		msg_processor.send(stringBuilder("inquire","",this.id,this.passwd,start,end));
		String rs[] = msg_processor.get().split(msg_split_token);
		if( rs[1].equals("Inquire Fail") )
			return null;
//		int no = 0;
		double incomeSum = 0;
		double outcomeSum = 0;
		Object[][] inquireResult = new Object[rs.length+1][5];
		inquireResult[0][0] = 0;
		inquireResult[0][4] = rs[1];
		
		for( int i = 0 ; i < rs.length-1; i++ )
		{
			String[] tmp = rs[i+1].split("\\t");
			
			if( !tmp[5].equals("") )
				incomeSum += Double.parseDouble(tmp[5]);
			if( !tmp[6].equals("") )
				outcomeSum += Double.parseDouble(tmp[6]);
			inquireResult[i+1][0] = i+1;
			inquireResult[i+1][1] = tmp[0];
			inquireResult[i+1][2] = tmp[5];
			inquireResult[i+1][3] = tmp[6];
			inquireResult[i+1][4] = tmp[7];
		}
		inquireResult[rs.length][0] = "Total";
		inquireResult[rs.length][2] = incomeSum == 0 ? "" : incomeSum;
		inquireResult[rs.length][3] = outcomeSum == 0 ? "" : outcomeSum;
		return inquireResult;
	}

	// jobno^transfer^fromid^sum^toid^toname
	public String transfer(String sum, String target_id, String target_name) throws Exception{
		//sum, target account id ,target name
		checkSum(sum);
		checkAid(target_id);
		checkName(target_name);
		msg_processor.send(stringBuilder("transfer",this.id,sum,target_id,target_name));
		return msg_processor.get().split(msg_split_token)[1];
	}
	
	// jobno^advinfo^aid^pid
	public String advinfo(String pid) throws Exception{
		checkPid(pid);
		msg_processor.send(stringBuilder("advinfo",this.id,pid));
		String advinfo_result = msg_processor.get().split(msg_split_token)[1];
		if( advinfo_result.equals("1") )
		{
			this.persional_id = pid;
			advinfo_result = "Advance Login Success";
		}
		else
			advinfo_result = "Advance Login Fail";
		return advinfo_result;
	}
	
	// jobnum^open^pid^type^passwd^balance
	//pid,type,balance,passwd,psw
	public static String open(String jobNum, String pid, String type, String balance,
			String passwd, String passwd2) throws Exception{
		checkPid(pid);
		checkSum(balance);
		checkPasswd(passwd);
		if( !passwd.equals(passwd2) )
			throw new Exception("Passwd must be the same");
		msg_processor.send( new StringBuilder()
				.append(jobNum).append(msg_token)
				.append("open").append(msg_token)
				.append(pid).append(msg_token)
				.append(type).append(msg_token)
				.append(passwd).append(msg_token)
				.append(balance).append(msg_token)
				.toString() );
		return msg_processor.get().split(msg_split_token)[1];
	}
	public boolean isAdvanceInfoConfirm() {
		if( this.persional_id != null )
			return true;
		else
			return false;
	}
	
	public String chpasswd(String passwd1, String passwd2) throws Exception{
		checkPasswd(passwd1);
		if( !passwd1.equals(passwd2) )
			throw new Exception("Passwd must be the same");
		msg_processor.send(stringBuilder("changepasswd",this.persional_id,this.id,passwd1));
		return msg_processor.get().split(msg_split_token)[1];
	}
	
	public String cancel() {
		msg_processor.send(stringBuilder("cancel",this.id));
		return msg_processor.get().split(msg_split_token)[1];
	}
	
	public String addattorney(String att_id, String att_passwd) throws Exception{
		checkPid(att_id);
		checkPasswd(att_passwd);
		msg_processor.send(stringBuilder("addattorney",this.persional_id,this.id,att_id,att_passwd));
		return msg_processor.get().split(msg_split_token)[1];
	}
	


	
	public static void checkPid(String persional_id) throws Exception {
		Exception e = new Exception("Persional_ID not correct, must be six figures");
		if( persional_id == null || persional_id.length() != 6 )
			throw e;
	}
	
	public static void checkAid(String aid) throws Exception {
		Exception e = new Exception("Account_ID not correct, must be six figures");
		if( aid == null || aid.length() != 6 )
			throw e;
	}
	
	public static void checkPasswd(String psw) throws Exception {
		Exception e = new Exception("Passwd not correct, must be six figures");
		if( psw == null ||psw.length() != 6 )
			throw e;
	}
	
	public static void checkSum(String init_balance) throws Exception {
		Exception e = new Exception("Sum or balance must not be empty");
		if( init_balance == null || init_balance.equals("") )
			throw e;
	}
	
	public static void checkName(String name) throws Exception {
		Exception e = new Exception("Name not correct");
		if( name == null || name.equals("") )
			throw e;
	}
	
	public static boolean checkTimeRange(String start, String end) {
		String dateFormatString = "yyyyMMdd";
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		dateFormat.setLenient(false);
		try{
			if( dateFormat.parse(start).after(dateFormat.parse(end)) )
				return false;
		}catch( Exception e ){
			e.printStackTrace();
		}
		return true;
	}
}
