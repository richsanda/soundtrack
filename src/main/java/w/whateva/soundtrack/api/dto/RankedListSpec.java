package w.whateva.soundtrack.api.dto;

import java.util.List;
import java.util.Optional;

/**
 * Created by rich on 3/8/17.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class RankedListSpec {

    private Optional<String> name;
    private Optional<List<String>> entries;

    public Optional<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }


    public Optional<List<String>> getEntries() {
        return entries;
    }

    public void setEntries(List<String> entries) {
        this.entries = Optional.ofNullable(entries);
    }
}
