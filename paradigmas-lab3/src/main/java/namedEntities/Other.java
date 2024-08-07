package namedEntities;

import java.util.List;

//Could be a false positive or a named entity that is not in our dictionary 
public class Other extends NamedEntity {

    public Other(String label, String category, List<String> topics, List<String> keywords, Integer count) {
        super(label, category, topics, keywords, count);
    }

}
