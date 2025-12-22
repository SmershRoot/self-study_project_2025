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

    @ShellMethod(value = "Test command", key = "spel-value")
    public String getSpelValue() {
        return spelValueService.getSpelValue();
    }

    @ShellMethod(value = "Test command", key = "spel-method")
    public String getSpelMethod() {
        return spelMethodService.getSpelValue();
    }

    @ShellMethod(value = "Test command", key = "spel-method-set-list")
    public String setListValue() {
        return spelMethodService.setListValue();
    }


}
