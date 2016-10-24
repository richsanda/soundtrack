package w.whateva.soundtrack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.repositories.EntryRepository;
import w.whateva.soundtrack.jobs.load.SoundtrackLoadJobRunner;

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

    /*
    @RequestMapping(value = "/load", method= RequestMethod.GET, produces = "application/text")
    public String loadSoundtrackData() throws Exception {
        soundtrackLoadJobRunner.run();
        return "Done";
    }
    */

    @RequestMapping(value = "/entry", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Entry giveMeAnEntry(@RequestParam String title) {
        List<Entry> entries = entryRepository.findByTitle(title);
        if (!CollectionUtils.isEmpty(entries)) {
            return entries.iterator().next();
        }
        return null;
    }

    @RequestMapping(value = "/entry/{year}/{ordinal}", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Entry giveMeAnEntryByYearAndOrdinal(@PathVariable("year") Integer year, @PathVariable("ordinal") Integer ordinal) {
        Entry entry = entryRepository.findByYearAndOrdinal(year, ordinal);
        return entry;
    }
}
