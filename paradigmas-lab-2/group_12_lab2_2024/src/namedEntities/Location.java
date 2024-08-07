package namedEntities;

import java.util.List;

// an especial case of named entity 
public class Location extends NamedEntity {
    private float altitude = 0;
    private float latitude = 0;
    private String placeType = "";

    public Location(String label, String category, List<String> topics,
            List<String> keywords, Integer count, float altitude, float latitude, String placeType) {

        super(label, category, topics, keywords, count);
        this.altitude = altitude;
        this.latitude = latitude;
        this.placeType = placeType;
    }

    public float getAltitude() {
        return altitude;
    }

    public float getLatitud() {
        return latitude;
    }

    public String getPlaceType() {
        return placeType;
    }
}
