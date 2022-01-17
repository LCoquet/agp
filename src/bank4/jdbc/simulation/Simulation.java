package bank4.jdbc.simulation;

import java.util.List;

import org.jfree.ui.RefineryUtilities;

import bank4.jdbc.bank.Bank;
import bank4.jdbc.bank.Cashier;
import bank4.jdbc.bank.Queue;
import bank4.jdbc.chart.PieGraphicalResult;
import bank4.jdbc.client.AbstractClient;
import bank4.jdbc.client.AbstractOperation;
import bank4.jdbc.client.Account;
import bank4.jdbc.client.Transfer;
import bank4.jdbc.client.Withdraw;
import bank4.jdbc.persistence.HibernatePersistence;
import bank4.jdbc.persistence.JdbcPersistence;
import bank4.jdbc.persistence.StatisticPersistence;

/**
 * System simulation. Access point of all related information.
 */
public class Simulation {
	private Bank bank;
	private StatisticManager statisticManager;
	private SimulationEntry simulationEntry;
	private int consultationAmount = 4;
	private int withdrawAmount = 2;
	private int transferAmount = 9;

	public Simulation() {

	}

	public void buildBank() {
		int cashierCount = simulationEntry.getCashierCount();
		bank = new Bank(cashierCount);
	}

	public void simulate() {
		int simulationDuration = simulationEntry.getSimulationDuration();
		int clientArrivalInterval = simulationEntry.getClientArrivalInterval();
		for (int currentSystemTime = 0; currentSystemTime <= simulationDuration; currentSystemTime++) {

			statisticManager.simulationDurationRecord();
			SimulationUtility.printBankStat(currentSystemTime, bank);

			updateBank(currentSystemTime);

			boolean newClientArrival = newClientArrival(clientArrivalInterval, currentSystemTime);
			if (newClientArrival) {
				int serviceTime = generateRandomServiceTime();
				double priorityClientRate = simulationEntry.getPriorityClientRate();
				AbstractClient client = SimulationUtility.getRandomClient(priorityClientRate);
				client.setArrivalTime(currentSystemTime);
				int clientPatienceTime = simulationEntry.getClientPatienceTime();
				client.setPatienceTime(clientPatienceTime);
				AbstractOperation operation = client.getOperation();
				operation.setServiceTime(serviceTime);
				
				//TODO modify the random account generation to a real account 
				Account account = SimulationUtility.getRandomAccount();
				client.setAccount(account);
				if(operation.toString().equals("Operation : Transfer")) {
					((Transfer) operation).setAmount((float) Math.random()*10);
					Account targetAccount = SimulationUtility.getRandomAccount();
					((Transfer) operation).setTargetAccount(targetAccount);
					incrementTransferAmount();
				} else if (operation.toString().equals("Operation : Withdraw")) {
					((Withdraw) operation).setAmount((float) Math.random()*10);
					incrementWithdrawAmount();
				} else {
					incrementConsultationAmount();
				}
				//end of TODO

				Cashier freeCashier = bank.getFreeCashier();
				if (freeCashier == null) {
					SimulationUtility.printClientArrival(currentSystemTime, false);
					Queue queue = bank.getQueue();
					queue.addQueueLast(client);
				} else {
					SimulationUtility.printClientArrival(currentSystemTime, true);
					serveClient(currentSystemTime, freeCashier, client);
				}
			}
		}
	}

