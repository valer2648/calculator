package exercise.calculator.models.operators;

public class AdditionOperator extends BinaryOperatorBase {
    public AdditionOperator(int priority) {
        super(priority);
    }

    public double evaluate(double leftSide, double rightSide) {
        return leftSide + rightSide;
    }
}
