package smersh.project.model;

import java.util.Date;
import java.util.List;

public class TestObjectWithList {
    public List<String> list;
    public String name;
    public Date date;

    public TestObjectWithList(String name, Date date, List<String> list) {
        this.name = name;
        this.date = date;
        this.list = list;
    }
}
