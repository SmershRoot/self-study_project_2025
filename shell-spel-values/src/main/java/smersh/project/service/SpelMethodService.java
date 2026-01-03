package smersh.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.support.ReflectiveIndexAccessor;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import smersh.project.holder.ContextHolder;
import smersh.project.model.FruitMap;
import smersh.project.model.TestObjectWithList;
import smersh.project.overloader.operator.ListOperatorOverloader;

import java.awt.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import smersh.project.resolver.TestBeanResolver;

/**
 * Наборы методов с SPEL
 */
@Service
public class SpelMethodService {

    @Autowired
    private ApplicationContext applicationContext;

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


    public String getValueFromMethod() {
        TestObjectWithList testObject = new TestObjectWithList("Nikola Tesla", null, new ArrayList<>(List.of("Zero")));

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext(testObject);
        try {
            context.setVariable("reverseString", StringUtils.class.getMethod("reverse", String.class));
            context.setVariable("reverseTestValue", this.getClass().getDeclaredMethod("getTestValue", String.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        var resTrue = parser.parseExpression("isCurrentName('Nikola Tesla')").getValue(context, Boolean.class);
        var resFalse = parser.parseExpression("isCurrentName('Vang')").getValue(context, Boolean.class);
        var name = parser.parseExpression("getName()").getValue(context, String.class);
        var getReverseValue = parser.parseExpression("#reverseString(getName())").getValue(context, String.class);
        var getTestValue = parser.parseExpression("#reverseTestValue(getName())").getValue(context, String.class);
        return "isCurrentName('Nikola Tesla'): %s\nisCurrentName('Vang'): %s\ngetName(): %s\ngetReverseValue: %s\ngetTestValue: %s"
                .formatted(resTrue, resFalse, name, getReverseValue, getTestValue);
    }

    public String getValueRootAndThis() {
        TestObjectWithList testObject = new TestObjectWithList("Nikola Tesla", null,
                new ArrayList<>(List.of("Zero", "One", "Two")));
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext(testObject);
        String expression = "#root.name + '\n ' + #root.getList.![length() + ' ' + #root.name + ' ' + #this.toString()+'\n']";
        return parser.parseExpression(expression)
                .getValue(context, String.class);
    }

    /**
     * Примеры использования функций через registerFunction и setVariable
     */
    public String getValueFunctions() throws NoSuchMethodException, IllegalAccessException {
        StandardEvaluationContext context = new StandardEvaluationContext();
        ExpressionParser parser = new SpelExpressionParser();
        MethodHandle formatted = MethodHandles.lookup().findVirtual(String.class, "formatted",
                MethodType.methodType(String.class, Object[].class));
        MethodHandle reverse = MethodHandles.lookup().findStatic(StringUtils.class, "reverse",
                MethodType.methodType(String.class, String.class));

        context.registerFunction("reverseString", StringUtils.class.getMethod("reverse", String.class));
        context.registerFunction("formatted", formatted);
        context.registerFunction("reverse", reverse);
        context.setVariable("formattedV", formatted);
        context.setVariable("reverseV", reverse);

        String resultOneBlock = parser.parseExpression(
                "'Function reverseString: ' + #reverseString('hello')+'\n'+"
                        + "'Function formatted: ' + #formatted('Result - <%s>', 'OK')+'\n'+"
                        + "'Function reverse: ' + #reverse('hello')+'\n'+"
                        + "'Variable formatted: ' + #formattedV('Result - <%s>', 'OK')+'\n'+"
                        + "'Variable reverse: ' + #reverseV('hello')"
                ).getValue(context, String.class);

        EvaluationContext evaContext = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        evaContext.setVariable("reverseV", reverse);
        //evaContext.registerFunction("reverse", reverse); Нет такого

        return resultOneBlock;
    }

    /**
     * Работа через получение контекста приложения и BeanResolver
     */
    public String getValueBean() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        ExpressionParser parser = new SpelExpressionParser();
        context.setBeanResolver(new BeanResolver(applicationContext));
        Expression expressionFruitMap = parser.parseExpression("@fruitMap.get(T(java.awt.Color).RED)");
        Expression expressionTestService = parser.parseExpression("@testService.doSomething()");
        return "FruitMap: %s\nTestService: %s".formatted(
                expressionFruitMap.getValue(context, String.class),
                expressionTestService.getValue(context, String.class)
        );
    }

    public String getValueTestBeanResolver() {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new TestBeanResolver());
        Expression expressionFruitMap = parser.parseExpression("@fruitMap.get(T(java.awt.Color).RED)");
        Expression expressionTestService = parser.parseExpression("@testService.doSomething()");
        Expression expressionNotAccess= parser.parseExpression("@notAccess.doSomething()");
        String notAccess = "";
        try {
            notAccess = expressionNotAccess.getValue(context, String.class);
        } catch (Exception e) {
            notAccess = e.getCause().getMessage();
        }
        return "BeanResolver\nFruitMap: %s\nTestService: %s\nNotAccess: %s".formatted(
                expressionFruitMap.getValue(context, String.class),
                expressionTestService.getValue(context, String.class),
                notAccess
        );

    }


    private static String getTestValue(String value) {
        return StringUtils.reverse(value);
    }

}
