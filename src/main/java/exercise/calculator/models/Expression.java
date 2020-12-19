package exercise.calculator.models;

import exercise.calculator.exceptions.ExpressionValidationException;
import exercise.calculator.models.operators.BinaryOperatorBase;
import exercise.calculator.models.operators.OperatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class Expression {
    private static final Logger LOGGER = LoggerFactory.getLogger(Expression.class);

    private final String trimmedExpressionStr;

    private final Stack<Double> operandsStack = new Stack<>();
    private final Stack<BinaryOperatorBase> operatorsStack = new Stack<>();

    public Expression(String expressionStr) {
        trimmedExpressionStr = expressionStr == null ? null : expressionStr.replaceAll("\\s", "");
    }

    public double evaluate() throws ExpressionValidationException {
        LOGGER.info(String.format("Evaluating expression '%s'", trimmedExpressionStr));

        validate();

        initEvaluationStacks();
        StringBuilder operandStringBuilder = new StringBuilder();

        for (int i = 0; i < trimmedExpressionStr.length(); i++) {
            char currentChar = trimmedExpressionStr.charAt(i);
            if (Character.isDigit(currentChar)) {
                operandStringBuilder.append(currentChar);
            } else {
                int operand = Integer.parseInt(operandStringBuilder.toString());
                operandsStack.push((double) operand);
                operandStringBuilder = new StringBuilder();

                BinaryOperatorBase currentOperator = OperatorFactory.getInstance(currentChar);
                evaluateOperatorsEqualToOrHigherPriority(currentOperator.getPriority());
                operatorsStack.push(currentOperator);
            }
        }

        int lastOperand = Integer.parseInt(operandStringBuilder.toString());
        operandsStack.push((double) lastOperand);

        evaluateAllOperators();

        double result = operandsStack.pop();

        LOGGER.info(String.format("Done evaluating expression '%s'. Result: %s", trimmedExpressionStr, result));

        return result;
    }

    private void validate() throws ExpressionValidationException {
        LOGGER.info(String.format("Validating expression '%s'", trimmedExpressionStr));

        if (!Character.isDigit(trimmedExpressionStr.charAt(0))) {
            String errorMsg = "Expression must start with a digit";
            LOGGER.error(errorMsg);
            throw new ExpressionValidationException(errorMsg);
        }

        if (!Character.isDigit(trimmedExpressionStr.charAt(trimmedExpressionStr.length() - 1))) {
            String errorMsg = "Expression must end with a digit";
            LOGGER.error(errorMsg);
            throw new ExpressionValidationException(errorMsg);
        }

        for (int i = 1; i < trimmedExpressionStr.length() - 1; i++) {
            char currentChar = trimmedExpressionStr.charAt(i);
            if (!Character.isDigit(currentChar)) {
                if (OperatorFactory.getInstance(currentChar) == null) {
                    String errorMsg = String.format("Expression '%s' contains unknown character: %s at index %s",
                            trimmedExpressionStr, currentChar, i);
                    LOGGER.error(errorMsg);
                    throw new ExpressionValidationException(errorMsg);
                }

                char previousChar = trimmedExpressionStr.charAt(i - 1);
                if (!Character.isDigit(previousChar)) {
                    String errorMsg = String.format(
                            "Expression '%s' contains 2 consecutive operators: %s%s at index %s",
                            trimmedExpressionStr, previousChar, currentChar, i - 1);
                    LOGGER.error(errorMsg);
                    throw new ExpressionValidationException(errorMsg);
                }
            }
        }

        LOGGER.info(String.format("Expression '%s' is valid", trimmedExpressionStr));
    }

    private void initEvaluationStacks() {
        operandsStack.clear();
        operatorsStack.clear();
    }

    private void evaluateOperatorsEqualToOrHigherPriority(int minPriority) {
        while (!operatorsStack.empty() && minPriority <= operatorsStack.peek().getPriority()) {
            evaluateTopOperand();
        }
    }

    private void evaluateAllOperators() {
        while (!operatorsStack.empty()) {
            evaluateTopOperand();
        }
    }

    private void evaluateTopOperand() {
        BinaryOperatorBase lastOperator = operatorsStack.pop();
        double rightSideOperand = operandsStack.pop();
        double leftSideOperand = operandsStack.pop();

        double result = lastOperator.evaluate(leftSideOperand, rightSideOperand);
        operandsStack.push(result);
    }
}
