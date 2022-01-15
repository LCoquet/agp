package bank4.jdbc.client;

public class VIPClient extends AbstractClient {
	
	public VIPClient() {
		
	}

	public VIPClient(int arrivalTime, AbstractOperation operation, int patienceTime, Account account) {
		super(arrivalTime, operation, patienceTime, account);
	}

	@Override
	public boolean isPriority() {
		return true;
	}

	@Override
	public String toString() {
		return "Priority " + super.toString();
	}

}
