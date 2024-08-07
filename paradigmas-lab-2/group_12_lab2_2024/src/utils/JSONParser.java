package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONParser {

    static public List<FeedsData> parseJsonFeedsData(String jsonFilePath) throws IOException {
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        List<FeedsData> feedsList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String label = jsonObject.getString("label");
            String url = jsonObject.getString("url");
            String type = jsonObject.getString("type");
            feedsList.add(new FeedsData(label, url, type));
        }
        return feedsList;
    }

    static public List<AuxNamedEntity> parseJsonAuxNamedEntity(String jsonFilePath) throws IOException {
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        List<AuxNamedEntity> feedsList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String label = jsonObject.getString("label");
            String category = jsonObject.getString("Category");
            List<String> topics = new ArrayList<>();
            List<String> keywords = new ArrayList<>();
            JSONArray topicsArray = jsonObject.getJSONArray("Topics");
            for (int j = 0; j < topicsArray.length(); j++) {
                topics.add(topicsArray.getString(j));
            }
            JSONArray keywordsArray = jsonObject.getJSONArray("keywords");
            for (int j = 0; j < keywordsArray.length(); j++) {
                keywords.add(keywordsArray.getString(j));
            }
            feedsList.add(new AuxNamedEntity(label, category, topics, keywords));
        }

        return feedsList;
    }

}
