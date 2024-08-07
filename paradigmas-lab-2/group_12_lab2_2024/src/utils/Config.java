package utils;

public class Config {
    private boolean printFeed = false;
    private boolean computeNamedEntities = false;
    private String heuristicSelected;
    private String feedKey;
    private String statsChoice;

    public Config(boolean printFeed, boolean computeNamedEntities,
            String feedKey, String statsChoice, String heuristicSelected) {
        this.printFeed = printFeed;
        this.computeNamedEntities = computeNamedEntities;
        this.feedKey = feedKey;
        this.statsChoice = statsChoice;
        this.heuristicSelected = heuristicSelected;
    }

    public boolean getPrintFeed() {
        return printFeed;
    }

    public boolean getComputeNamedEntities() {
        return computeNamedEntities;
    }

    public String getFeedKey() {
        return feedKey;
    }

    public String getStatsChoice() {
        return statsChoice; // Getter for the choice in the stats
    }

    public String getHeuristicSelected() {
        return heuristicSelected;
    }
}