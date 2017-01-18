package w.whateva.soundtrack.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.Person;
import w.whateva.soundtrack.domain.repository.EntryRepository;
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

    @Override
    public ApiEntry createEntry(ApiEntrySpec apiEntrySpec) {
        Entry entry = SoundtrackDataBuilder.buildEntry(apiEntrySpec);
        entryRepository.save(entry);
        return SoundtrackDataBuilder.buildApiEntry(entry);
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
    public List<ApiEntry> readEntries(List<String> personTags) {
        List<Entry> entries = Lists.newArrayList(entryRepository.findByPersonTags(personTags));
        return sortAndConvert(entries);
    }

    @Override
    public ApiEntry readEntry(Integer year, Integer ordinal) {
        return SoundtrackDataBuilder.buildApiEntry(entryRepository.findByYearAndOrdinal(year, ordinal));
    }

    @Override
    public ApiEntry updateEntry(String key, ApiEntrySpec apiEntrySpec) {

        Entry entry = entryRepository.findById(new Long(key));

        if (apiEntrySpec.getStory() != null) {

            String story = apiEntrySpec.getStory().get();

            List<String> personTags = SoundtrackUtil.extractTags(story, TagType.PERSON);

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

            entry.setStory(story);
        }

        if (null != apiEntrySpec.getOrdinal() || null != apiEntrySpec.getYear()) {

            Integer originalYear = entry.getYear();
            Integer originalOrdinal = entry.getOrdinal();

            Integer newYear = null != apiEntrySpec.getYear() ? apiEntrySpec.getYear().orElse(originalYear) : originalYear;
            Integer newOrdinal = null != apiEntrySpec.getOrdinal() ? apiEntrySpec.getOrdinal().orElse(originalOrdinal) : originalOrdinal;

            if (null != originalYear && !newYear.equals(originalYear) || null != originalOrdinal && !newOrdinal.equals(originalOrdinal)) {

                entry.setYear(newYear);
                entry.setOrdinal(newOrdinal);

                List<Entry> newYearEntries = entryRepository.findByYear(newYear);
                newYearEntries.remove(entry);

                Collections.sort(newYearEntries, ENTRY_COMPARATOR);

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
