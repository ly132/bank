package employee;

public class BankingDirector extends BankingManager{

	public BankingDirector(){
		super();
	};
	public BankingDirector(String job_number, String pid, String name, String age, String phone, String address) {
		super( job_number,  pid,  name,  age,  phone,  address);
	}
}
