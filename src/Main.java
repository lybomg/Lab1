import java.io.*;
import java.util.*;

interface Function {
    double value(double x);
}

// Аналітична функція
class AnalyticalFunction implements Function {

    int type;
    double a;

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

// Таблична функція
class TableFunction implements Function {

    ArrayList<Double> xs = new ArrayList<>();
    ArrayList<Double> ys = new ArrayList<>();

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

// Обчислення похідної
class Differentiator {

    static double derivative(Function f, double x, double h) {
        return (f.value(x + h) - f.value(x - h)) / (2 * h);
    }
}

public class Main {

    public static void main(String[] args) {

        double start = 1.5;
        double end = 6.0;
        double step = 0.05;
        double h = 0.0001;

        // функція 1
        Function f1 = new AnalyticalFunction(1, 0);
        save(f1, "function1.txt", start, end, step, h);

        // функція 2
        double[] aValues = {0.5, 1.0, 1.5};

        for (double a : aValues) {
            Function f2 = new AnalyticalFunction(2, a);
            save(f2, "function2_a_" + a + ".txt",
                    start, end, step, h);
        }

        // таблична функція
        createSinTable("sin_table.txt", start, end, step);

        Function f3 = new TableFunction("sin_table.txt");
        save(f3, "function3.txt", start, end, step, h);

        System.out.println("Готово!");
    }

    // запис результатів
    static void save(Function f,
                     String file,
                     double start,
                     double end,
                     double step,
                     double h) {

        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));

            for (double x = start; x <= end; x += step) {

                double y = f.value(x);
                double dy = Differentiator.derivative(f, x, h);

                out.printf(Locale.US,
                        "%.4f %.10f %.10f\n", x, y, dy);
            }

            out.close();

        } catch (Exception e) {
            System.out.println("Помилка запису");
        }
    }

    // створення таблиці sin(x)
    static void createSinTable(String file,
                               double start,
                               double end,
                               double step) {

        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));

            for (double x = start; x <= end; x += step) {
                out.printf(Locale.US,
                        "%.4f %.8f\n", x, Math.sin(x));
            }

            out.close();

        } catch (Exception e) {
            System.out.println("Помилка створення таблиці");
        }
    }
}