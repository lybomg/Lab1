import javax.swing.*;
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.*;

public class GUI extends JFrame {

    private JTextField functionField = new JTextField("sin(x)");
    private JTextField startField = new JTextField("", 6);
    private JTextField stopField = new JTextField("", 6);
    private JTextField stepField = new JTextField("",6 );

    private ChartPanel chartPanel;

    public GUI() {

        setTitle("GUIApplication");
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Function"));

        JPanel inputPanel = new JPanel(new GridLayout(1,2));
        inputPanel.add(new JLabel("f(x):"));
        inputPanel.add(functionField);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        XYSeriesCollection dataset = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "X",
                "Y",
                dataset
        );

        chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JButton plotButton = new JButton("Plot");
        JButton exitButton = new JButton("Exit");

        bottomPanel.add(plotButton);
        bottomPanel.add(exitButton);
        bottomPanel.add(new JLabel("Start:"));
        bottomPanel.add(startField);
        bottomPanel.add(new JLabel("Stop:"));
        bottomPanel.add(stopField);
        bottomPanel.add(new JLabel("Step:"));
        bottomPanel.add(stepField);

        add(bottomPanel, BorderLayout.SOUTH);


        plotButton.addActionListener(e -> draw());
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void draw() {

        try {
            String formula = functionField.getText();
            double start = Double.parseDouble(startField.getText().replace(",", "."));
            double stop = Double.parseDouble(stopField.getText().replace(",", "."));
            double step = Double.parseDouble(stepField.getText().replace(",", "."));

            StringFunction f = new StringFunction(formula, 1);
            Differentiator d = new Differentiator();

            XYSeries functionSeries = new XYSeries("Function");
            XYSeries derivativeSeries = new XYSeries("Derivative");

            for (double x = start; x <= stop; x += step) {
                functionSeries.add(x, f.value(x));
                derivativeSeries.add(x, d.derivative(f, x));
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(functionSeries);
            dataset.addSeries(derivativeSeries);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "",
                    "X",
                    "Y",
                    dataset
            );

            chartPanel.setChart(chart);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Помилка введення даних");
        }
    }
}