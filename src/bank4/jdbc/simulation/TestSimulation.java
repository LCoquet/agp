package bank4.jdbc.simulation;

import bank4.jdbc.container.SpringContainer;

public class TestSimulation {

	public static void main(String[] args) {
		Simulation simulation = (Simulation) SpringContainer.getBean("simulation");
		simulation.buildBank();
		simulation.simulate();
		System.out.println(simulation.simulationResults());
		simulation.graphicalResults();
	}
}
