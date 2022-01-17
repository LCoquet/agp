package bank4.jdbc.chart;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


public class LineChart extends ApplicationFrame {
	private static final long serialVersionUID = 1L;
	private double cashierOccupation[];
	
	/*public LineChart( String applicationTitle , String chartTitle, int cashierOccup[]) {
		super(applicationTitle);
		this.cashierOccupation = cashierOccup;
		JFreeChart lineChart = ChartFactory.createLineChart(chartTitle,"Cashier occupation","Cashier occupation",
				createDataset(), PlotOrientation.VERTICAL, true,true,false);
		         
		ChartPanel chartPanel = new ChartPanel( lineChart );
		chartPanel.setPreferredSize( new java.awt.Dimension(560 , 367 ) );
		setContentPane( chartPanel );
	}*/
	public LineChart(String title, double cashierOccup[]) {
		super(title);
		this.cashierOccupation = cashierOccup;
		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);

	}
	   private XYDataset createDataset() {
			
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries series= new XYSeries("First");
			for (int i = 0; i < cashierOccupation.length; i++) {
				series.add(i, cashierOccupation[i]);
			}
			dataset.addSeries(series);
			return dataset;
		}
	   private JFreeChart createChart(XYDataset dataset) {
			return ChartFactory.createXYLineChart("Line Chart Demo 6", "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
		}
}
