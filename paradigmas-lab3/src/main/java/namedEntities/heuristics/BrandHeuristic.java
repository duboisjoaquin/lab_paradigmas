package namedEntities.heuristics;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaRDD;
import java.io.Serializable;

public class BrandHeuristic implements Serializable{

    static public JavaRDD<String> extractCandidates(JavaRDD<String> words) {
        return words.flatMap(word -> {
            List<String> candidates = new ArrayList<>();
            String text = word;
            text = text.replaceAll("[-+.^:,\"]", "");
            text = Normalizer.normalize(text, Normalizer.Form.NFD);
            text = text.replaceAll("\\p{M}", "");

            Pattern pattern = Pattern.compile("[0-9]+(\\s)*([A-Z][a-z]+)(\\s)*[0-9]*(\\s)*([A-Za-z]+)");

            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                candidates.add(matcher.group());
            }
            return candidates.iterator();
        });
    }
}