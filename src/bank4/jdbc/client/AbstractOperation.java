package bank4.jdbc.client;

public abstract class AbstractOperation {
	private int serviceTime;

	/**
	 * 
	 */
	public AbstractOperation() {
	}

	public abstract void execute(Account account);
	
	public AbstractOperation(int serviceTime) {
		this.serviceTime = serviceTime;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
	}

	public abstract boolean isUrgent();
	
	public abstract String print(Account account);
}
