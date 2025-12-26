package smersh.project.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import smersh.project.service.SpelMethodService;
import smersh.project.service.SpelValueService;

@ShellComponent
public class ShellCommands {

    private final SpelValueService spelValueService;

    private final SpelMethodService spelMethodService;

    public ShellCommands(
            SpelValueService spelValueService,
            SpelMethodService spelMethodService
    ) {
        this.spelValueService = spelValueService;
        this.spelMethodService = spelMethodService;
    }

    @ShellMethod(value = "Test command", key = "test")
    public String test() {
        return "Test command";
    }

    @ShellMethod(value = "Получение какого-то значения на основе выражения в @Value", key = "spel-value")
    public String getSpelValue() {
        return spelValueService.getSpelValue();
    }

    @ShellMethod(value = "Получение какого-то значения на основе выражения в методе со Spel", key = "spel-method")
    public String getSpelMethod() {
        return spelMethodService.getSpelValue();
    }

    @ShellMethod(value = "Инициализация фруктового объекта с Map-ой соответствия цвета и фрукта", key = "init-fruit")
    public String initFruitMethod() {
        return spelMethodService.initFruitMapAccessor();
    }

    /**
     * TODO Добавить задание для цвета и фрукта
     */
    @ShellMethod(value = "Установка нового значения для соответствия цвета и фрукта", key = "set-fruit")
    public String setFruitMethod() {
        return spelMethodService.setFruitValue();
    }

    /**
     * TODO Добавить передачу цвета
     */
    @ShellMethod(value = "Получение текущего значения фрукта для цвета", key = "get-fruit")
    public String getFruitMethod() {
        return spelMethodService.getFruitValue();
    }

    @ShellMethod(value = "Очистка контекста порожденного инициализацией фруктового объекта", key = "clear-context")
    public String clearContext() {
        return "" + spelMethodService.clearContext();
    }

    @ShellMethod(value = "Получение значения фруктового объекта на основе выражения в @Value", key = "get-bean-fruit")
    public String getFruitValue() {
        return spelValueService.getFruitValue();
    }

    @ShellMethod(value = "Добавление элементов в список с авторасширением списка", key = "spel-method-set-list")
    public String setListValue() {
        return spelMethodService.setListValue();
    }


}
