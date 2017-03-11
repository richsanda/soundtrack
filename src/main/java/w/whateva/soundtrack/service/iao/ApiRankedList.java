package w.whateva.soundtrack.service.iao;

import java.util.List;

/**
 * Created by rich on 3/8/17.
 */
public class ApiRankedList {

    private String key;
    private String type;
    private List<ApiEntry> entries;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ApiEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ApiEntry> entries) {
        this.entries = entries;
    }
}
