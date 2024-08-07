package feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FeedParser {

    public static List<Article> parseXML(String xmlData) {
        // Initialize an empty list to hold the parsed articles
        List<Article> articles = new ArrayList<>();
        try {
            // Instance
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(xmlData.trim());
            // Normalize the document to combine adjacent text nodes
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("item"); // Get all the item nodes from the document

            // Iterate over each item node
            for (int temp = 0; temp < nList.getLength(); temp++) {
                // Cast the current node to an Element to access its children
                Element element = (Element) nList.item(temp);

                // Extract the title, description, publication date, and link from the current
                // "item"
                String title = element.getElementsByTagName("title").item(0).getTextContent();
                String description = element.getElementsByTagName("description").item(0).getTextContent();
                String pubDate = element.getElementsByTagName("pubDate").item(0).getTextContent();
                String link = element.getElementsByTagName("link").item(0).getTextContent();

                // Create a new Article object with the extracted information
                Article article = new Article(title, description, pubDate, link);
                // Add the new Article object to the list of articles
                articles.add(article);
            }
        } catch (Exception e) {
            // Print the stack trace in case of an exception
            e.printStackTrace();
        }
        // Return the list of Article objects
        return articles;
    }

    public static String fetchFeed(String feedURL) throws MalformedURLException, IOException, Exception {

        URL url = new URL(feedURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setRequestProperty("User-agent", "Grupo-12-2024");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new Exception("HTTP error code: " + status);
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            return content.toString();
        }
    }
}