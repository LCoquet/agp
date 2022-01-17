package bank4.jdbc.simulation;

import org.jfree.ui.RefineryUtilities;

import bank4.jdbc.container.SpringContainer;
import bank4.jdbc.persistence.DataInit;
import bank4.jdbc.persistence.HibernatePersistence;

public class TestSimulation {
	
	public static void main(String[] args) {
		DataInit.createTables();
		HibernatePersistence.initAccountList();
		Simulation simulation = (Simulation) SpringContainer.getBean("simulation");
		simulation.buildBank();
		simulation.simulate();
		System.out.println(simulation.simulationResults());
		//simulation.graphicalResults();
		
		BarChart chart = new BarChart("Bar Chart", simulation.getConsultationAmount(), simulation.getWithdrawAmount(), simulation.getTransferAmount());
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
