package w.whateva.soundtrack.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import w.whateva.soundtrack.api.dto.*;
import w.whateva.soundtrack.mapper.*;
import w.whateva.soundtrack.service.HashTagService;
import w.whateva.soundtrack.service.PersonService;
import w.whateva.soundtrack.service.RankedListService;
import w.whateva.soundtrack.service.SoundtrackService;
import w.whateva.soundtrack.service.iao.ApiHashTagSortSpec;

import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@RestController
public class SoundtrackRestController implements SoundtrackRestService {

    private final SoundtrackService soundtrackService;
    private final PersonService personService;
    private final HashTagService hashTagService;
    private final RankedListService rankedListService;

    private final EntryMapper entryMapper;
    private final EntrySpecMapper entrySpecMapper;
    private final PersonMapper personMapper;
    private final HashTagMapper hashTagMapper;
    private final HashTagSpecMapper hashTagSpecMapper;
    private final RankedListSpecMapper rankedListSpecMapper;
    private final RankedListMapper rankedListMapper;

    @Autowired
    public SoundtrackRestController(SoundtrackService soundtrackService,
                                    PersonService personService,
                                    HashTagService hashTagService,
                                    RankedListService rankedListService,
                                    EntryMapper entryMapper,
                                    EntrySpecMapper entrySpecMapper,
                                    PersonMapper personMapper,
                                    HashTagMapper hashTagMapper,
                                    HashTagSpecMapper hashTagSpecMapper,
                                    RankedListSpecMapper rankedListSpecMapper,
                                    RankedListMapper rankedListMapper) {
        this.soundtrackService = soundtrackService;
        this.personService = personService;
        this.hashTagService = hashTagService;
        this.rankedListService = rankedListService;
        this.entryMapper = entryMapper;
        this.entrySpecMapper = entrySpecMapper;
        this.personMapper = personMapper;
        this.hashTagMapper = hashTagMapper;
        this.hashTagSpecMapper = hashTagSpecMapper;
        this.rankedListSpecMapper = rankedListSpecMapper;
        this.rankedListMapper = rankedListMapper;
    }

    @Transactional(value = "transactionManager")
    public Entry createEntry(EntrySpec entry) {
        try {
            return entryMapper.toRest(soundtrackService.createEntry(entrySpecMapper.toApi(entry)));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public Entry readEntry(@PathVariable("key") String key) {
        try {
            return entryMapper.toRest(soundtrackService.readEntry(key));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    @Transactional(value = "transactionManager")
    public Entry updateEntry(@PathVariable("key") String key, @RequestBody EntrySpec entry) {
        try {
            return entryMapper.toRest(soundtrackService.updateEntry(key, entrySpecMapper.toApi(entry)));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public Entry deleteEntry(@PathVariable("key") String key) {
        try {
            return entryMapper.toRest(soundtrackService.deleteEntry(key));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public List<Entry> readEntries() {
        try {
            return entryMapper.toRest(soundtrackService.readEntries());
        } catch (MapperException e) {
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
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public List<Entry> readSoundtrack() {
        try {
            return entryMapper.toRest(soundtrackService.readEntries());
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public List<Entry> readEntries(@PathVariable("year") Integer year) {
        try {
            return entryMapper.toRest(soundtrackService.readEntries(year));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public Entry readEntry(@PathVariable("year") Integer year, @PathVariable("ordinal") Integer ordinal) {
        try {
            return entryMapper.toRest(soundtrackService.readEntry(year, ordinal));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public List<Person> readPersons() {
        try {
            return personMapper.toRest(personService.readPersons());
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public List<HashTag> readHashTags(String type, ApiHashTagSortSpec sortSpec) {

        List<HashTag> result = Lists.newArrayList();

        try {
            result = hashTagMapper.toRest(hashTagService.readHashTags(type, sortSpec));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }

        return result;
    }

    @Override
    public HashTag readHashTag(@PathVariable("key") String key) {
        try {
            return hashTagMapper.toRest(hashTagService.readHashTag(key));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    @Override
    public HashTag updateHashTag(@PathVariable("key") String key, @RequestBody HashTagSpec spec) {
        try {
            return hashTagMapper.toRest(hashTagService.updateHashTag(key, hashTagSpecMapper.toApi(spec)));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    public HashTag deleteHashTag(@PathVariable("key") String key) {
        try {
            return hashTagMapper.toRest(hashTagService.deleteHashTag(key));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    @Override
    public RankedList createRankedList(@RequestBody RankedListSpec spec) {
        try {
            return rankedListMapper.toRest(rankedListService.createRankedList(rankedListSpecMapper.toApi(spec)));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    @Override
    public RankedList readRankedList(@PathVariable("key") String key) {
        try {
            return rankedListMapper.toRest(rankedListService.readRankedList(key));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    @Override
    public RankedList updateRankedList(@PathVariable("key") String key, @RequestBody RankedListSpec spec) {
        try {
            return rankedListMapper.toRest(rankedListService.updateRankedList(key, rankedListSpecMapper.toApi(spec), false));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }

    @Override
    public RankedList deleteRankedList(@PathVariable("key") String key) {
        try {
            return rankedListMapper.toRest(rankedListService.deleteRankedList(key));
        } catch (MapperException e) {
            // TODO: throw useful exception
        }
        return null;
    }
}
