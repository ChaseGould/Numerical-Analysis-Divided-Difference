import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


public class XYSeriesDemo extends ApplicationFrame {

public XYSeriesDemo(final String title, ArrayList<Double> xi, ArrayList<Double> yi, 
		ArrayList<Double> xInputsInterpolation, ArrayList<Double> pxi, ArrayList<Double> chebyshevPoints, 
		ArrayList<Double> chebyYi) {

    super(title);
    final XYSeries xAndYSeries = new XYSeries("F(x)");
    for (int i = 0; i < xi.size(); i++) {
    	xAndYSeries.add(xi.get(i), yi.get(i));
    }
    
    final XYSeries PXSeries = new XYSeries("P(x)");
    for (int i = 0; i < xInputsInterpolation.size(); i++) {
    	PXSeries.add(xInputsInterpolation.get(i), pxi.get(i));
    }
    
    final XYSeries ChebyshevSeries = new XYSeries("Chebyshev");
    for (int i = 0; i < chebyshevPoints.size(); i++) {
    	ChebyshevSeries.add(chebyshevPoints.get(i), chebyYi.get(i));
    }
    
    //create an xyseriescollection and pass both series to it.
    final XYSeriesCollection XvsY = new XYSeriesCollection(xAndYSeries);
    XvsY.addSeries(PXSeries);
    XvsY.addSeries(ChebyshevSeries);
 
    final JFreeChart chart = ChartFactory.createXYLineChart(
       title,
        "X", 
        "Y", 
        XvsY,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );

    final ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(1800, 972));
    setContentPane(chartPanel);

	}
}