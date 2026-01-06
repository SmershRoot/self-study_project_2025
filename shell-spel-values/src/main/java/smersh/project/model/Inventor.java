package smersh.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.GregorianCalendar;

@Data
@NoArgsConstructor
public class Inventor {

    private String name;
    private String nationality;
    private String[] inventions;
    private Date birthdate;
    private PlaceOfBirth placeOfBirth;

    public Inventor(String name, String nationality) {
        GregorianCalendar c= new GregorianCalendar();
        this.name = name;
        this.nationality = nationality;
        this.birthdate = c.getTime();
    }

    public Inventor(String name, PlaceOfBirth placeOfBirth, String nationality) {
        this.name = name;
        this.nationality = nationality;
        this.placeOfBirth = placeOfBirth;
    }
}
