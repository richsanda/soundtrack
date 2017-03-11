package w.whateva.soundtrack.mapper;

import org.springframework.stereotype.Component;
import w.whateva.soundtrack.api.dto.Entry;
import w.whateva.soundtrack.service.iao.ApiEntry;

/**
 * Created by rich on 3/3/17.
 */
@Component
public class EntryMapper extends Mapper<Entry, ApiEntry> {

    @Override
    public Class<Entry> getRestClass() {
        return Entry.class;
    }
}
