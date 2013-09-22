/**
 * 
 */
package mx.com.recomendare.web.admin;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.com.recomendare.web.session.AuthenticatedPage;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.time.Duration;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * @author jerry
 *
 */
public final class MemoryStatusChartPage extends AuthenticatedPage {
    private static final long serialVersionUID = 1L;

    //the time series to update...
    private TimeSeries freeMemoryTimeSerie;
    private TimeSeries maxMemoryTimeSerie;
    private TimeSeries allocatedMemoryTimeSerie;
    private TimeSeries totalFreeMemoryTimeSerie;

    /**
     * 
     */
    public MemoryStatusChartPage() {
        super();
        init();
    }

    private void init() {
        freeMemoryTimeSerie = new TimeSeries("Libre", Minute.class);
        maxMemoryTimeSerie = new TimeSeries("Maxima", Minute.class);
        allocatedMemoryTimeSerie = new TimeSeries("Alojada", Minute.class);
        totalFreeMemoryTimeSerie = new TimeSeries("Total Libre", Minute.class);

        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(freeMemoryTimeSerie);
        dataset.addSeries(maxMemoryTimeSerie);
        dataset.addSeries(allocatedMemoryTimeSerie);
        dataset.addSeries(totalFreeMemoryTimeSerie);

        final NonCachingImage memoryChart = new NonCachingImage("memoryChart", new DynamicImageResource() {
            private static final long serialVersionUID = 1L;

            protected byte[] getImageData() {
                Runtime runtime = Runtime.getRuntime();
                final long maxMemory = runtime.maxMemory();
                final long allocatedMemory = runtime.totalMemory();
                final long freeMemory = runtime.freeMemory();

                freeMemoryTimeSerie.add(new Minute(new Date()), freeMemory / 1024);
                maxMemoryTimeSerie.add(new Minute(new Date()), maxMemory / 1024);
                allocatedMemoryTimeSerie.add(new Minute(new Date()), allocatedMemory / 1024);
                totalFreeMemoryTimeSerie.add(new Minute(new Date()), (freeMemory + (maxMemory - allocatedMemory)) / 1024);

                JFreeChart chart = ChartFactory.createTimeSeriesChart(
                        "Memoria", // title
                        "Fecha", // x-axis label
                        "Mbytes", // y-axis label
                        dataset, // data
                        true, // create legend?
                        true, // generate tooltips?
                        false // generate URLs?
                        );
                chart.setBackgroundPaint(Color.white);

                XYPlot plot = (XYPlot) chart.getPlot();
                plot.setBackgroundPaint(Color.lightGray);
                plot.setDomainGridlinePaint(Color.white);
                plot.setRangeGridlinePaint(Color.white);
                //plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
                plot.setDomainCrosshairVisible(true);
                plot.setRangeCrosshairVisible(true);

                XYItemRenderer r = plot.getRenderer();
                if (r instanceof XYLineAndShapeRenderer) {
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
                    renderer.setBaseShapesVisible(true);
                    renderer.setBaseShapesFilled(true);
                }

                final DateAxis axis = (DateAxis) plot.getDomainAxis();
                axis.setDateFormatOverride(new SimpleDateFormat("hh:mma"));
                return toImageData(chart.createBufferedImage(450, 300));
            }
        });
        memoryChart.setOutputMarkupId(true);
        memoryChart.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(60)));
        add(memoryChart);
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return null;
	}

	/**
	 * 
	 */
	protected String getMetaDescription() {
		return "";
	}

	/**
	 * 
	 */
	protected String getMetaKeywords() {
		return "";
	}
    
}//END OF FILE