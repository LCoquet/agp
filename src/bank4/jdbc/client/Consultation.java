package bank4.jdbc.client;

public class Consultation extends AbstractOperation {
	
	public Consultation() {
		
	}

	public Consultation(int serviceTime) {
		super(serviceTime);
	}

	@Override
	public void execute(Account account) {
		print(account);
	}

	@Override
	public boolean isUrgent() {
		return false;
	}
	
	@Override
	public String toString() {
		return "Operation : Consultation";
	}

	@Override
	public String print(Account account) {
		String res = "Consultation\n";
		res += "Account number : " + account.getNumber() + "\n";
		res += "Account balance : " + account.getBalance() + "\n";		
		return res;
	}
}
