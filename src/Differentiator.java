class Differentiator {

    private double h = 0.0001;

    double derivative(Function f, double x) {
        return (f.value(x + h) - f.value(x - h)) / (2 * h);
    }
}