package exercise.calculator.models.operators;

public class DivisionOperator extends BinaryOperatorBase {
    public DivisionOperator(int priority) {
        super(priority);
    }

    @Override
    public double evaluate(double leftSide, double rightSide) {
        return leftSide / rightSide;
    }
}
