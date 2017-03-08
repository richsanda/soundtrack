package w.whateva.soundtrack.service.sao;

import java.util.List;
import java.util.Optional;

/**
 * Created by rich on 3/8/17.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ApiRankedListSpec {

    private Optional<String> name;
    private Optional<List<String>> entries;

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<List<String>> getEntries() {
        return entries;
    }

    public void setEntries(Optional<List<String>> entries) {
        this.entries = entries;
    }
}
