package smersh.project.service;

import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.support.ReflectiveIndexAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import smersh.project.holder.ContextHolder;
import smersh.project.model.FruitMap;
import smersh.project.model.TestObjectWithList;
import smersh.project.overloader.operator.ListOperatorOverloader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Наборы методов с SPEL
 */
@Service
public class SpelMethodService {


    public String getSpelValue() {
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, Calendar.AUGUST, 9);
        TestObjectWithList testObject = new TestObjectWithList("Nikola Tesla", c.getTime(), new ArrayList<>(List.of("One")));

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("name == 'Nikola Tesla'");
        var isTesla = exp.getValue(testObject, Boolean.class);
        exp = parser.parseExpression("name");
        var name  = (String) exp.getValue(testObject);
        exp = parser.parseExpression("date");
        var date = exp.getValue(testObject);
        exp = parser.parseExpression("list");
        var list = (List) exp.getValue(testObject);
        return "This is Tesla? " + isTesla +
                "\nName:" + name +
                "\nDate:" + date +
                "\nListSize:" + list.size();
    }

    /**
     * Добавление элемента в список
     * <p>
     * <br>SpelParserConfiguration -устанавливаю метод MIXED, вычисление выражений автоматически переключается между
     * интерпретируемым и скомпилированным режимами
     * <br>Загрузчик классов использую текущий класс, думаю это лучше чем использовать потоковый (null)
     * <br>true - автодополнение свойств и коллекций
     * </p>
     *
     * @return
     */
    public String setListValue() {
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, Calendar.AUGUST, 9);
        TestObjectWithList testObject = new TestObjectWithList("Nikola Tesla", c.getTime(), new ArrayList<>(List.of("Zero")));

        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.MIXED, this.getClass().getClassLoader(), true, true, Integer.MAX_VALUE);

        ExpressionParser parser = new SpelExpressionParser(config);

        Expression exp = parser.parseExpression("list");
        var list = (List) exp.getValue(testObject);
        exp = parser.parseExpression("list[3]");
        exp.setValue(testObject, "Three");

        return "Был в массиве 1 элемент [0] = 'Zero', добавили list[3] и стало: " + list.size() + " элементов:\n"
                + IntStream.range(0, list.size()).mapToObj(i -> i + ": " + list.get(i)).collect(Collectors.joining("\n"));
    }

    public String initFruitMapAccessor() {
// Создание ReflectiveIndexAccessor для FruitMap
        IndexAccessor fruitMapAccessor = new ReflectiveIndexAccessor(
                FruitMap.class, Color.class, "getFruit", "setFruit");

// Создание контекста с IndexAccessor для FruitMap
//TODO если контекст уже есть, то не создавать новый, а использовать старый
//TODO И в отдельный метод
        var context = new StandardEvaluationContext();
        ContextHolder.setContext(context);

        context.addIndexAccessor(fruitMapAccessor);

// Создает фруктовый объект fruitMap и регистрирует его в контексте
        context.setVariable("fruitMap", new FruitMap());

//получение значения
        ExpressionParser parser = new SpelExpressionParser();
        String fruit = parser.parseExpression("#fruitMap[T(java.awt.Color).RED]")
                .getValue(context, String.class);
        return fruit;
    }

    public String setFruitValue() {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = ContextHolder.getContext();
        parser.parseExpression("#fruitMap[T(java.awt.Color).RED]")
                .setValue(context, "NEW FRUIT");
        String fruit = parser.parseExpression("#fruitMap[T(java.awt.Color).RED]")
                .getValue(context, String.class);
        return fruit;
    }

    public String getFruitValue() {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = ContextHolder.getContext();
        String fruit = parser.parseExpression("#fruitMap[T(java.awt.Color).RED]")
                .getValue(context, String.class);
        return fruit;
    }

    public boolean clearContext() {
        ContextHolder.clearContext();
        return true;
    }

    public List<String> performOperationsOnArrays() {
        List<Number> leftList = new ArrayList<>(List.of(1,2,3));
        List<Number> rightList = new ArrayList<>(List.of(4,5));

        List<String> result = new ArrayList<>();
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setOperatorOverloader(new ListOperatorOverloader());

        int numOperations = 1;
        for (Operation operation : Operation.values()) {
            result.add(performOperationOnArrays(parser, context, leftList, rightList, operation, numOperations));
            numOperations++;
        }
        return result;
    }

    private String performOperationOnArrays(
            ExpressionParser parser,
            StandardEvaluationContext context,
            List<Number> leftList,
            List<Number> rightList,
            Operation operation,
            int numOperations
    ) {
        var operationSymbol = getOperationSymbol(operation);
        context.setVariable("leftList", leftList);
        context.setVariable("rightList", rightList);
        var list = performOperationOnArrays(parser, context, operationSymbol);
        return "%d. Result for lists %s and %s with operation %s: %s".formatted(
                numOperations, leftList.toString(), rightList.toString(),
                operationSymbol, list.toString()
        );
    }

    private String getOperationSymbol(Operation operation) {
        return switch (operation) {
            case ADD -> "+";
            case SUBTRACT -> "-";
            case DIVIDE -> "/";
            case MULTIPLY -> "*";
            case MODULUS -> "%";
            case POWER -> "^";
        };
    }

    /**
     *
     * Примеры использования  parser.parseExpression с массивами
     * parser.parseExpression("{1, 2, 3} + {4, 5}")
     * parser.parseExpression("T(java.util.List).of(1,2,3) + T(java.util.List).of(1,2,3)")
     */
    public List<Number> performOperationOnArrays(
            ExpressionParser parser,
            StandardEvaluationContext context,
            String operation
    ) {
        var expressionString = "#leftList" + operation + "#rightList";

        return parser.parseExpression(expressionString).getValue(context, List.class);
    }

}
