package namedEntities;

import java.util.List;

// an especial case of named entity 
public class Organization extends NamedEntity {

    private String typeOrganization = "";
    private int age = 0;
    private List<String> founderMembers = null;

    public Organization(String label, String category, List<String> topics, List<String> keywords, Integer count,
            String typeOrganization, int age, List<String> founderMembers) {

        super(label, category, topics, keywords, count);
        this.typeOrganization = typeOrganization;
        this.age = age;
        this.founderMembers = founderMembers;
    }

    public String gettypeOrganization() {
        return typeOrganization;
    }

    public int getAge() {
        return age;
    }

    public List<String> getCounderMembers() {
        return founderMembers;
    }
}
