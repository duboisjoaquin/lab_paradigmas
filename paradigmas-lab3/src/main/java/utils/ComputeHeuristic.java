package utils;
import java.util.*;

import org.apache.spark.api.java.JavaRDD;

import namedEntities.*;
import namedEntities.heuristics.AllCapitalizedHeuristic;
import namedEntities.heuristics.BrandHeuristic;
import namedEntities.heuristics.CapitalizedWordHeuristic;

import java.io.Serializable;

public class ComputeHeuristic implements Serializable {

    static public JavaRDD<NamedEntity> computeHeuristic(JavaRDD<String> lines, int selectedHeuristic){
        // Creation of our hashmap, mostly to search the candidates in our dictionary
        Map<String, AuxNamedEntity> keywordToLabel = HashCategoryData.main();
        List<String> otherList = new ArrayList<>();
        otherList.add("OTHER");

        // For every article we extract candidates to named entity from the title and
        // the description

        JavaRDD<String> candidates = null;

        if(selectedHeuristic == 1){
            candidates = CapitalizedWordHeuristic.extractCandidates(lines);
        }
        else if (selectedHeuristic == 2){
            candidates = AllCapitalizedHeuristic.extractCandidates(lines);
        }
        else if (selectedHeuristic == 3){
            candidates = BrandHeuristic.extractCandidates(lines);
        }



        JavaRDD<NamedEntity> entitiesRDD = candidates.map(candidate ->{
            if(keywordToLabel.containsKey(candidate)){
                AuxNamedEntity aux = keywordToLabel.get(candidate);
                if(aux.getCategory().equals("LOCATION")){
                    return new Location(aux.getLabel(), aux.getCategory(), aux.getTopics(), aux.getKeywords(), 1, 0, 0, "placeType");
                }
                else if(aux.getCategory().equals("PERSON")){
                    return new Person(aux.getLabel(), aux.getCategory(), aux.getTopics(), aux.getKeywords(), 1, 0, 0, "profession");
                }
                else if(aux.getCategory().equals("ORGANIZATION")){
                    return new Organization(aux.getLabel(), aux.getCategory(), aux.getTopics(), aux.getKeywords(), 1, "typeOrganization", 0, null);
                }
                else{
                    return new Other(candidate, "OTHER", otherList, null, 1);
                 }
            } 
             else {
                return new Other(candidate, "OTHER", otherList, null, 1);
             }
        });


        return entitiesRDD;
    }

}