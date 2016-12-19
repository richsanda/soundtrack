package w.whateva.soundtrack.api;

import org.springframework.web.bind.annotation.*;
import w.whateva.soundtrack.api.dto.Entry;
import w.whateva.soundtrack.api.dto.EntrySpec;

import java.util.List;

/**
 * Created by rich on 12/17/16.
 */
public interface SoundtrackOperations {

    @RequestMapping(value = "/entry", method= RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    Entry createEntry(@RequestBody EntrySpec entry);

    @RequestMapping(value = "/entry/{key}", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    Entry readEntry(@PathVariable("key") String key);

    @RequestMapping(value = "/entries", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Entry> readEntries();

    @RequestMapping(value = "/soundtrack", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Entry> readSoundtrack();

    @RequestMapping(value = "/entry/{key}", method= RequestMethod.PATCH, consumes = "application/json", produces = "application/json")
    @ResponseBody
    Entry updateEntry(@PathVariable("key") String key, @RequestBody EntrySpec entry);

    @RequestMapping(value = "/entries/{year}", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<Entry> readEntries(@PathVariable("year") Integer year);

    @RequestMapping(value = "/entry/{year}/{ordinal}", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    Entry readEntry(@PathVariable("year") Integer year, @PathVariable("ordinal") Integer ordinal);
}
