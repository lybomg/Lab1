import edu.hws.jcm.data.*;
import edu.hws.jcm.functions.*;

class StringFunction implements Function {

    private Expression expr;
    private Variable x;
    private Variable a;

    StringFunction(String formula, double aValue) {

        Parser parser = new Parser();
        x = new Variable("x");
        a = new Variable("a");

        parser.add(x);
        parser.add(a);

        expr = parser.parse(formula);

        a.setVal(aValue);
    }

    public double value(double val) {
        x.setVal(val);
        return expr.getVal();
    }

    public double symbolicDerivative(double val) {

        Expression deriv = expr.derivative(x);
        x.setVal(val);
        return deriv.getVal();
    }
}