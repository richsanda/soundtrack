package w.whateva.soundtrack.service;

import w.whateva.soundtrack.service.iao.ApiArtist;
import w.whateva.soundtrack.service.iao.ApiEntry;
import w.whateva.soundtrack.service.iao.ApiEntrySpec;

import java.util.List;

/**
 * Created by rich on 12/17/16.
 */
public interface SoundtrackService {

    ApiEntry createEntry(ApiEntrySpec apiEntrySpec);

    ApiEntry readEntry(String key);

    List<ApiEntry> readEntries();

    List<ApiEntry> readEntries(Integer year);

    List<ApiEntry> readEntriesRandomized();

    List<ApiEntry> readEntriesRanked();

    List<ApiEntry> readEntriesByPersonTags(List<String> personTags);

    List<ApiEntry> readEntriesByHashTags(List<String> hashTags);

    ApiEntry readEntry(Integer year, Integer ordinal);

    List<ApiEntry> readEntries(String artist);

    ApiEntry updateEntry(String key, ApiEntrySpec apiEntrySpec);

    ApiEntry deleteEntry(String key);

    List<ApiArtist> readAllArtists();
}
