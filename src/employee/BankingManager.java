package employee;

public class BankingManager extends Foreground{
	public BankingManager(){};
	public BankingManager(String string, String string2) {
		this.Job_Number = string;
		this.Name = string2;
	}
	
	//
	public void start(String s1, String s2) {
		this.basic_start(s1, s2);
		this.operations.put("addem", "Add new subordinate employee.");
		this.operations.put("delem", "Delete subordinate employee.");
		this.operations.put("getSubordinate", "Get subordinate employee information.");
		this.service();
	}
}
