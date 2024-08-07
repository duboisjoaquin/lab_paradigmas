package namedEntities;

import java.util.List;

// an especial case of named entity 
public class Person extends NamedEntity {
    private int age = 0;
    private float height = 0;
    private String profession = "";

    public Person(String label, String category, List<String> topics,
            List<String> keywords, Integer count, int age, float height, String profession) {

        super(label, category, topics, keywords, count);
        this.age = age;
        this.height = height;
        this.profession = profession;
    }

    public int getEdad() {
        return age;
    }

    public float getAltura() {
        return height;
    }

    public String getProfesion() {
        return profession;
    }
}
