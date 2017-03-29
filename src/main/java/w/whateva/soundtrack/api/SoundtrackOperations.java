package w.whateva.soundtrack.api;

import org.springframework.web.bind.annotation.*;
import w.whateva.soundtrack.api.dto.*;
import w.whateva.soundtrack.api.dto.Entry;
import w.whateva.soundtrack.api.dto.HashTag;
import w.whateva.soundtrack.api.dto.Person;
import w.whateva.soundtrack.api.dto.RankedList;
import w.whateva.soundtrack.service.iao.ApiHashTagSortSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rich on 12/17/16.
 */
public interface SoundtrackOperations {

    @RequestMapping(value = "/entry", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    Entry createEntry(@RequestBody EntrySpec entry);

    @RequestMapping(value = "/entry/{key}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    Entry readEntry(@PathVariable("key") String key);

    @RequestMapping(value = "/entries", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<Entry> readEntries(@RequestParam(value = "personTags[]", required = false) ArrayList<String> personTags, @RequestParam(value = "hashTags[]", required = false) ArrayList<String> hashTags);

    @RequestMapping(value = "/soundtrack", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<Entry> readSoundtrack();

    @RequestMapping(value = "/entry/{key}", method = RequestMethod.PATCH, consumes = "application/json", produces = "application/json")
    @ResponseBody
    Entry updateEntry(@PathVariable("key") String key, @RequestBody EntrySpec entry);

    @RequestMapping(value = "/entry/{key}", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    @ResponseBody
    Entry deleteEntry(@PathVariable("key") String key);

    @RequestMapping(value = "/entries/{year}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<Entry> readEntries(@PathVariable("year") Integer year);

    @RequestMapping(value = "/entry/{year}/{ordinal}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    Entry readEntry(@PathVariable("year") Integer year, @PathVariable("ordinal") Integer ordinal);

    @RequestMapping(value = "/persons", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<Person> readPersons();

    @RequestMapping(value = "/hashTags", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<HashTag> readHashTags(@RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "sort", required = false) ApiHashTagSortSpec sort);

    @RequestMapping(value = "/hashTag/{key}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    HashTag readHashTag(@PathVariable("key") String key);

    @RequestMapping(value = "/hashTag/{key}", method = RequestMethod.PATCH, consumes = "application/json", produces = "application/json")
    @ResponseBody
    HashTag updateHashTag(@PathVariable("key") String key, @RequestBody HashTagSpec hashTag);

    @RequestMapping(value = "/hashTag/{key}", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    @ResponseBody
    HashTag deleteHashTag(@PathVariable("key") String key);

    @RequestMapping(value = "/rankedList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    RankedList createRankedList(@RequestBody RankedListSpec spec);

    @RequestMapping(value = "/rankedList/{key}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    RankedList readRankedList(@PathVariable("key") String key);

    @RequestMapping(value = "/rankedList/{key}", method = RequestMethod.PATCH, produces = "application/json")
    @ResponseBody
    RankedList updateRankedList(@PathVariable("key") String key, @RequestBody RankedListSpec spec);

    @RequestMapping(value = "/rankedList/{key}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    RankedList deleteRankedList(@PathVariable("key") String key);

    @RequestMapping(value = "/rankedList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    RankedList readRankedListByType(@RequestParam(value = "type") String type);

    @RequestMapping(value = "/rankedList", method = RequestMethod.PATCH, produces = "application/json")
    @ResponseBody
    RankedList updateRankedListByType(@RequestParam(value = "type") String type, @RequestBody RankedListSpec spec);

    @RequestMapping(value = "/rankedList", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    RankedList deleteRankedListByType(@PathVariable("type") String type);
}
