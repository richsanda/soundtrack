package w.whateva.soundtrack.service;

import w.whateva.soundtrack.service.data.ApiEntry;

import java.util.List;

/**
 * Created by rich on 12/17/16.
 */
public interface SoundtrackService {

    ApiEntry createEntry(ApiEntry entry);

    ApiEntry readEntry(String key);

    List<ApiEntry> readEntries();

    List<ApiEntry> readEntries(Integer year);

    List<ApiEntry> readEntries(List<String> personTags);

    ApiEntry readEntry(Integer year, Integer ordinal);

    ApiEntry updateEntry(String key, ApiEntry entry);

    ApiEntry deleteEntry(String key);
}
