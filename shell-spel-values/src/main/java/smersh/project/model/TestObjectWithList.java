package smersh.project.model;

import lombok.Getter;

import java.util.Date;
import java.util.List;

public class TestObjectWithList {
    @Getter
    private List<String> list;
    @Getter
    private String name;
    @Getter
    private Date date;

    public TestObjectWithList(String name, Date date, List<String> list) {
        this.name = name;
        this.date = date;
        this.list = list;
    }

    public boolean isCurrentName(String name) {
        return this.name.equalsIgnoreCase(name);
    }
}
