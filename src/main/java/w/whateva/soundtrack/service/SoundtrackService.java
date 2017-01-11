package w.whateva.soundtrack.service;

import w.whateva.soundtrack.service.sao.ApiEntry;
import w.whateva.soundtrack.service.sao.ApiEntrySpec;

import java.util.List;

/**
 * Created by rich on 12/17/16.
 */
public interface SoundtrackService {

    ApiEntry createEntry(ApiEntrySpec apiEntrySpec);

    ApiEntry readEntry(String key);

    List<ApiEntry> readEntries();

    List<ApiEntry> readEntries(Integer year);

    List<ApiEntry> readEntries(List<String> personTags);

    ApiEntry readEntry(Integer year, Integer ordinal);

    ApiEntry updateEntry(String key, ApiEntrySpec apiEntrySpec);

    ApiEntry deleteEntry(String key);
}
