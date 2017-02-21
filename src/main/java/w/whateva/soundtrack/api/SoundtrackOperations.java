package w.whateva.soundtrack.api;

import org.springframework.web.bind.annotation.*;
import w.whateva.soundtrack.api.dto.*;

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
    List<Entry> readEntries(@RequestParam(value = "personTags", required = false) List<String> personTags, @RequestParam(value = "hashTags", required = false) List<String> hashTags);

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
    List<HashTag> readHashTags(@RequestParam(value = "type", required = false) String type);

    @RequestMapping(value = "/hashTag/{key}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    HashTag readHashTag(@PathVariable("key") String key);

    @RequestMapping(value = "/hashTag/{key}", method = RequestMethod.PATCH, consumes = "application/json", produces = "application/json")
    @ResponseBody
    HashTag updateHashTag(@PathVariable("key") String key, @RequestBody HashTagSpec hashTag);
}
