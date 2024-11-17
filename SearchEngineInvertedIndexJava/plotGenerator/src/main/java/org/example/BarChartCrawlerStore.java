package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;
import java.util.Map;

public class BarChartCrawlerStore extends JFrame {

    private JFreeChart chart;

    public BarChartCrawlerStore(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createChart(String jsonFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> benchmarks = objectMapper.readValue(
                new File(jsonFilePath),
                new TypeReference<List<Map<String, Object>>>() {}
        );

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map<String, Object> benchmark : benchmarks) {
            Map<String, String> params = (Map<String, String>) benchmark.get("params");
            String number = params.get("number");
            Map<String, Object> primaryMetric = (Map<String, Object>) benchmark.get("primaryMetric");
            double score = (double) primaryMetric.get("score");
            System.out.println(score);
            dataset.addValue(score, "Score", number);
        }

        // Create chart
        chart = ChartFactory.createBarChart(
                "Store Results",
                "Number of books",
                "Score (ms/op)",
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
                BarChartCrawlerStore chart = new BarChartCrawlerStore("Benchmark Results");
                chart.createChart("plotGenerator/src/main/resources/json/document_store_benchmark_results.json");
                chart.saveChartAsImage("crawler_store.png");
            } catch (IOException e) {
                System.err.println("Error creating the chart: " + e.getMessage());
            }
        });
    }
}
