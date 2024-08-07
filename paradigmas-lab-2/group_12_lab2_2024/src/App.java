
import feed.Article;
import feed.FeedParser;
import java.io.IOException;
import java.util.*;
import namedEntities.*;
import utils.*;

public class App {

    public static void main(String[] args) {

        List<FeedsData> feedsDataArray = new ArrayList<>();
        try {
            feedsDataArray = JSONParser.parseJsonFeedsData("src/data/feeds.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Check if the user has requested help
        for (String arg : args) {
            if (arg.equals("-h") || arg.equals("--help")) {
                printHelp(feedsDataArray); // Assuming feedsDataArray is accessible here or passed as an argument
                return; // Exit after displaying help
            }
        }

        UserInterface ui = new UserInterface();
        Config config = ui.handleInput(args);

        // If there is a key already selected, we only want to work in the selected one
        if (config.getFeedKey() != null) {
            boolean found = false;
            for (FeedsData feedData : feedsDataArray) {
                if (feedData.getLabel().equals(config.getFeedKey())) {
                    feedsDataArray.clear();
                    feedsDataArray.add(feedData);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Invalid feed key");
                return;
            }
        }

        run(config, feedsDataArray);
    }

    private static void run(Config config, List<FeedsData> feedsDataArray) {

        if (feedsDataArray == null || feedsDataArray.size() == 0) {
            System.out.println("No feeds data found");
            return;
        }

        // We create a list of Articles with our parser of XML
        List<Article> allArticles = new ArrayList<>();
        for (FeedsData feedData : feedsDataArray) {
            allArticles = FeedParser.parseXML(feedData.getUrl()); // Fetch and parse the feed
        }

        if (config.getPrintFeed()) {
            System.out.println("Printing feed(s) ");
            for (Article article : allArticles) {
                article.print();
            }
        }

        if (config.getComputeNamedEntities()) {

            // maybe set insted of List
            Set<NamedEntity> namedEntities = new HashSet<>();

            System.out.println("Computing named entities using " + config.getHeuristicSelected());

            // If the first heuristic is selected
            if (config.getHeuristicSelected().equals("CapitalizedWordHeuristic")) {
                namedEntities = ComputeHeuristic.computeHeuristic(allArticles,1);
            } else if (config.getHeuristicSelected().equals("AllCapitalizedHeuristic")) {
                namedEntities = ComputeHeuristic.computeHeuristic(allArticles,2);
            } else if (config.getHeuristicSelected().equals("BrandHeuristic")) {
                namedEntities = ComputeHeuristic.computeHeuristic(allArticles,3);
            } else {
                printHelp(feedsDataArray);
                return;
            }

            System.out.println("\nStats: ");
            // System.out.println("-".repeat(80));

            if (config.getStatsChoice().equals("cat")) {
                List<String> cateogories = new ArrayList<>();
                // harcodeo
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

            } else if (config.getStatsChoice().equals("topic")) {

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
    }

    private static void printHelp(List<FeedsData> feedsDataArray) {
        System.out.println("Usage: make run ARGS=\"[OPTION]\"");
        System.out.println("Options:");
        System.out.println("  -h, --help: Show this help message and exit");
        System.out.println("  -f, --feed <feedKey>:                Fetch and process the feed with");
        System.out.println("                                       the specified key");
        System.out.println("                                       Available feed keys are: ");
        for (FeedsData feedData : feedsDataArray) {
            System.out.println("                                       " + feedData.getLabel());
        }
        System.out.println("  -ne, --named-entity <heuristicName>: Use the specified heuristic to extract");
        System.out.println("                                       named entities");
        System.out.println("                                       Available heuristic names are: ");
        System.out.println("                                       <name>: <description>");
        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        System.out.println("                                       cat: Category-wise stats");
        System.out.println("                                       topic: Topic-wise stats");
        System.out.println("  -hr, --heuristic-reference <heuristicName>: Use the specified heuristic as a reference");
        System.out.println("                                       for the computation of named entities");
        System.out.println("                                       Available keys are: ");
        System.out.println("                                       heuristic0: Don't use any heuristic as reference");
        System.out.println("                                       heuristic1: CapitalizedWordHeuristic");
        System.out.println("                                       heuristic2: AllCapitalizedHeuristic");
        System.out.println("                                       heuristic3: BrandHeuristic");
    }

}
