package w.whateva.soundtrack.api.dto;

import java.util.List;
import java.util.Optional;

/**
 * Created by rich on 3/8/17.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class RankedListSpec {

    private Optional<String> type;
    private Optional<List<String>> entries;

    public Optional<String> getType() {
        return type;
    }

    public void setType(String type) {
        this.type = Optional.ofNullable(type);
    }


    public Optional<List<String>> getEntries() {
        return entries;
    }

    public void setEntries(List<String> entries) {
        this.entries = Optional.ofNullable(entries);
    }
}
