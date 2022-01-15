package bank4.jdbc.client;

public class Client extends AbstractClient {
	
	public Client() {
		
	}

	public Client(int arrivalTime, AbstractOperation operation, int patienceTime, Account account) {
		super(arrivalTime, operation, patienceTime, account);		
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
