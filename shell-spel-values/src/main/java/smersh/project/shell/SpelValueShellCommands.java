package smersh.project.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import smersh.project.service.SpelValueService;

@ShellComponent
public class SpelValueShellCommands {

    private final SpelValueService spelValueService;

    public SpelValueShellCommands(
            SpelValueService spelValueService
    ) {
        this.spelValueService = spelValueService;
    }

    @ShellMethod(value = "Test command", key = "test")
    public String test() {
        return "Test command";
    }

    @ShellMethod(value = "Получение какого-то значения на основе выражения в @Value", key = "spel-value")
    public String getSpelValue() {
        return spelValueService.getSpelValue();
    }

    @ShellMethod(value = "Получение значения фруктового объекта на основе выражения в @Value", key = "get-bean-fruit")
    public String getFruitValue() {
        return spelValueService.getFruitValue();
    }

}
