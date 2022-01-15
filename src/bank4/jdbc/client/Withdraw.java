package bank4.jdbc.client;

public class Withdraw extends AbstractOperation {
	private float amount;
	private boolean done;

	public Withdraw() {

	}

	public Withdraw(int serviceTime, float amount) {
		super(serviceTime);
		this.amount = amount;
		this.done = false;
	}
	
	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public boolean isUrgent() {
		return true;
	}

	@Override
	public String toString() {
		return "Operation : Withdraw";
	}

	@Override
	public String print(Account account) {
		String res = "Withdraw \n";
		res += isDone() ? "Withdrawed " : "Could not withdraw "
				+ amount + " from account " + account.getNumber() + "\n";
		return res;
	}

}
