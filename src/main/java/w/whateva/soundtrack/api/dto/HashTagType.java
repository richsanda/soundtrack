package w.whateva.soundtrack.api.dto;

/**
 * Created by rich on 2/24/17.
 */
public enum HashTagType {

    MUSIC("music"),
    MEDIA("media"),
    FORMAT("format"),
    PLAYER("player"),
    TIMELINE("timeline"),
    GENERAL(null);

    private String type;

    HashTagType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
