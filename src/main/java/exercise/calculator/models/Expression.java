package exercise.calculator.models;

import exercise.calculator.exceptions.ExpressionValidationException;
import exercise.calculator.models.operators.BinaryOperatorBase;
import exercise.calculator.models.operators.OperatorFactory;

import java.util.Stack;

public class Expression {
    private String trimmedExpressionStr;

    private final Stack<Double> operandsStack = new Stack<>();
    private final Stack<BinaryOperatorBase> operatorsStack = new Stack<>();

    public Expression(String expressionStr) {
        trimmedExpressionStr = expressionStr == null ? null : expressionStr.replaceAll("\\s", "");
    }

    public String getTrimmedExpressionStr() {
        return trimmedExpressionStr;
    }

    public void validate() throws ExpressionValidationException {
        if (!Character.isDigit(trimmedExpressionStr.charAt(0))) {
            throw new ExpressionValidationException("Expression must start with a digit");
        }

        if (!Character.isDigit(trimmedExpressionStr.charAt(trimmedExpressionStr.length() - 1))) {
            throw new ExpressionValidationException("Expression must end with a digit");
        }

        for (int i = 1; i < trimmedExpressionStr.length() - 1; i++) {
            char currentChar = trimmedExpressionStr.charAt(i);
            if (!Character.isDigit(currentChar)) {
                if (OperatorFactory.getInstance(currentChar) == null) {
                    throw new ExpressionValidationException(
                            String.format("Expression '%s' contains unknown character: %s at index %s",
                                    trimmedExpressionStr, currentChar, i));
                }

                char previousChar = trimmedExpressionStr.charAt(i - 1);
                if (!Character.isDigit(previousChar)) {
                    throw new ExpressionValidationException(
                            String.format("Expression '%s' contains 2 consecutive operators: %s%s at index %s",
                                    trimmedExpressionStr, previousChar, currentChar, i - 1));
                }
            }
        }
    }

    public double evaluate() {
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

        return operandsStack.pop();
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
