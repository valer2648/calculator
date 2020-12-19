package exercise.calculator.models.operators;

public class MultiplicationOperator extends BinaryOperatorBase {
    public MultiplicationOperator(int priority) {
        super(priority);
    }

    @Override
    public double evaluate(double leftSide, double rightSide) {
        return leftSide * rightSide;
    }
}
