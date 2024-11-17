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

public class MultiLineChartExample extends JFrame {

    private JFreeChart chart;

    public MultiLineChartExample(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createChart(String[] jsonFilePaths, String[] labels) throws IOException, JSONException {
        if (jsonFilePaths.length != labels.length) {
            throw new IllegalArgumentException("The number of file paths must match the number of labels.");
        }

        DefaultCategoryDataset dataset = createDataset(jsonFilePaths, labels);

        chart = ChartFactory.createLineChart(
                "Metadata - Processing Time by Storage System",
                "Number of Books (N)",
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

    private DefaultCategoryDataset createDataset(String[] jsonFilePaths, String[] labels) throws IOException, JSONException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < jsonFilePaths.length; i++) {
            String jsonFilePath = jsonFilePaths[i];
            String label = labels[i];

            String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            JSONArray jsonArray = new JSONArray(jsonData);

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject obj = jsonArray.getJSONObject(j);
                String nValue = obj.getJSONObject("params").getString("N");
                double score = obj.getJSONObject("primaryMetric").getDouble("score");

                // Add data to the dataset
                dataset.addValue(score, label, nValue);
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
        renderer.setSeriesPaint(2, Color.decode("#4CAF50"));
        renderer.setSeriesPaint(3, Color.decode("#F44336"));

        renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6)); // Small circles
        renderer.setSeriesShape(1, new java.awt.geom.Rectangle2D.Double(-3, -3, 6, 6)); // Small squares
        renderer.setSeriesShape(2, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6)); // Small circles
        renderer.setSeriesShape(3, new java.awt.geom.Rectangle2D.Double(-3, -3, 6, 6)); // Small squares

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
                String[] jsonFiles = {
                        "plotGenerator/src/main/resources/json/Binary-Metadata_Store.json",
                        "plotGenerator/src/main/resources/json/Text-Metadata_Store.json",
                        "plotGenerator/src/main/resources/json/MongoDB-Metadata.json",
                        "plotGenerator/src/main/resources/json/Neo4J-Metadata.json"
                };
                String[] labels = {"Binary File", "Text File", "MongoDB", "Neo4j"};

                MultiLineChartExample chart = new MultiLineChartExample("Metadata - Storage Systems Processing Time");
                chart.createChart(jsonFiles, labels);
                chart.saveChartAsImage("multi_line_chart_metadata.png");
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
