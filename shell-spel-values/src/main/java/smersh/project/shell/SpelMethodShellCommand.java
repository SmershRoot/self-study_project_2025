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

}
