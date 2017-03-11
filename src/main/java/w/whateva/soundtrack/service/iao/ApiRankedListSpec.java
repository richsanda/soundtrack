package w.whateva.soundtrack.service.iao;

import java.util.List;
import java.util.Optional;

/**
 * Created by rich on 3/8/17.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ApiRankedListSpec {

    private Optional<String> type;
    private Optional<List<String>> entries;

    public Optional<String> getType() {
        return type;
    }

    public void setType(Optional<String> type) {
        this.type = type;
    }

    public Optional<List<String>> getEntries() {
        return entries;
    }

    public void setEntries(Optional<List<String>> entries) {
        this.entries = entries;
    }
}
