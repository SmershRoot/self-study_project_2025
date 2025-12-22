package smersh.project.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import smersh.project.service.SpelValueService;

@ShellComponent
public class ShellCommands {

    private final SpelValueService spelValueService;

    public ShellCommands(SpelValueService spelValueService) {
        this.spelValueService = spelValueService;
    }

    @ShellMethod(value = "Test command", key = "test")
    public String test() {
        return "Test command";
    }

    @ShellMethod(value = "Test command", key = "spel-value")
    public String getSpelValue() {
        return spelValueService.getSpelValue();
    }

    @ShellMethod(value = "Test command", key = "spel-method")
    public String getSpelMethod() {
        return spelValueService.getSpelValue();
    }

}
