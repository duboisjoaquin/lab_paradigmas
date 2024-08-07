package feed;

public class Article {
    private String title;
    private String description;
    private String pubDate;
    private String link;

    public Article(String title, String description, String pubDate, String link) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
    }

    public void print() {
        System.out.println("\n");
        System.out.println("Noticia:\n");
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Publication Date: " + pubDate);
        System.out.println("Link: " + link);
        System.out.println("\n");
        System.out.println("*".repeat(80));
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}