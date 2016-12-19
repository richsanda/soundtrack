package w.whateva.soundtrack.service.util;

import org.springframework.beans.BeanUtils;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.service.data.ApiEntry;

/**
 * Created by rich on 12/18/16.
 */
public class SoundtrackDataBuilder {

    public static ApiEntry buildApiEntry(Entry entry) {
        ApiEntry result = new ApiEntry();
        BeanUtils.copyProperties(entry, result);
        return result;
    }

    public static Entry buildEntry(ApiEntry apiEntry) {
        Entry entry = new Entry();
        BeanUtils.copyProperties(apiEntry, entry);
        return entry;
    }
}
