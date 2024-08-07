package utils;

import java.util.*;
import namedEntities.NamedEntity;

public class PrintStats {
    public static void category(Set<NamedEntity> namedEntities){
        System.out.println("\nStats: ");
        List<String> cateogories = new ArrayList<>();
    
        cateogories.add("PERSON");
        cateogories.add("ORGANIZATION");
        cateogories.add("LOCATION");

        cateogories.add("OTHER");

        Boolean flag = true;
        for (String curCat : cateogories) {
            if (flag) {
                System.out.println("* " + curCat + ": ");
                flag = false;
            }

            for (NamedEntity curEntity : namedEntities) {

                if (curEntity.getCategory().equals(curCat)) {
                    System.out.println(" " + curEntity.label + " (" + curEntity.getCount() + ") ");
                }
            }
            flag = true;
        }
    }

    public static void topic(Set<NamedEntity> namedEntities){
        System.out.println("\nStats: ");

        List<String> topics = new ArrayList<>();
        // Listado de topicos
        topics.add("SPORTS");
        topics.add("POLITICS");
        topics.add("ECONOMICS");
        topics.add("BUSINESS");
        topics.add("TECHNOLOGY");
        topics.add("ENTERTAINMENT");
        topics.add("OTHER");

        Boolean flag = true;
        for (String curTopic : topics) {
            if (flag) {
                System.out.println("* " + curTopic + ": ");
                flag = false;
            }

            for (NamedEntity curEntity : namedEntities) {

                for (String curEntityTopic : curEntity.getTopics()) {
                    if (curEntityTopic.equals(curTopic)) {
                        System.out.println(" " + curEntity.label + " (" + curEntity.getCount() + ") ");
                    }
                }

            }

            flag = true;
        }
    }
}
