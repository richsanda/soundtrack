package w.whateva.soundtrack.domain;

import com.google.common.collect.Maps;

import java.util.Map;

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

    private static final Map<String, HashTagType> types = Maps.newHashMap();

    static {
        for (HashTagType e : values()) {
            addType(e.getType(), e);
        }
    }

    HashTagType(String type) {
        this.type = type;
    }

    private static void addType(String type, HashTagType e) {
        types.put(type, e);
    }

    public String getType() {
        return type;
    }

    public static HashTagType valueForType(String type) {
        return types.get(type);
    }
}
