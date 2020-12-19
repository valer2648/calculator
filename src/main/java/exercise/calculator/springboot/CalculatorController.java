package exercise.calculator.springboot;

import exercise.calculator.exceptions.ExpressionValidationException;
import exercise.calculator.models.Expression;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class CalculatorController {
    @RequestMapping("/")
    public String showView() {
        return "index.html";
    }

    @RequestMapping("/eval")
    @ResponseBody
    public double evaluateExpression(@RequestParam(name = "expr") String expressionStr) {
        Expression expression = new Expression(expressionStr);
        try {
            expression.validate();
        } catch (ExpressionValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return expression.evaluate();
    }
}