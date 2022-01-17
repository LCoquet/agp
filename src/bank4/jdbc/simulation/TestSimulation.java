package bank4.jdbc.simulation;

import bank4.jdbc.container.SpringContainer;
import bank4.jdbc.persistence.DataInit;
import bank4.jdbc.persistence.HibernatePersistence;

import bank4.jdbc.chart.BarChart;

public class TestSimulation {
	
	public static void main(String[] args) {
		DataInit.createTables();
		HibernatePersistence.initAccountList();
		Simulation simulation = (Simulation) SpringContainer.getBean("simulation");
		simulation.buildBank();
		simulation.simulate();
		System.out.println(simulation.simulationResults());
		//simulation.graphicalResults();
	}
}
