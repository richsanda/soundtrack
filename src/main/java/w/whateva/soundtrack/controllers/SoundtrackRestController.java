package w.whateva.soundtrack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.repositories.EntryRepository;
import w.whateva.soundtrack.jobs.load.SoundtrackLoadJobRunner;

/**
 * Created by rich on 4/3/16.
 */
@RestController
public class SoundtrackRestController {

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    SoundtrackLoadJobRunner soundtrackLoadJobRunner;

    @RequestMapping(value = "/load", method= RequestMethod.GET, produces = "application/html")
    public String loadOclData() throws Exception {
        soundtrackLoadJobRunner.run();
        return "Done";
    }

    @RequestMapping(value = "/entry", method= RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Entry giveMeAnEntry(@RequestParam String title) {
        Entry entry = entryRepository.findByTitle(title);
        return entry;
    }
}
