package w.whateva.soundtrack.service;

import w.whateva.soundtrack.service.iao.IAEntry;
import w.whateva.soundtrack.service.iao.IAEntrySpec;

import java.util.List;

/**
 * Created by rich on 12/17/16.
 */
public interface SoundtrackService {

    IAEntry createEntry(IAEntrySpec entry);

    IAEntry readEntry(String key);

    List<IAEntry> readEntries();

    List<IAEntry> readEntries(Integer year);

    List<IAEntry> readEntries(List<String> personTags);

    IAEntry readEntry(Integer year, Integer ordinal);

    IAEntry updateEntry(String key, IAEntrySpec entry);

    IAEntry deleteEntry(String key);
}
