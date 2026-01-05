package smersh.project.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import smersh.project.service.SpelMethodService;

/**
 * Команды для работы с SpEL через сервис методов
 */
@ShellComponent
public class SpelMethodShellCommand {

    private final SpelMethodService spelMethodService;

    public SpelMethodShellCommand(SpelMethodService spelMethodService) {
        this.spelMethodService = spelMethodService;
    }

    @ShellMethod(value = "Получение какого-то значения на основе выражения в методе со Spel", key = "spel-method")
    public String getTestValue() {
        return spelMethodService.getSpelValue();
    }

    @ShellMethod(value = "Инициализация фруктового объекта с Map-ой соответствия цвета и фрукта", key = "init-fruit")
    public String initFruit() {
        return spelMethodService.initFruitMapAccessor();
    }

    /**
     * TODO Добавить задание для цвета и фрукта
     */
    @ShellMethod(value = "Установка нового значения для соответствия цвета и фрукта", key = "set-fruit")
    public String setFruit() {
        return spelMethodService.setFruitValue();
    }

    /**
     * TODO Добавить передачу цвета
     */
    @ShellMethod(value = "Получение текущего значения фрукта для цвета", key = "get-fruit")
    public String getFruit() {
        return spelMethodService.getFruitValue();
    }

    @ShellMethod(value = "Очистка контекста порожденного инициализацией фруктового объекта", key = "clear-context")
    public String clearContext() {
        return "" + spelMethodService.clearContext();
    }

    @ShellMethod(value = "Добавление элементов в список с авторасширением списка", key = "spel-method-set-list")
    public String setListValue() {
        return spelMethodService.setListValue();
    }

    @ShellMethod(value = "Произвести переопределённые операции над 2-мя массивами", key = "operations-2lists")
    public String performOperationsOnArrays() {
        return String.join("\n", spelMethodService.performOperationsOnArrays());
    }

    @ShellMethod(value = "Работа с методом класса", key = "get-value-class-method")
    public String getValueClassMethod() {
        return spelMethodService.getValueFromMethod();
    }

    @ShellMethod(value = "Работа с Root и This", key = "get-rt-method")
    public String getValueRootAndThis() {
        return spelMethodService.getValueRootAndThis();
    }

    @ShellMethod(value = "Работа с Function", key = "get-func-method")
    public String getValueFunction() throws NoSuchMethodException, IllegalAccessException {
        return spelMethodService.getValueFunctions();
    }

    @ShellMethod(value = "Работа с Bean через BeanResolver с ApplicationContext", key = "get-bean-method")
    public String getValueBean() {
        return spelMethodService.getValueBean();
    }

    @ShellMethod(value = "Работа с Bean через свой BeanResolver", key = "get-bean-res-method")
    public String getValueTestBeanResolver() {
        return spelMethodService.getValueTestBeanResolver();
    }

    @ShellMethod(value = "Работа с Операциями", key = "get-oper-vals")
    public String getValueWithOperators() {
        return spelMethodService.getValueWithOperators();
    }



}
