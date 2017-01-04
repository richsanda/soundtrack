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
import w.whateva.soundtrack.service.sao.SAEntry;
import w.whateva.soundtrack.service.sao.SAEntrySpec;
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
    public SAEntry createEntry(SAEntrySpec saEntrySpec) {
        Entry entry = SoundtrackDataBuilder.buildEntry(saEntrySpec);
        entryRepository.save(entry);
        return SoundtrackDataBuilder.buildSAEntry(entry);
    }

    @Override
    public SAEntry readEntry(String key) {
        return SoundtrackDataBuilder.buildSAEntry(findEntry(key));
    }

    @Override
    public List<SAEntry> readEntries() {
        List<Entry> entries = Lists.newArrayList(entryRepository.findAll());
        return sortAndConvert(entries);
    }

    @Override
    public List<SAEntry> readEntries(Integer year) {
        List<Entry> entries = Lists.newArrayList(entryRepository.findByYear(year));
        return sortAndConvert(entries);
    }

    @Override
    public List<SAEntry> readEntries(List<String> personTags) {
        List<Entry> entries = Lists.newArrayList(entryRepository.findByPersonTags(personTags));
        return sortAndConvert(entries);
    }

    @Override
    public SAEntry readEntry(Integer year, Integer ordinal) {
        return SoundtrackDataBuilder.buildSAEntry(entryRepository.findByYearAndOrdinal(year, ordinal));
    }

    @Override
    public SAEntry updateEntry(String key, SAEntrySpec saEntrySpec) {

        Entry entry = entryRepository.findById(new Long(key));

        if (saEntrySpec.getStory() != null) {

            String story = saEntrySpec.getStory().get();

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
                entry.setPersons(Lists.newArrayList());
            }

            entry.setStory(story);
        }

        if (saEntrySpec.getSpotify() != null) {
            entry.setSpotify(saEntrySpec.getSpotify().orElse(null));
        }

        entryRepository.save(entry);

        return SoundtrackDataBuilder.buildSAEntry(entry);
    }

    @Override
    public SAEntry deleteEntry(String key) {
        SAEntry result = SoundtrackDataBuilder.buildSAEntry(findEntry(key));
        entryRepository.delete(new Long(key));
        return result;
    }

    private Entry findEntry(String key) {
        return entryRepository.findById(new Long(key));
    }

    private List<SAEntry> sortAndConvert(List<Entry> entries) {
        Collections.sort(entries, ENTRY_COMPARATOR);
        List<SAEntry> result = Lists.newArrayList();
        for (Entry entry : entries) {
            result.add(SoundtrackDataBuilder.buildSAEntry(entry));
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
