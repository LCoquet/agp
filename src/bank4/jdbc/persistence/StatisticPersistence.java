package bank4.jdbc.persistence;

import bank4.jdbc.simulation.SimulationEntry;
import bank4.jdbc.simulation.StatisticManager;

/**
 * General interface for persistence APIs.
 */
public interface StatisticPersistence {

	int persist(SimulationEntry simulationEntry, StatisticManager statisticManager);

	int servedClientCount(int simulationEntryId);
	
	int nonServedClientCount(int simulationEntryId);

}
