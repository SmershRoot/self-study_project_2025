package smersh.project.service;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.stereotype.Service;
import smersh.project.model.TestObjectWithList;

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

    public String setListValue() {
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, Calendar.AUGUST, 9);
        TestObjectWithList testObject = new TestObjectWithList("Nikola Tesla", c.getTime(), new ArrayList<>(List.of("Zero")));

        SpelParserConfiguration config = new SpelParserConfiguration(true, true);

        ExpressionParser parser = new SpelExpressionParser(config);

        Expression exp = parser.parseExpression("list");
        var list = (List) exp.getValue(testObject);
        exp = parser.parseExpression("list[3]");
//        exp.getValue(testObject);
        exp.setValue(testObject, "Three");

        return "Был в массиве 1 элемент [0] = 'Zero', добавили list[3] и стало: " + list.size() + " элементов:\n"
                + IntStream.range(0, list.size()).mapToObj(i -> i + ": " + list.get(i)).collect(Collectors.joining("\n"));
    }




}
