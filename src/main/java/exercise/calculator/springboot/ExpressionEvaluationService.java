package exercise.calculator.springboot;

import exercise.calculator.exceptions.ExpressionValidationException;
import exercise.calculator.models.Expression;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExpressionEvaluationService {
    @Cacheable("expression-results")
    public double evaluate(String expressionStr) throws ExpressionValidationException {
        Expression expression = new Expression(expressionStr);
        return expression.evaluate();
    }
}
