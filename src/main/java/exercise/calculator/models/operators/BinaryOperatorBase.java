package exercise.calculator.models.operators;

public abstract class BinaryOperatorBase {
    private final int priority;

    public BinaryOperatorBase(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public abstract double evaluate(double leftSide, double rightSide);
}
