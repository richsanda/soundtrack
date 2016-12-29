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
import w.whateva.soundtrack.service.iao.IAEntry;
import w.whateva.soundtrack.service.iao.IAEntrySpec;
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
    public IAEntry createEntry(IAEntrySpec innerEntrySpec) {
        Entry entry = SoundtrackDataBuilder.buildEntry(innerEntrySpec);
        entryRepository.save(entry);
        return SoundtrackDataBuilder.buildIAEntry(entry);
    }

    @Override
    public IAEntry readEntry(String key) {
        return SoundtrackDataBuilder.buildIAEntry(findEntry(key));
    }

    @Override
    public List<IAEntry> readEntries() {
        List<Entry> entries = Lists.newArrayList(entryRepository.findAll());
        return sortAndConvert(entries);
    }

    @Override
    public List<IAEntry> readEntries(Integer year) {
        List<Entry> entries = Lists.newArrayList(entryRepository.findByYear(year));
        return sortAndConvert(entries);
    }

    @Override
    public List<IAEntry> readEntries(List<String> personTags) {
        List<Entry> entries = Lists.newArrayList(entryRepository.findByPersonTags(personTags));
        return sortAndConvert(entries);
    }

    @Override
    public IAEntry readEntry(Integer year, Integer ordinal) {
        return SoundtrackDataBuilder.buildIAEntry(entryRepository.findByYearAndOrdinal(year, ordinal));
    }

    @Override
    public IAEntry updateEntry(String key, IAEntrySpec innerEntrySpec) {

        Entry entry = entryRepository.findById(new Long(key));

        if (innerEntrySpec.getStory() != null) {

            String story = innerEntrySpec.getStory().get();

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
            }

            entry.setStory(story);
        }

        if (innerEntrySpec.getSpotify() != null) {
            entry.setSpotify(innerEntrySpec.getSpotify().orElse(null));
        }

        entryRepository.save(entry);

        return SoundtrackDataBuilder.buildIAEntry(entry);
    }

    @Override
    public IAEntry deleteEntry(String key) {
        IAEntry result = SoundtrackDataBuilder.buildIAEntry(findEntry(key));
        entryRepository.delete(new Long(key));
        return result;
    }

    private Entry findEntry(String key) {
        return entryRepository.findById(new Long(key));
    }

    private List<IAEntry> sortAndConvert(List<Entry> entries) {
        Collections.sort(entries, ENTRY_COMPARATOR);
        List<IAEntry> result = Lists.newArrayList();
        for (Entry entry : entries) {
            result.add(SoundtrackDataBuilder.buildIAEntry(entry));
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
