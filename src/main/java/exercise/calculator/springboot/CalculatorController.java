package exercise.calculator.springboot;

import exercise.calculator.exceptions.ExpressionValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class CalculatorController {
    @Autowired
    private ExpressionEvaluationService expressionEvaluationService;

    @RequestMapping("/")
    public String showView() {
        return "index.html";
    }

    @RequestMapping("/eval")
    @ResponseBody
    public double evaluateExpression(@RequestParam(name = "expr") String expressionStr) {
        //Trim expression for better caching
        String trimmedExpression = expressionStr.replaceAll("\\s", "");
        try {
            return expressionEvaluationService.evaluate(trimmedExpression);
        } catch (ExpressionValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}