	private void updateBank(int currentSystemTime) {
		List<Cashier> cashiers = bank.getCashiers();
		Queue queue = bank.getQueue();
		for (Cashier cashier : cashiers) {

			if (!cashier.isFree()) {
				statisticManager.cashierOccupationRecord();
			}

			cashier.work();

			if (cashier.serviceFinished()) {
				// Leaving client
				AbstractClient leavingClient = cashier.getServingClient();
				leavingClient.setDepartureTime(currentSystemTime);
				SimulationUtility.printClientDeparture(currentSystemTime);
				statisticManager.registerServedClient(leavingClient);
				System.out.println(leavingClient.executeOperation());
				HibernatePersistence.updateAccountByNumber(leavingClient.getAccount().getNumber(), leavingClient.getAccount().getBalance());
				
				if(leavingClient.getOperation().toString().equals("Operation : Transfer")) {
					Transfer operationTarget = (Transfer) leavingClient.getOperation();
					HibernatePersistence.updateAccountByNumber(operationTarget.getTargetAccount().getNumber(), operationTarget.getTargetAccount().getBalance());
				}

				cashier.setServingClient(null);

				// Serve a client in the queue
				if (!queue.isEmpty()) {
					AbstractClient nextClient;

					nextClient = queue.findPriorityClient();
					if (nextClient == null) {
						nextClient = queue.getQueueFirst();
					} else {
						queue.removePriorityClient(nextClient);
					}
					serveClient(currentSystemTime, cashier, nextClient);
				}
			}
		}

		// Leaving impatient clients
		queue.updateClientPatience();
		List<AbstractClient> impatientClients = queue.removeImpatientClients();
		for (AbstractClient client : impatientClients) {
			client.setDepartureTime(currentSystemTime);
			statisticManager.registerNonServedClient(client);
			SimulationUtility.printClientDepartureWithoutBeingServed(currentSystemTime);
		}
	}

	private void serveClient(int currentSystemTime, Cashier cashier, AbstractClient client) {
		client.setServiceStartTime(currentSystemTime);
		AbstractOperation operation = client.getOperation();
		int serviceTime = operation.getServiceTime();
		cashier.serve(client);
		SimulationUtility.printServiceTimeTrace(currentSystemTime, serviceTime);
	}

	private int generateRandomServiceTime() {
		int minServiceTime = simulationEntry.getMinServiceTime();
		int maxServiceTime = simulationEntry.getMaxServiceTime();
		int randomServiceTime = SimulationUtility.getRandomServiceTime(minServiceTime, maxServiceTime);
		return randomServiceTime;
	}

	private boolean newClientArrival(int clientArrivalInterval, int simIter) {
		return simIter % clientArrivalInterval == 0;
	}

	public String simulationResults() {
		StringBuffer results = new StringBuffer();
		results.append("########## Simulation results : #####################\n");
		results.append("Simulation Duration : " + simulationEntry.getSimulationDuration() + "\n");
		results.append("Served client count : " + statisticManager.servedClientCount() + "\n");
		results.append("Average client waiting time : " + statisticManager.calculateAverageClientWaitingTime() + " \n");
		results.append("Average client service time : " + statisticManager.calculateAverageClientServiceTime() + " \n");
		results.append("Cashier occupation rate : "
				+ statisticManager.calculateAverageCashierOccupationRate(simulationEntry.getCashierCount()) + " % \n");
		results.append("Non-Served client count : " + statisticManager.nonServedClientCount() + "\n");
		results.append("Client satisfaction rate : " + statisticManager.calculateClientSatisfactionRate() + " %");
		return results.toString();
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public StatisticManager getStatisticManager() {
		return statisticManager;
	}

	public void setStatisticManager(StatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	public SimulationEntry getSimulationEntry() {
		return simulationEntry;
	}

	public void setSimulationEntry(SimulationEntry simulationEntry) {
		this.simulationEntry = simulationEntry;
	}
	

	public void graphicalResults() {
		int idEntry = persistSimulationResult();
		showGraphicalSimulationResult(idEntry);
	}
	
	private void showGraphicalSimulationResult(int idEntry) {
		PieGraphicalResult pieGraphicalResult = new PieGraphicalResult(idEntry);
		pieGraphicalResult.pack();
		RefineryUtilities.centerFrameOnScreen(pieGraphicalResult);
		pieGraphicalResult.setVisible(true);
	}

	private int persistSimulationResult() {
		StatisticPersistence persistenceProxy = new JdbcPersistence();
		return persistenceProxy.persist(simulationEntry, statisticManager);
	}

	public int getConsultationAmount() {
		return consultationAmount;
	}

	public void setConsultationAmount(int consultationAmount) {
		this.consultationAmount = consultationAmount;
	}

	public int getWithdrawAmount() {
		return withdrawAmount;
	}

	public void setWithdrawAmount(int withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}

	public int getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(int transferAmount) {
		this.transferAmount = transferAmount;
	}

	public void incrementConsultationAmount() {
		consultationAmount ++;
	}
	
	public void incrementWithdrawAmount() {
		withdrawAmount ++;
	}
	
	public void incrementTransferAmount() {
		transferAmount ++;
	}

}