package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class BarChartFrequencyOPSWords extends JFrame {

    private JFreeChart topChart;
    private JFreeChart bottomChart;

    public BarChartFrequencyOPSWords(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createChart(Map<String, Double> topData, Map<String, Double> bottomData) {
        DefaultCategoryDataset topDataset = createDataset(topData);
        DefaultCategoryDataset bottomDataset = createDataset(bottomData);
        topChart = ChartFactory.createBarChart(
                "Average Time - Most Frequent Words",
                "Data Structure",
                "Time (ops/ms)",
                topDataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        bottomChart = ChartFactory.createBarChart(
                "Average Time - Least Frequent Words",
                "Data Structure",
                "Time (ops/ms)",
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
        File topImageFile = new File(topChartPath);
        ChartUtils.saveChartAsPNG(topImageFile, topChart, 800, 600);

        File bottomImageFile = new File(bottomChartPath);
        ChartUtils.saveChartAsPNG(bottomImageFile, bottomChart, 800, 600);
    }

    private void customizeChartAppearance(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(Color.WHITE);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.decode("#7cb6f0")); // Azul mate
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
    }

    private DefaultCategoryDataset createDataset(Map<String, Double> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            String structure = entry.getKey();
            Double score = entry.getValue();
            dataset.addValue(score, "Time", structure);
        }
        return dataset;
    }

    public static void main(String[] args) {
        File jsonFile = new File("plotGenerator/src/main/resources/json/Frequency-QueryEngine-Benchmark.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Double> topData = new HashMap<>();
        Map<String, Double> bottomData = new HashMap<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonFile);

            for (JsonNode benchmark : rootNode) {
                String benchmarkName = benchmark.get("benchmark").asText();
                double score = benchmark.get("primaryMetric").get("score").asDouble();

                if (benchmarkName.contains("TopWords")) {
                    if (benchmarkName.contains("Binary")) topData.put("Binary", score);
                    else if (benchmarkName.contains("Mongo")) topData.put("MongoDB", score);
                    else if (benchmarkName.contains("Neo4j")) topData.put("Neo4j", score);
                    else if (benchmarkName.contains("Txt")) topData.put("TextFile", score);
                } else if (benchmarkName.contains("BottomWords")) {
                    if (benchmarkName.contains("Binary")) bottomData.put("Binary", score);
                    else if (benchmarkName.contains("Mongo")) bottomData.put("MongoDB", score);
                    else if (benchmarkName.contains("Neo4j")) bottomData.put("Neo4j", score);
                    else if (benchmarkName.contains("Txt")) bottomData.put("TextFile", score);
                }
            }

            BarChartFrequencyOPSWords chart = new BarChartFrequencyOPSWords("Word Benchmark");
            chart.createChart(topData, bottomData);

            chart.saveChartAsImage("top_chart_ops.png", "bottom_chart_ops.png");

        } catch (IOException e) {
            System.err.println("Error reading JSON: " + e.getMessage());
        }
    }
}
