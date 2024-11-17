package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ProcessingTimeChart extends JFrame {

    private JFreeChart chart;

    public ProcessingTimeChart(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createChart(double invertedIndexTime, double metadataTime) {
        // Create dataset with reversed order
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(metadataTime, "Time", "Metadata"); // Add Metadata first
        dataset.addValue(invertedIndexTime, "Time", "Inverted Index"); // Add Inverted Index second

        // Create chart
        chart = ChartFactory.createBarChart(
                "Processing Time Comparison", // Title
                "Task",                        // X-axis label
                "Time (ms)",                   // Y-axis label
                dataset,                       // Data
                PlotOrientation.VERTICAL,
                false,                         // Disable legend
                true,
                false
        );

        // Customize chart appearance
        customizeChartAppearance();

        // Create panel for the chart
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void customizeChartAppearance() {
        CategoryPlot plot = chart.getCategoryPlot();

        // Set white background
        plot.setBackgroundPaint(Color.WHITE);

        // Enable and customize gridlines
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY); // Gridline color for X-axis
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);  // Gridline color for Y-axis

        // Customize bar colors: use the same color for all bars
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.decode("#7cb6f0")); // Matte blue for bars
        renderer.setShadowVisible(false); // Remove shadows
        renderer.setBarPainter(new StandardBarPainter()); // Standard bar painter

        // Remove bar outlines for a cleaner look
        renderer.setDrawBarOutline(false);

        // Apply Times New Roman font to all chart elements
        Font timesNewRoman = new Font("Times New Roman", Font.PLAIN, 14);
        chart.getTitle().setFont(new Font("Times New Roman", Font.BOLD, 16)); // Title in bold
        plot.getDomainAxis().setLabelFont(timesNewRoman);                    // X-axis label
        plot.getRangeAxis().setLabelFont(timesNewRoman);                     // Y-axis label
        plot.getDomainAxis().setTickLabelFont(timesNewRoman);                // X-axis tick labels
        plot.getRangeAxis().setTickLabelFont(timesNewRoman);                 // Y-axis tick labels
    }

    public void saveChartAsImage(String outputPath) throws IOException {
        File imageFile = new File(outputPath);
        ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ProcessingTimeChart chart = new ProcessingTimeChart("Processing Time Comparison");
                chart.createChart(589.89496, 35.70688); // Pass the data here
                chart.saveChartAsImage("processing_time_chart.png");
            } catch (IOException e) {
                System.err.println("Error saving the chart: " + e.getMessage());
            }
        });
    }
}
