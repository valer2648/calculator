package exercise.calculator.models.operators;

import java.util.HashMap;
import java.util.Map;

public class OperatorFactory {
    private static Map<Character, BinaryOperatorBase> operators;

    static {
        operators = new HashMap<>();
        operators.put('+', new AdditionOperator(1));
        operators.put('-', new SubtractionOperator(1));
        operators.put('*', new MultiplicationOperator(2));
        operators.put('/', new DivisionOperator(2));
    }

    public static BinaryOperatorBase getInstance(char representation) {
        return operators.get(representation);
    }
}
