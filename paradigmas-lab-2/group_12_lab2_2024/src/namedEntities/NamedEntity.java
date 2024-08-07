package namedEntities;

import java.util.List;

//super clase
public class NamedEntity {
    public String label;
    public String category;
    public List<String> topics;
    public List<String> keywords;
    public Integer count;

    public NamedEntity(String label, String category, List<String> topics, List<String> keywords, Integer count) {
        this.label = label;
        this.category = category;
        this.topics = topics;
        this.keywords = keywords;
        this.count = count;
    }

    public String getLabel() {
        return label;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getTopics() {
        return topics;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Integer getCount() {
        return count;
    }

    public void increaseCount() {
        this.count = count + 1;
    }
}