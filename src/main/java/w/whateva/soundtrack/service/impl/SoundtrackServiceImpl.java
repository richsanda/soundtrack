package w.whateva.soundtrack.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import w.whateva.soundtrack.domain.Artist;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.HashTag;
import w.whateva.soundtrack.domain.Person;
import w.whateva.soundtrack.domain.repository.EntryRepository;
import w.whateva.soundtrack.domain.repository.HashTagRepository;
import w.whateva.soundtrack.domain.repository.PersonRepository;
import w.whateva.soundtrack.service.MigrationService;
import w.whateva.soundtrack.service.SoundtrackService;
import w.whateva.soundtrack.service.TagType;
import w.whateva.soundtrack.service.iao.ApiArtist;
import w.whateva.soundtrack.service.iao.ApiEntry;
import w.whateva.soundtrack.service.iao.ApiEntrySpec;
import w.whateva.soundtrack.service.util.SoundtrackDataBuilder;
import w.whateva.soundtrack.service.util.SoundtrackUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rich on 12/17/16.
 */
@Component
public class SoundtrackServiceImpl implements SoundtrackService, MigrationService {

    private final EntryRepository entryRepository;

    private final PersonRepository personRepository;

    private final HashTagRepository hashTagRepository;

    @Autowired
    public SoundtrackServiceImpl(EntryRepository entryRepository, PersonRepository personRepository, HashTagRepository hashTagRepository) {
        this.entryRepository = entryRepository;
        this.personRepository = personRepository;
        this.hashTagRepository = hashTagRepository;
    }

    @Override
    public ApiEntry createEntry(ApiEntrySpec apiEntrySpec) {

        Entry entry = new Entry();

        return updateEntry(entry, apiEntrySpec);
    }

    @Override
    public ApiEntry readEntry(String key) {
        return SoundtrackDataBuilder.buildApiEntry(findEntry(key));
    }

    @Override
    public List<ApiEntry> readEntries() {
        List<Entry> entries = Lists.newArrayList(entryRepository.findAll());
        return sortAndConvert(entries);
    }

    @Override
    public List<ApiEntry> readEntries(Integer year) {
        List<Entry> entries = Lists.newArrayList(entryRepository.findByYear(year));
        return sortAndConvert(entries);
    }

    @Override
    public List<ApiEntry> readEntriesRandomized() {
        List<Entry> entries = Lists.newArrayList(entryRepository.findAll());
        return randomizeAndConvert(entries);
    }

    @Override
    public List<ApiEntry> readEntriesRanked(Long limit) {
        List<Entry> entries = Lists.newArrayList(entryRepository.findAll());
        return rankAndConvert(entries, limit);
    }

    @Override
    public List<ApiEntry> readEntriesByPersonTags(List<String> personTags) {
        List<Entry> entries = Lists.newArrayList(entryRepository.findByPersonTags(personTags));
        return sortAndConvert(entries);
    }

    @Override
    public List<ApiEntry> readEntriesByHashTags(List<String> hashTags) {
        List<Entry> entries = Lists.newArrayList(entryRepository.findByHashTags(hashTags));
        return sortAndConvert(entries);
    }

    @Override
    public ApiEntry readEntry(Integer year, Integer ordinal) {
        return SoundtrackDataBuilder.buildApiEntry(entryRepository.findByYearAndOrdinal(year, ordinal));
    }

    @Override
    public List<ApiEntry> readEntries(String artist) {
        List<Entry> entries = entryRepository.findByArtist(artist);
        return sortAndConvert(entries);
    }

    @Override
    public ApiEntry updateEntry(String key, ApiEntrySpec apiEntrySpec) {

        Entry entry = entryRepository.findById(new Long(key));

        return updateEntry(entry, apiEntrySpec);
    }

    @Override
    public void refreshEntry(Entry entry) {

        ApiEntrySpec apiEntrySpec = SoundtrackDataBuilder.buildApiEntrySpec(entry);
        entry.setRankings(null);

        updateEntry(entry, apiEntrySpec);
    }

