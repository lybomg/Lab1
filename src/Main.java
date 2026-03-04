import java.io.*;
import java.util.*;

interface Function {
    double value(double x);
}

class AnalyticalFunction implements Function {

    private int type;
    private double a;

    AnalyticalFunction(int type, double a) {
        this.type = type;
        this.a = a;
    }

    public double value(double x) {

        if (type == 1) {
            return Math.exp(-x * x) * Math.sin(x);
        } else {
            return Math.exp(-a * x * x) * Math.sin(x);
        }
    }
}

class TableFunction implements Function {

    private ArrayList<Double> xs = new ArrayList<>();
    private ArrayList<Double> ys = new ArrayList<>();

    TableFunction(String filename) {

        try {
            Scanner sc = new Scanner(new File(filename));
            sc.useLocale(Locale.US);

            while (sc.hasNextDouble()) {
                xs.add(sc.nextDouble());
                ys.add(sc.nextDouble());
            }

            sc.close();

        } catch (Exception e) {
            System.out.println("Помилка читання файлу");
        }
    }

    public double value(double x) {

        for (int i = 0; i < xs.size() - 1; i++) {

            double x1 = xs.get(i);
            double x2 = xs.get(i + 1);

            if (x >= x1 && x <= x2) {

                double y1 = ys.get(i);
                double y2 = ys.get(i + 1);

                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        return 0;
    }
}

class Differentiator {

    private double h;

    Differentiator(double h){
        this.h = h;
    }

    double derivative(Function f, double x) {
        return (f.value(x + h) - f.value(x - h)) / (2 * h);
    }
}

class FileManager {

    void save(String filename,
              Function f,
              Differentiator d,
              double start,
              double end,
              double step) {

        try {
            PrintWriter out = new PrintWriter(new FileWriter(filename));

            for (double x = start; x <= end; x += step) {

                double y = f.value(x);
                double dy = d.derivative(f, x);

                out.printf(Locale.US,
                        "%.4f %.10f %.10f\n", x, y, dy);
            }

            out.close();
        }
        catch (Exception e) {
            System.out.println("помилка запису");
        }
    }
    void createSinTable(String filename,
                        double start,
                        double end,
                        double step) {

        try {
            PrintWriter out = new PrintWriter(new FileWriter(filename));

            for (double x = start; x <= end; x += step)
                out.printf(Locale.US,
                        "%.4f %.8f\n", x, Math.sin(x));

            out.close();

        } catch (Exception e) {
            System.out.println("помилка таблиці");
        }
    }
}

class Calculator {

    private FileManager files;
    private Differentiator diff;

    Calculator(FileManager files, Differentiator diff){
        this.files = files;
        this.diff = diff;
    }

    void run(double start, double end, double step) {

        Function f1 = new AnalyticalFunction(1, 0);
        files.save("function1.txt", f1, diff,
                start, end, step);

        double[] aValues = {0.5, 1.0, 1.5};

        for (double a : aValues) {
            Function f2 =
                    new AnalyticalFunction(2, a);

            files.save("function2_" + a + ".txt",
                    f2, diff, start, end, step);
        }

        files.createSinTable("sin_table.txt",
                start, end, step);

        Function f3 =
                new TableFunction("sin_table.txt");

        files.save("function3.txt",
                f3, diff, start, end, step);
    }

}

public class Main {

    public static void main(String[] args) {

        double start = 1.5;
        double end = 6.0;
        double step = 0.05;

        FileManager files = new FileManager();
        Differentiator diff = new Differentiator(0.0001);

        Calculator calc =
                new Calculator(files, diff);

        calc.run(start, end, step);

        System.out.println("готово");
    }
}
