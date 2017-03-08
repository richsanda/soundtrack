package w.whateva.soundtrack.api.dto;

import java.util.List;

/**
 * Created by rich on 3/8/17.
 */
public class RankedList {

    private String name;
    private List<Entry> entries;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}
