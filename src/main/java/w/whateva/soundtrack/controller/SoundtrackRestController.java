package w.whateva.soundtrack.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import w.whateva.soundtrack.api.dto.*;
import w.whateva.soundtrack.service.HashTagService;
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

    @Autowired
    HashTagService hashTagService;

    private static final SoundtrackRestMapper.EntryMapper entryMapper = new SoundtrackRestMapper.EntryMapper();
    private static final SoundtrackRestMapper.EntrySpecMapper entrySpecMapper = new SoundtrackRestMapper.EntrySpecMapper();
    private static final SoundtrackRestMapper.PersonMapper personMapper = new SoundtrackRestMapper.PersonMapper();
    private static final SoundtrackRestMapper.HashTagMapper hashTagMapper = new SoundtrackRestMapper.HashTagMapper();
    private static final SoundtrackRestMapper.HashTagSpecMapper hashTagSpecMapper = new SoundtrackRestMapper.HashTagSpecMapper();

    @Transactional(value = "transactionManager")
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

    public Entry deleteEntry(@PathVariable("key") String key) {
        try {
            return entryMapper.toRest(soundtrackService.deleteEntry(key));
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

    public List<HashTag> readHashTags(String type) {
        try {
            if (StringUtils.isEmpty(type)) {
                return hashTagMapper.toRest(hashTagService.readHashTags());
            } else {
                return hashTagMapper.toRest(hashTagService.readHashTags(type));
            }
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    @Override
    public HashTag readHashTag(@PathVariable("key") String key) {
        try {
            return hashTagMapper.toRest(hashTagService.readHashTag(key));
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    @Override
    public HashTag updateHashTag(@PathVariable("key") String key, @RequestBody HashTagSpec spec) {
        try {
            return hashTagMapper.toRest(hashTagService.updateHashTag(key, hashTagSpecMapper.toApi(spec)));
        } catch (SoundtrackRestMapper.MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }
}
