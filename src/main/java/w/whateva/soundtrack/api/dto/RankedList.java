package w.whateva.soundtrack.api.dto;

import java.util.List;

/**
 * Created by rich on 3/8/17.
 */
public class RankedList {

    private String type;
    private List<Entry> entries;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}
