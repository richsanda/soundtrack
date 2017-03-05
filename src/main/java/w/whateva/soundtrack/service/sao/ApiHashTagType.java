package w.whateva.soundtrack.service.sao;

/**
 * Created by rich on 2/24/17.
 */
public enum ApiHashTagType {

    MUSIC("music"),
    TIMELINE("timeline"),
    MEDIA("media"),
    FORMAT("format"),
    PLAYER("player"),
    GENERAL(null);

    private String type;

    ApiHashTagType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}