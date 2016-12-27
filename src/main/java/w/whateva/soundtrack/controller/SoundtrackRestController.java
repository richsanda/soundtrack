package w.whateva.soundtrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import w.whateva.soundtrack.api.dto.Entry;
import w.whateva.soundtrack.api.dto.EntrySpec;
import w.whateva.soundtrack.api.dto.Person;
import w.whateva.soundtrack.service.PersonService;
import w.whateva.soundtrack.service.SoundtrackService;

import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@RestController
public class SoundtrackRestController implements SoundtrackRestService {

    @Autowired
    SoundtrackService soundtrackService;

    @Autowired
    PersonService personService;

    private static final SoundtrackRestMapper.EntryMapper entryMapper = new SoundtrackRestMapper.EntryMapper();
    private static final SoundtrackRestMapper.EntrySpecMapper entrySpecMapper = new SoundtrackRestMapper.EntrySpecMapper();
    private static final SoundtrackRestMapper.PersonMapper personMapper = new SoundtrackRestMapper.PersonMapper();

    public Entry createEntry(EntrySpec entry) {
        return entryMapper.toDto(soundtrackService.createEntry(entrySpecMapper.toApi(entry)));
    }

    public Entry readEntry(@PathVariable("key") String key) {
        return entryMapper.toDto(soundtrackService.readEntry(key));
    }

    @Transactional(value = "transactionManager")
    public Entry updateEntry(@PathVariable("key") String key, @RequestBody EntrySpec entry) {
        return entryMapper.toDto(soundtrackService.updateEntry(key, entrySpecMapper.toApi(entry)));
    }

    public List<Entry> readEntries() {
        return entryMapper.toDto(soundtrackService.readEntries());
    }

    public List<Entry> readSoundtrack() {
        return entryMapper.toDto(soundtrackService.readEntries());
    }

    public List<Entry> readEntries(@PathVariable("year") Integer year) {
        return entryMapper.toDto(soundtrackService.readEntries(year));
    }

    public Entry readEntry(@PathVariable("year") Integer year, @PathVariable("ordinal") Integer ordinal) {
        return entryMapper.toDto(soundtrackService.readEntry(year, ordinal));
    }

    public List<Person> readPersons() {
        return personMapper.toDto(personService.readPersons());
    }
}
