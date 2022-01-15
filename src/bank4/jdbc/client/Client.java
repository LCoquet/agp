package bank4.jdbc.client;

public class Client extends AbstractClient {
	
	public Client() {
		
	}

	public Client(int arrivalTime, AbstractOperation operation, int patienceTime) {
		super(arrivalTime, operation, patienceTime);		
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
