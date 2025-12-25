package smersh.project.model;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FruitMap {

    private final Map<Color, String> map = new HashMap<>();

    public FruitMap() {
        this.map.put(Color.RED, "cherry");
        this.map.put(Color.ORANGE, "orange");
        this.map.put(Color.YELLOW, "banana");
    }

    public String getFruit(Color color) {
        return this.map.get(color);
    }

    public void setFruit(Color color, String fruit) {
        this.map.put(color, fruit);
    }
}