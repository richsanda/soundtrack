package w.whateva.soundtrack.service;

import w.whateva.soundtrack.service.sao.SAEntry;
import w.whateva.soundtrack.service.sao.SAEntrySpec;

import java.util.List;

/**
 * Created by rich on 12/17/16.
 */
public interface SoundtrackService {

    SAEntry createEntry(SAEntrySpec entry);

    SAEntry readEntry(String key);

    List<SAEntry> readEntries();

    List<SAEntry> readEntries(Integer year);

    List<SAEntry> readEntries(List<String> personTags);

    SAEntry readEntry(Integer year, Integer ordinal);

    SAEntry updateEntry(String key, SAEntrySpec entry);

    SAEntry deleteEntry(String key);
}
