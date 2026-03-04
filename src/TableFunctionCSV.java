import java.io.*;
import java.util.*;

class TableFunctionCSV implements Function {

    private TreeMap<Double, Double> map = new TreeMap<>();
    private TreeSet<Double> set = new TreeSet<>();

    TableFunctionCSV(String filename) {

        try {
            Scanner sc = new Scanner(new File(filename));
            sc.useDelimiter(",");

            while (sc.hasNext()) {
                double x = Double.parseDouble(sc.next());
                double y = Double.parseDouble(sc.next());

                map.put(x, y);
                set.add(x);
            }

            sc.close();
        }
        catch (Exception e) {
            System.out.println("Помилка читання CSV");
        }
    }

    public double value(double x) {

        if (map.containsKey(x))
            return map.get(x);

        Double lower = map.floorKey(x);
        Double higher = map.ceilingKey(x);

        if (lower == null || higher == null)
            return 0;

        double y1 = map.get(lower);
        double y2 = map.get(higher);

        return y1 + (y2 - y1) * (x - lower) / (higher - lower);
    }
}