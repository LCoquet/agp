package bank4.jdbc.simulation;

import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

public class BarChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	private int consultationAmount;
	private int withdrawAmount;
	private int transferAmount;

	public BarChart(String title, int consultationAmount, int withdrawAmount, int transferAmount) {

		super(title);

		this.consultationAmount = consultationAmount;
		this.withdrawAmount = withdrawAmount;
		this.transferAmount = transferAmount;
		
		CategoryDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(chartPanel);

	}

	private CategoryDataset createDataset() {

		String operations = "Operations";

		String consultation = "Consultation";
		String withdraw = "Withdraw";
		String transfer = "Transfer";

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(consultationAmount, operations, consultation);
		dataset.addValue(withdrawAmount, operations, withdraw);
		dataset.addValue(transferAmount, operations, transfer);

		return dataset;

	}

	private JFreeChart createChart(CategoryDataset dataset) {

		JFreeChart chart = ChartFactory.createBarChart("Bar Chart Demo", "Category", "Value", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		return chart;

	}
}
