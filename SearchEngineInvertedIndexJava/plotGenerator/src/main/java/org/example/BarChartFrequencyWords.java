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
import java.util.HashMap;
import java.util.Map;

public class BarChartFrequencyWords extends JFrame {

    private JFreeChart topChart;
    private JFreeChart bottomChart;

    public BarChartFrequencyWords(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createChart(Map<String, Double> topData, Map<String, Double> bottomData) {
        DefaultCategoryDataset topDataset = createDataset(topData);
        DefaultCategoryDataset bottomDataset = createDataset(bottomData);

        topChart = ChartFactory.createBarChart(
                "Average Time - Most Frequent Words",
                "Data Structure",
                "Time (ms)",
                topDataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        bottomChart = ChartFactory.createBarChart(
                "Average Time - Least Frequent Words",
                "Data Structure",
                "Time (ms)",
                bottomDataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        customizeChartAppearance(topChart);
        customizeChartAppearance(bottomChart);

        JPanel chartPanelTop = new ChartPanel(topChart);
        JPanel chartPanelBottom = new ChartPanel(bottomChart);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(chartPanelTop);
        add(chartPanelBottom);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void saveChartAsImage(String topChartPath, String bottomChartPath) throws IOException {
        ChartUtils.saveChartAsPNG(new File(topChartPath), topChart, 800, 600);
        ChartUtils.saveChartAsPNG(new File(bottomChartPath), bottomChart, 800, 600);
    }

    private void customizeChartAppearance(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(Color.WHITE);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.decode("#7cb6f0"));
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());

        renderer.setDrawBarOutline(false);

        Font titleFont = new Font("Times New Roman", Font.BOLD, 16);
        Font axisFont = new Font("Times New Roman", Font.PLAIN, 12);
        chart.getTitle().setFont(titleFont);
        plot.getDomainAxis().setLabelFont(axisFont);
        plot.getRangeAxis().setLabelFont(axisFont);
        plot.getDomainAxis().setTickLabelFont(axisFont);
        plot.getRangeAxis().setTickLabelFont(axisFont);

        plot.getRangeAxis().setStandardTickUnits(org.jfree.chart.axis.NumberAxis.createIntegerTickUnits());
    }

    private DefaultCategoryDataset createDataset(Map<String, Double> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), "Time", entry.getKey());
        }
        return dataset;
    }

    public static void main(String[] args) {
        Map<String, Double> topData = new HashMap<>();
        topData.put("TXT", 15.3406);
        topData.put("MongoDB", 246.9963);
        topData.put("Neo4j", 376.3965);

        Map<String, Double> bottomData = new HashMap<>();
        bottomData.put("TXT", 17.4641);
        bottomData.put("MongoDB", 40.0985);
        bottomData.put("Neo4j", 124.3623);

        SwingUtilities.invokeLater(() -> {
            try {
                BarChartFrequencyWords chart = new BarChartFrequencyWords("Word Performance Charts");
                chart.createChart(topData, bottomData);
                chart.saveChartAsImage("top_words_chart_ms_without_binary.png", "bottom_words_chart_ms_without_binary.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
