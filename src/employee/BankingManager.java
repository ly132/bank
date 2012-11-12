package employee;

public class BankingManager extends Foreground{
	public BankingManager(){
		super();
		this.operations.put("addem", "Add new subordinate employee.");
		this.operations.put("delem", "Delete subordinate employee.");
		this.operations.put("getSubordinate", "Get subordinate employee information.");
	};
	public BankingManager(String job_number, String pid, String name, String age, String phone, String address) {
		super( job_number,  pid,  name,  age,  phone,  address);
	}
	
	//
	public void start(String s1, String s2) {
		this.basic_start(s1, s2);
		
		this.service();
	}
}
