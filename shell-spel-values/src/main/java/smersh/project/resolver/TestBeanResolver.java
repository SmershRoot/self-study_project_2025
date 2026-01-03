package smersh.project.resolver;

import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import smersh.project.service.test.TestService;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestBeanResolver implements BeanResolver {

    @Override
    public Object resolve(EvaluationContext context, String beanName) throws AccessException {
        if (!isAllowedBean(beanName)) {
            throw new AccessException("Access denied to bean: " + beanName);
        }

        return switch (beanName) {
            case "fruitMap" -> Map.of(Color.RED, "Apple", Color.GREEN, "Grape", Color.BLUE, "Watermelon");
            case "testService" -> new TestService();
            default -> null;
        };
    }

    private boolean isAllowedBean(String beanName) {
        Set<String> allowedBeans = new HashSet<>(Arrays.asList("fruitMap", "testService")); // список разрешенных бинов
        return allowedBeans.contains(beanName);
    }
}
