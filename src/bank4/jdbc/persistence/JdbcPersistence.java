package bank4.jdbc.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import bank4.jdbc.client.AbstractClient;
import bank4.jdbc.simulation.SimulationEntry;
import bank4.jdbc.simulation.StatisticManager;

public class JdbcPersistence implements StatisticPersistence {

	private static String host = "localhost";
	private static String base = "bank";
	private static String user = "root";
	private static String password = "";
	private static String url = "jdbc:mysql://" + host + "/" + base;

	/**
	 * Lazy singleton instance.
	 */
	private Connection connection;

	public JdbcPersistence() {
		prepareConnection();
	}

	private void prepareConnection() {
		if (connection == null) {
			try {
				connection = DriverManager.getConnection(url, user, password);
			} catch (Exception e) {
				System.err.println("Connection failed : " + e.getMessage());
			}
		}
	}

	@Override
	public int persist(SimulationEntry simulationEntry, StatisticManager statisticManager) {
		int idEntry = persistEntry(simulationEntry);
		if (idEntry != 0) {
			persistClients(statisticManager, idEntry);
		}
		return idEntry;

	}

	private void persistClients(StatisticManager statisticManager, int idEntry) {
		List<AbstractClient> servedClients = statisticManager.getServedClients();

		for (AbstractClient client : servedClients) {
			addClient(idEntry, client, true);
		}

		List<AbstractClient> nonServedClients = statisticManager.getNonServedClients();

		for (AbstractClient client : nonServedClients) {
			addClient(idEntry, client, false);
		}

	}

	private void addClient(int idEntry, AbstractClient client, boolean isServed) {
		try {

			String insertAddressQuery = "INSERT INTO client (arrival_time, service_start_time, departure_time, "
					+ "is_served, priority, entry_id) VALUES (?,?,?,?,?,?)";

			PreparedStatement preparedStatement = connection.prepareStatement(insertAddressQuery);

			preparedStatement.setInt(1, client.getArrivalTime());
			preparedStatement.setInt(2, client.getServiceStartTime());
			preparedStatement.setInt(3, client.getDepartureTime());
			preparedStatement.setBoolean(4, isServed);
			preparedStatement.setBoolean(5, client.isPriority());
			preparedStatement.setInt(6, idEntry);

			preparedStatement.executeUpdate();

			preparedStatement.close();
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}
	}

	private int persistEntry(SimulationEntry simulationEntry) {
		int idEntry = 0;
		try {

			String insertAddressQuery = "INSERT INTO entry (simulation_duration, cashier_count, "
					+ "min_service_time, max_service_time, client_arrival_interval, priority_client_rate, client_patience_time"
					+ ") VALUES (?,?,?,?,?,?,?)";

			PreparedStatement preparedStatement = connection.prepareStatement(insertAddressQuery);

			// Set values of parameters in the query.
			preparedStatement.setInt(1, simulationEntry.getSimulationDuration());
			preparedStatement.setInt(2, simulationEntry.getCashierCount());
			preparedStatement.setInt(3, simulationEntry.getMinServiceTime());
			preparedStatement.setInt(4, simulationEntry.getMaxServiceTime());
			preparedStatement.setInt(5, simulationEntry.getClientArrivalInterval());
			preparedStatement.setDouble(6, simulationEntry.getPriorityClientRate());
			preparedStatement.setInt(7, simulationEntry.getClientPatienceTime());

			preparedStatement.executeUpdate();

			ResultSet keys = preparedStatement.getGeneratedKeys();
			keys.next();
			idEntry = keys.getInt(1);

			preparedStatement.close();
		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}
		return idEntry;
	}

	@Override
	public int servedClientCount(int simulationEntryId) {
		int count = 0;
		try {

			String selectAddressQuery = "SELECT count(*) AS co FROM client AS c WHERE c.entry_id = ? AND c.is_served = true";

			PreparedStatement preparedStatement = connection.prepareStatement(selectAddressQuery);

			preparedStatement.setInt(1, simulationEntryId);

			ResultSet result = preparedStatement.executeQuery();

			result.next();
			count = result.getInt("co");

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}
		return count;
	}

	public int nonServedClientCount(int simulationEntryId) {
		int count = 0;
		try {

			String selectAddressQuery = "SELECT count(*) AS co FROM client AS c WHERE c.entry_id = ? AND c.is_served = false";

			PreparedStatement preparedStatement = connection.prepareStatement(selectAddressQuery);

			preparedStatement.setInt(1, simulationEntryId);

			ResultSet result = preparedStatement.executeQuery();

			result.next();
			count = result.getInt("co");

			preparedStatement.close();

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		}
		return count;
	}

}
