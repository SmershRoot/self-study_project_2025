package smersh.project.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Society {

    private String name;
    private List<Inventor> members = new ArrayList<>();
    private Map<String, Inventor> officers = new HashMap();
}