package smersh.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Наборы методов с SPEL
 */
@Service
public class SpelValueService {

    @Value("${spel.value}")
    private String spelValue;

    @Value("#{new java.util.Date().toString()}")
    private String spelDateValue;


    @Value("#{'Hello World'.concat('!')}")
    private String hw;

    public String getSpelValue() {
        return spelValue + "\n" + spelDateValue + "\n" + hw;
    }


}
