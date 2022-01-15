package bank4.jdbc.client;

public class Transfer extends AbstractOperation{
	private Account targetAccount;
	private float amount;
	private boolean done;	
	public Transfer() {
		
	}

	public Transfer(int serviceTime, Account targetAccount, float amount) {
		super(serviceTime);
		this.targetAccount = targetAccount;
		this.amount = amount;
		this.done = false;
	}
	
	@Override
	public void execute(Account account) {
		if(amount < account.getBalance()) {
			account.setBalance(account.getBalance() - amount);
			targetAccount.setBalance(targetAccount.getBalance() + amount);
			setDone(true);
		}
		print(account);
	}

	@Override
	public boolean isUrgent() {
		return true;
	}
	
	public Account getTargetAccount() {
		return targetAccount;
	}

	public void setTargetAccount(Account targetAccount) {
		this.targetAccount = targetAccount;
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
	public String toString() {
		return "Operation : Transfer";
	}

	@Override
	public String print(Account account) {
		String res = "Transfer\n";
		res += "From account number " + account.getNumber() + " to account number " + targetAccount.getNumber() + "\n";
		res += isDone() ? "Amount transfered : " : "Could not transfer : " + amount + "\n";
		return res;
	}

}
