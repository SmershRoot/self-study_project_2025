package smersh.project.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppModelConfig {
    @Bean
    public Map<Color, String> fruitMap() {
        Map<Color, String> map = new HashMap<>();
        map.put(Color.RED, "Apple");
        map.put(Color.GREEN, "Banana");
        return map;
    }

}
