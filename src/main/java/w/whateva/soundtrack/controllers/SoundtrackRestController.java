package w.whateva.soundtrack.controllers;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.repositories.EntryRepository;
import w.whateva.soundtrack.jobs.load.SoundtrackLoadJobRunner;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@RestController
public class SoundtrackRestController {

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    SoundtrackLoadJobRunner soundtrackLoadJobRunner;

    @RequestMapping(value = "/entry/{key}", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Entry retrieveEntry(@PathVariable("key") String key) {
        Entry entry = entryRepository.findById(new Long(key));
        return entry;
    }

    @Transactional(value = "transactionManager")
    @RequestMapping(value = "/entry/{key}", method= RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Entry updateEntry(@PathVariable("key") String key, @RequestBody Entry update) {
        Entry entry = entryRepository.findById(new Long(key));
        entry.setStory(update.getStory());
        entryRepository.save(entry);
        return entry;
    }

    @RequestMapping(value = "/entries", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Entry> giveMeTheEntries() {
        List<Entry> entries = Lists.newArrayList(entryRepository.findAll());
        Collections.sort(entries, ENTRY_COMPARATOR);
        return entries;
    }

    @RequestMapping(value = "/soundtrack", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Entry> giveMeTheSoundtrack() {
        List<Entry> entries = Lists.newArrayList(entryRepository.findAll());
        Collections.sort(entries, ENTRY_COMPARATOR);
        return entries;
    }

    @RequestMapping(value = "/entries/{year}", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Entry> giveMeAYear(@PathVariable("year") Integer year) {
        List<Entry> entries = entryRepository.findByYear(year);
        Collections.sort(entries, ENTRY_COMPARATOR);
        return entries;
    }

    @RequestMapping(value = "/entry/{year}/{ordinal}", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Entry giveMeAnEntryByYearAndOrdinal(@PathVariable("year") Integer year, @PathVariable("ordinal") Integer ordinal) {
        Entry entry = entryRepository.findByYearAndOrdinal(year, ordinal);
        return entry;
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
