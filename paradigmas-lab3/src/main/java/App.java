
import feed.Article;
import feed.FeedParser;
import java.io.IOException;
import java.util.*;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import namedEntities.*;
import utils.*;

public class App {

    public static void main(String[] args) {

        List<FeedsData> feedsDataArray = new ArrayList<>();
        try {
            feedsDataArray = JSONParser.parseJsonFeedsData("target/classes/data/feeds.json");
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

        // Creating the Big DATA
        String fp;

        if (config.getFile() == null) {   //Si no me pasan un file -> uso los feeds de noticias
            fp = "src/main/java/data/bigData.txt";
            FeedParser.articlesToFile(allArticles, fp);
        }
        else if (config.getFile()!= null && (config.getPrintFeed() || (config.getFeedKey()!=null))){ //si me pasan un file y tambien feeds imprimo help
            printHelp(feedsDataArray);
            return;
        }
        else{ //si me pasan un file y no feeds, uso un file como big data
            fp = config.getFile();
        }

        // String fp = "src/main/java/data/bigData.txt";
        // FeedParser.articlesToFile(allArticles, fp);

        if (config.getComputeNamedEntities()) {
            // Configurar Spark
            SparkSession spark = SparkSession.builder()
                    .appName("NamedEntityComputation")
                    .getOrCreate();


            JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
            JavaRDD<String> lines;
            try{
                lines = spark.read().textFile(fp).javaRDD();
            } catch (Exception e){
                System.out.println("Error: " + e);
                spark.stop();
                sc.stop();
                return;
            }
            

            JavaRDD<NamedEntity> namedEntitiesRDD = null;
            
            System.out.println("Computing named entities using " + config.getHeuristicSelected());
            
            
            // If the first heuristic is selected
            if (config.getHeuristicSelected().equals("CapitalizedWordHeuristic")) {
                namedEntitiesRDD = ComputeHeuristic.computeHeuristic(lines,1);
            } else if (config.getHeuristicSelected().equals("AllCapitalizedHeuristic")) {
                namedEntitiesRDD = ComputeHeuristic.computeHeuristic(lines,2);
            } else if (config.getHeuristicSelected().equals("BrandHeuristic")) {
                namedEntitiesRDD = ComputeHeuristic.computeHeuristic(lines,3);
            } else {
                printHelp(feedsDataArray);
                sc.stop();
                spark.stop();
                return;
            }
            
            List<NamedEntity> namedEntitiesList = namedEntitiesRDD.collect();
            Set<NamedEntity> namedEntitiesSet = new HashSet<>();

            for(NamedEntity namedEntity : namedEntitiesList) {
                boolean isOnSet = false;
                for(NamedEntity namedEntitySet : namedEntitiesSet) {
                    if(namedEntitySet.getLabel().equals(namedEntity.getLabel())) {
                        namedEntitySet.increaseCount();
                        isOnSet = true;
                        break;
                    }
                }
                if(!isOnSet) {
                    namedEntitiesSet.add(namedEntity);
                }
            }
    

            // Modularize and error fix
            if (config.getStatsChoice().equals("cat")) {
                PrintStats.category(namedEntitiesSet);
            }
            else if (config.getStatsChoice().equals("topic")) {
                PrintStats.topic(namedEntitiesSet);
            }
            else{
                System.out.println("Invalid stats format");
                printHelp(feedsDataArray);
                sc.stop();
                spark.stop();
                return;
            }
            
            sc.stop();
            spark.stop();

        }
    }

    private static void printHelp(List<FeedsData> feedsDataArray) {
        System.out.println("Usage: /<route>/SPARK_FOLDER/bin/spark-submit --class App --master spark://<user>:7077 /<route>/target/<exe>.jar <-flags> 2>/dev/null ");
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
        System.out.println("                                       heuristic1: CapitalizedWordHeuristic");
        System.out.println("                                       heuristic2: AllCapitalizedHeuristic");
        System.out.println("                                       heuristic3: BrandHeuristic");
        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        System.out.println("                                       cat: Category-wise stats");
        System.out.println("                                       topic: Topic-wise stats");
        System.out.println("  -gf <file>, --get-file <file>:       Use the specified file as the big data");
    }

}
