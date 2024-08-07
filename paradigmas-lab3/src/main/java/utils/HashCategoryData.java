package utils;

import java.util.*;

import java.io.Serializable;

// O(m) (m = numero de keywords)
public class HashCategoryData implements Serializable{

    public static Map<String, AuxNamedEntity> main() {

        // Function with the path of our dictionary (parser of json)
        Map<String, AuxNamedEntity> keywordToLabel = new HashMap<>();
        List<AuxNamedEntity> auxList = new ArrayList<>();

        try {
            auxList = JSONParser.parseJsonAuxNamedEntity("target/classes/data/dictionary.json");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        for (AuxNamedEntity aux : auxList) {
            for (String keyword : aux.getKeywords()) {
                keywordToLabel.put(keyword, aux);
            }
        }
        return keywordToLabel;
    }
}