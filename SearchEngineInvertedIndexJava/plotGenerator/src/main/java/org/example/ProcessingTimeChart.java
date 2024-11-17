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
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(metadataTime, "Time", "Metadata");
        dataset.addValue(invertedIndexTime, "Time", "Inverted Index");

        chart = ChartFactory.createBarChart(
                "Processing Time Comparison",
                "Task",
                "Time (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        customizeChartAppearance();

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void customizeChartAppearance() {
        CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(Color.WHITE);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.decode("#7cb6f0"));
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());

        renderer.setDrawBarOutline(false);

        Font timesNewRoman = new Font("Times New Roman", Font.PLAIN, 14);
        chart.getTitle().setFont(new Font("Times New Roman", Font.BOLD, 16));
        plot.getDomainAxis().setLabelFont(timesNewRoman);
        plot.getRangeAxis().setLabelFont(timesNewRoman);
        plot.getDomainAxis().setTickLabelFont(timesNewRoman);
        plot.getRangeAxis().setTickLabelFont(timesNewRoman);
    }

    public void saveChartAsImage(String outputPath) throws IOException {
        File imageFile = new File(outputPath);
        ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ProcessingTimeChart chart = new ProcessingTimeChart("Processing Time Comparison");
                chart.createChart(589.89496, 35.70688);
                chart.saveChartAsImage("processing_time_chart.png");
            } catch (IOException e) {
                System.err.println("Error saving the chart: " + e.getMessage());
            }
        });
    }
}
