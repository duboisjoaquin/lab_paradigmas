package utils;

import feed.*;
import java.util.*;
import namedEntities.*;
import namedEntities.heuristics.AllCapitalizedHeuristic;
import namedEntities.heuristics.BrandHeuristic;
import namedEntities.heuristics.CapitalizedWordHeuristic;

public class ComputeHeuristic {

    static public Set<NamedEntity> computeHeuristic(List<Article> allArticles, int selectedHeuristic) {
        // Creation of our hashmap, mostly to search the candidates in our dictionary
        Map<String, AuxNamedEntity> keywordToLabel = HashCategoryData.main();
        Set<NamedEntity> namedEntities = new HashSet<>();
        List<String> otherList = new ArrayList<>();
        boolean isOnSet = false;
        otherList.add("OTHER");

        // For every article we extract candidates to named entity from the title and
        // the description
        for (Article article : allArticles) {
            List<String> candidates = new ArrayList<>();
            if(selectedHeuristic == 1){
                candidates = CapitalizedWordHeuristic.extractCandidates(article.getTitle());
                candidates.addAll(CapitalizedWordHeuristic.extractCandidates(article.getDescription()));
            }
            else if (selectedHeuristic == 2){
                candidates = AllCapitalizedHeuristic.extractCandidates(article.getTitle());
                candidates.addAll(AllCapitalizedHeuristic.extractCandidates(article.getDescription()));
            }
            else if (selectedHeuristic == 3){
                candidates = BrandHeuristic.extractCandidates(article.getTitle());
                candidates.addAll(BrandHeuristic.extractCandidates(article.getDescription()));
            }
            // for every candidate in the previus list
            for (String candidate : candidates) {
                isOnSet = false;
                // if candidate is a key, the value is going to be a CategoryData Object
                if (keywordToLabel.containsKey(candidate)) {
                    AuxNamedEntity aux = keywordToLabel.get(candidate);

                    // with the getter function in aux class we match the expected category
                    if (aux.getCategory().equals("LOCATION")) {
                        for (NamedEntity currEntity : namedEntities) {
                            if (currEntity.getLabel().equals(aux.getLabel())) {
                                currEntity.increaseCount();
                                isOnSet = true;
                            }
                        }
                        if (!isOnSet) {
                            Location loc = new Location(aux.getLabel(), aux.getCategory(), aux.getTopics(),
                                    aux.getKeywords(), 1, 0, 0, "placeType");
                            namedEntities.add(loc);
                        }

                    } else if (aux.getCategory().equals("PERSON")) {
                        for (NamedEntity currEntity : namedEntities) {
                            if (currEntity.getLabel().equals(aux.getLabel())) {
                                currEntity.increaseCount();
                                isOnSet = true;
                            }
                        }
                        if (!isOnSet) {
                            Person per = new Person(aux.getLabel(), aux.getCategory(), aux.getTopics(),
                                    aux.getKeywords(), 1, 0, 0, "profession");
                            namedEntities.add(per);
                        }
                    } else if (aux.getCategory().equals("ORGANIZATION")) {
                        for (NamedEntity currEntity : namedEntities) {
                            if (currEntity.getLabel().equals(aux.getLabel())) {
                                currEntity.increaseCount();
                                isOnSet = true;
                            }
                        }
                        if (!isOnSet) {
                            Organization org = new Organization(aux.getLabel(), aux.getCategory(), aux.getTopics(),
                                    aux.getKeywords(), 1, "typeOrganization", 0, null);
                            namedEntities.add(org);
                        }
                    }

                } else {
                    for (NamedEntity currEntity : namedEntities) {
                        if (currEntity.getLabel().equals(candidate)) {
                            currEntity.increaseCount();
                            isOnSet = true;
                        }
                    }
                    if (!isOnSet) {
                        Other other = new Other(candidate, "OTHER", otherList, otherList, 1);
                        namedEntities.add(other);
                    }
                }
            }
        }

        return namedEntities;
    }

}