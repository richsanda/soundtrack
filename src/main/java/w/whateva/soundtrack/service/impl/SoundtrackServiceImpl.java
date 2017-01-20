package w.whateva.soundtrack.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.HashTag;
import w.whateva.soundtrack.domain.Person;
import w.whateva.soundtrack.domain.repository.EntryRepository;
import w.whateva.soundtrack.domain.repository.HashTagRepository;
import w.whateva.soundtrack.domain.repository.PersonRepository;
import w.whateva.soundtrack.service.SoundtrackService;
import w.whateva.soundtrack.service.TagType;
import w.whateva.soundtrack.service.sao.ApiEntry;
import w.whateva.soundtrack.service.sao.ApiEntrySpec;
import w.whateva.soundtrack.service.util.SoundtrackDataBuilder;
import w.whateva.soundtrack.service.util.SoundtrackUtil;

import java.util.*;

/**
 * Created by rich on 12/17/16.
 */
@Component
public class SoundtrackServiceImpl implements SoundtrackService {

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    HashTagRepository hashTagRepository;

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
    public ApiEntry updateEntry(String key, ApiEntrySpec apiEntrySpec) {

        Entry entry = entryRepository.findById(new Long(key));

        return updateEntry(entry, apiEntrySpec);
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

            if (!newYear.equals(originalYear) || !newOrdinal.equals(originalOrdinal)) {

                List<Entry> newYearEntries = entryRepository.findByYear(newYear);
                newYearEntries.remove(entry);
                Collections.sort(newYearEntries, ENTRY_COMPARATOR);

                // adjust the ordinal to be in the range of 1 to # of entries for year
                newOrdinal = newOrdinal < 1 ? 1 : newOrdinal > newYearEntries.size() ? newYearEntries.size() + 1 : newOrdinal;

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
                if (!newYear.equals(originalYear)) {

                    List<Entry> originalYearEntries = entryRepository.findByYear(originalYear);
                    Collections.sort(originalYearEntries, ENTRY_COMPARATOR);

                    i = 1;
                    for (Entry originalYearEntry : originalYearEntries) {
                        if (i != originalYearEntry.getOrdinal()) {
                            originalYearEntry.setOrdinal(i);
                            entryRepository.save(originalYearEntry);
                        }
                        i++;
                    }
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

        entryRepository.save(entry);

        return SoundtrackDataBuilder.buildApiEntry(entry);
    }

    @Override
    public ApiEntry deleteEntry(String key) {
        ApiEntry result = SoundtrackDataBuilder.buildApiEntry(findEntry(key));
        entryRepository.delete(new Long(key));
        return result;
    }

    private Entry findEntry(String key) {
        return entryRepository.findById(new Long(key));
    }

    private List<ApiEntry> sortAndConvert(List<Entry> entries) {

        Collections.sort(entries, ENTRY_COMPARATOR);
        List<ApiEntry> result = Lists.newArrayList();
        for (Entry entry : entries) {
            result.add(SoundtrackDataBuilder.buildApiEntry(entry));
        }
        return result;
    }

    private static final Comparator<Entry> ENTRY_COMPARATOR = new Comparator<Entry>() {

        @Override
        public int compare(Entry entry1, Entry entry2) {
            return new CompareToBuilder()
                    .append(entry1.getYear(), entry2.getYear())
                    .append(entry1.getOrdinal(), entry2.getOrdinal())
                    .toComparison();
        }
    };
}
