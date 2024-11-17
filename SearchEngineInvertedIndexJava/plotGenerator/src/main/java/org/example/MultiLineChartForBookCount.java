package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MultiLineChartForBookCount extends JFrame {

    private JFreeChart chart;

    public MultiLineChartForBookCount(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createChart(String jsonFilePath) throws IOException, JSONException {
        DefaultCategoryDataset dataset = createDataset(jsonFilePath);

        chart = ChartFactory.createLineChart(
                "Benchmark Performance by Book Count",
                "Book Count",
                "Time (ms/op)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
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

    private DefaultCategoryDataset createDataset(String jsonFilePath) throws IOException, JSONException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONArray jsonArray = new JSONArray(jsonData);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String benchmark = obj.getString("benchmark");
            String bookCount = obj.getJSONObject("params").getString("bookCount");

            JSONArray rawDataArray = obj.getJSONObject("primaryMetric").getJSONArray("rawData").getJSONArray(0);

            for (int j = 0; j < rawDataArray.length(); j++) {
                double time = rawDataArray.getDouble(j);
                dataset.addValue(time, benchmark, bookCount);
            }
        }

        return dataset;
    }

    private void customizeChartAppearance() {
        CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(Color.WHITE);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.decode("#7cb6f0"));
        renderer.setSeriesPaint(1, Color.decode("#FF9800"));

        plot.setRenderer(renderer);

        Font timesNewRoman = new Font("Times New Roman", Font.PLAIN, 14);
        chart.getTitle().setFont(new Font("Times New Roman", Font.BOLD, 16));
        plot.getDomainAxis().setLabelFont(timesNewRoman);
        plot.getRangeAxis().setLabelFont(timesNewRoman);
        plot.getDomainAxis().setTickLabelFont(timesNewRoman);
        plot.getRangeAxis().setTickLabelFont(timesNewRoman);
        chart.getLegend().setItemFont(timesNewRoman);

    }

    public void saveChartAsImage(String outputPath) throws IOException {
        File imageFile = new File(outputPath);
        ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String jsonFilePath = "plotGenerator/src/main/resources/json/Scalability-Benchmark.json";
                MultiLineChartForBookCount chart = new MultiLineChartForBookCount("Benchmark Performance by Book Count");
                chart.createChart(jsonFilePath);

                String outputPath = "SearchEngineInvertedIndexJava/PlotGeneratorStage2/benchmark_chart.png";
                chart.saveChartAsImage(outputPath);

            } catch (IOException | JSONException e) {
                System.err.println("Error: " + e.getMessage());
            }
        });
    }
}