    private ApiEntry updateEntry(Entry entry, ApiEntrySpec apiEntrySpec) {

        if (apiEntrySpec.getStory() != null) {

            String story = apiEntrySpec.getStory().get();

            Multimap<TagType, String> tags = SoundtrackUtil.extractTags(story);
            Collection<String> personTags = tags.get(TagType.PERSON);
            Collection<String> hashTagTags = tags.get(TagType.HASH);

            if (!CollectionUtils.isEmpty(personTags)) {
                List<Person> persons = personRepository.findByTagIn(personTags);
                Map<String, Person> personTagsToPersons = Maps.newHashMap();
                for (Person person : persons) {
                    personTagsToPersons.put(person.getTag(), person);
                }
                for (String personTag : personTags) {
                    if (!personTagsToPersons.containsKey(personTag)) {
                        Person person = new Person(personTag);
                        personRepository.save(person);
                        personTagsToPersons.put(personTag, person);
                    }
                }

                personRepository.save(personTagsToPersons.values());
                entry.setPersons(Lists.newArrayList(personTagsToPersons.values()));
            } else {
                entry.setPersons(Lists.<Person>newArrayList());
            }

            if (!CollectionUtils.isEmpty(hashTagTags)) {
                List<HashTag> hashTags = hashTagRepository.findByTagIn(hashTagTags);
                Map<String, HashTag> tagsToHashTags = Maps.newHashMap();
                for (HashTag hashTag : hashTags) {
                    tagsToHashTags.put(hashTag.getTag(), hashTag);
                }
                for (String hashTagTag : hashTagTags) {
                    if (!tagsToHashTags.containsKey(hashTagTag)) {
                        HashTag hashTag = new HashTag(hashTagTag);
                        tagsToHashTags.put(hashTagTag, hashTag);
                    }
                }

                hashTagRepository.save(tagsToHashTags.values());
                entry.setHashTags(Lists.newArrayList(tagsToHashTags.values()));
            } else {
                entry.setHashTags(Lists.<HashTag>newArrayList());
            }

            entry.setStory(story);
        }

        if (null != apiEntrySpec.getOrdinal() || null != apiEntrySpec.getYear()) {

            Integer originalYear = entry.getYear();
            Integer originalOrdinal = entry.getOrdinal();

            Integer newYear = null != apiEntrySpec.getYear() ? apiEntrySpec.getYear().orElse(originalYear) : originalYear;
            Integer newOrdinal = null != apiEntrySpec.getOrdinal() ? apiEntrySpec.getOrdinal().orElse(originalOrdinal) : originalOrdinal;

            if (null == newYear || null == newOrdinal || !newYear.equals(originalYear) || !newOrdinal.equals(originalOrdinal)) {

                if (null == newYear) {
                    newYear = entryRepository.findGreatestYear();
                }

                List<Entry> newYearEntries = entryRepository.findByYear(newYear);
                newYearEntries.remove(entry);
                Collections.sort(newYearEntries, ENTRY_COMPARATOR);

                // adjust the ordinal to be in the range of 1 to # of entries for year
                newOrdinal = null == newOrdinal ? newYearEntries.size() + 1
                        : newOrdinal < 1 ? 1
                        : newOrdinal > newYearEntries.size() ? newYearEntries.size() + 1
                        : newOrdinal;

                entry.setYear(newYear);
                entry.setOrdinal(newOrdinal);

                int i = 1;
                for (Entry newYearEntry : newYearEntries) {
                    if (i == newOrdinal) {
                        i++; // skip this i... it's reserved for entry
                    }
                    if (i != newYearEntry.getOrdinal()) {
                        newYearEntry.setOrdinal(i);
                        entryRepository.save(newYearEntry);
                    }
                    i++;
                }

                // go redo the year it was moved from...
                if (null != originalYear && !newYear.equals(originalYear)) {

                    reorderYear(originalYear);
                }
            }
        }

        if (apiEntrySpec.getSpotify() != null) {
            entry.setSpotify(apiEntrySpec.getSpotify().orElse(null));
        }

        if (apiEntrySpec.getTitle() != null) {
            entry.setTitle(apiEntrySpec.getTitle().orElse(null));
        }

        if (apiEntrySpec.getArtist() != null) {
            entry.setArtist(apiEntrySpec.getArtist().orElse(null));
        }

        if (apiEntrySpec.getReleaseDate() != null) {
            entry.setReleaseDate(apiEntrySpec.getReleaseDate().orElse(null));
        }

        entryRepository.save(entry);

        return SoundtrackDataBuilder.buildApiEntry(entry);
    }

    @Override
    public ApiEntry deleteEntry(String key) {
        Entry entry = findEntry(key);
        ApiEntry result = SoundtrackDataBuilder.buildApiEntry(entry);
        entryRepository.delete(new Long(key));
        reorderYear(entry.getYear());
        return result;
    }

    @Override
    public List<ApiArtist> readAllArtists() {

        List<Artist> artists = entryRepository.findAllArtists();
        return artists.stream().map(SoundtrackDataBuilder::buildApiArtist).collect(Collectors.toList());
    }

    private void reorderYear(Integer year) {

        List<Entry> entries = entryRepository.findByYear(year);
        entries.sort(ENTRY_COMPARATOR);

        int i = 1;
        for (Entry entry : entries) {
            if (i != entry.getOrdinal()) {
                entry.setOrdinal(i);
                entryRepository.save(entry);
            }
            i++;
        }
    }

    private Entry findEntry(String key) {
        return entryRepository.findById(new Long(key));
    }

    private List<ApiEntry> sortAndConvert(List<Entry> entries) {

        return entries.stream().sorted(ENTRY_COMPARATOR).map(SoundtrackDataBuilder::buildApiEntry).collect(Collectors.toList());
    }

    private List<ApiEntry> randomizeAndConvert(List<Entry> entries) {

        Collections.shuffle(entries);
        List<ApiEntry> result = Lists.newArrayList();
        for (Entry entry : entries) {
            result.add(SoundtrackDataBuilder.buildApiEntry(entry));
        }
        return result;
    }

    private List<ApiEntry> rankAndConvert(List<Entry> entries, Long limit) {

        return entries.stream()
                .sorted(ENTRY_RANKER)
                .map(SoundtrackDataBuilder::buildApiEntry)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private static final Comparator<Entry> ENTRY_COMPARATOR = (entry1, entry2) -> new CompareToBuilder()
            .append(entry1.getYear(), entry2.getYear())
            .append(entry1.getOrdinal(), entry2.getOrdinal())
            .toComparison();

    private static final Comparator<Entry> ENTRY_RANKER = (entry1, entry2) -> new CompareToBuilder()
            .append(entry2.getScore(), entry1.getScore())
            //.append(getScore(entry1.getRanking(FAVORITE)), entry2.getRanking(FAVORITE))
            .toComparison();
}
