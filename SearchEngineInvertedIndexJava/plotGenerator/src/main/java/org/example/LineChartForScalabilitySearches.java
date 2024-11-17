package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class LineChartForScalabilitySearches extends JFrame {

    private JFreeChart chart;

    public LineChartForScalabilitySearches(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createChart(List<DataPoint> data) {
        DefaultCategoryDataset dataset = createDataset(data);

        chart = ChartFactory.createLineChart(
                "Comparison of Search Times with Varying Number of Books Without Binary File",
                "Book Count",
                "Search Time (ms)",
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

    private DefaultCategoryDataset createDataset(List<DataPoint> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (DataPoint point : data) {
            dataset.addValue(point.fileSystemSearch, "FileSystemSearch", String.valueOf(point.bookCount));
            dataset.addValue(point.neo4jSearch, "Neo4jSearch", String.valueOf(point.bookCount));
            dataset.addValue(point.mongoDBSearch, "MongoDBSearch", String.valueOf(point.bookCount));
            dataset.addValue(point.binarySearch, "BinarySearch", String.valueOf(point.bookCount));
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
        renderer.setSeriesPaint(0, Color.decode("#FF9800"));
        renderer.setSeriesPaint(1, Color.decode("#D32F2F"));
        renderer.setSeriesPaint(2, Color.decode("#4CAF50"));
        renderer.setSeriesPaint(3, Color.decode("#7cb6f0"));


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


    public static class DataPoint {
        int bookCount;
        int fileSystemSearch;
        int neo4jSearch;
        int mongoDBSearch;
        int binarySearch;

        public DataPoint(int bookCount, int fileSystemSearch, int neo4jSearch, int mongoDBSearch, int BinarySearch) {
            this.bookCount = bookCount;
            this.fileSystemSearch = fileSystemSearch;
            this.neo4jSearch = neo4jSearch;
            this.mongoDBSearch = mongoDBSearch;
            this.binarySearch = BinarySearch;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<DataPoint> data = List.of(
                    new DataPoint(10, 14, 10, 29, 3392),
                    new DataPoint(50, 9, 6, 6,3493),
                    new DataPoint(100, 8, 6, 10,3741),
                    new DataPoint(500, 11, 6, 5,4975),
                    new DataPoint(1000, 7, 7, 7,7783)
            );

            LineChartForScalabilitySearches chart = new LineChartForScalabilitySearches("Benchmark Performance by Search Type");
            chart.createChart(data);
            try {
                chart.saveChartAsImage("scalability_ms.png");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
