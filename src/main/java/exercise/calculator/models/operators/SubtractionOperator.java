package exercise.calculator.models.operators;

public class SubtractionOperator extends BinaryOperatorBase {
    public SubtractionOperator(int priority) {
        super(priority);
    }

    @Override
    public double evaluate(double leftSide, double rightSide) {
        return leftSide - rightSide;
    }
}
