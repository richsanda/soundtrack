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
import w.whateva.soundtrack.service.data.ApiEntry;
import w.whateva.soundtrack.service.util.SoundtrackDataBuilder;
import w.whateva.soundtrack.service.util.SoundtrackUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    public ApiEntry createEntry(ApiEntry apiEntry) {
        Entry entry = SoundtrackDataBuilder.buildEntry(apiEntry);
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
    public ApiEntry updateEntry(String key, ApiEntry apiEntry) {

        Entry entry = entryRepository.findById(new Long(key));

        List<String> personTags = SoundtrackUtil.extractTags(apiEntry.getStory(), TagType.PERSON);

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
        }

        entry.setStory(apiEntry.getStory());
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
