package w.whateva.soundtrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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
        try {
            return entryMapper.toRest(soundtrackService.createEntry(entrySpecMapper.toApi(entry)));
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public Entry readEntry(@PathVariable("key") String key) {
        try {
            return entryMapper.toRest(soundtrackService.readEntry(key));
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    @Transactional(value = "transactionManager")
    public Entry updateEntry(@PathVariable("key") String key, @RequestBody EntrySpec entry) {
        try {
            return entryMapper.toRest(soundtrackService.updateEntry(key, entrySpecMapper.toApi(entry)));
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    // um, for now, instead of delete, just read
    public Entry deleteEntry(@PathVariable("key") String key) {
        try {
            return entryMapper.toRest(soundtrackService.readEntry(key));
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public List<Entry> readEntries() {
        try {
            return entryMapper.toRest(soundtrackService.readEntries());
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public List<Entry> readEntries(List<String> personTags, List<String> hashTags) {
        try {
            if (!CollectionUtils.isEmpty(personTags)) {
                return entryMapper.toRest(soundtrackService.readEntriesByPersonTags(personTags));
            } else {
                return entryMapper.toRest(soundtrackService.readEntriesByHashTags(hashTags));
            }
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public List<Entry> readSoundtrack() {
        try {
            return entryMapper.toRest(soundtrackService.readEntries());
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public List<Entry> readEntries(@PathVariable("year") Integer year) {
        try {
            return entryMapper.toRest(soundtrackService.readEntries(year));
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public Entry readEntry(@PathVariable("year") Integer year, @PathVariable("ordinal") Integer ordinal) {
        try {
            return entryMapper.toRest(soundtrackService.readEntry(year, ordinal));
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public List<Person> readPersons() {
        try {
            return personMapper.toRest(personService.readPersons());
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }
}
