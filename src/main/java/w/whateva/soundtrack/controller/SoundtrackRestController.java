package w.whateva.soundtrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import w.whateva.soundtrack.api.dto.Entry;
import w.whateva.soundtrack.api.dto.EntrySpec;
import w.whateva.soundtrack.service.SoundtrackService;

import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@RestController
public class SoundtrackRestController implements SoundtrackRestService {

    @Autowired
    SoundtrackService soundtrackService;

    @Transactional(value = "transactionManager")
    public Entry createEntry(EntrySpec entry) {
        return SoundtrackRestMapper.map(soundtrackService.createEntry(SoundtrackRestMapper.map(entry)));
    }

    public Entry readEntry(@PathVariable("key") String key) {
        return SoundtrackRestMapper.map(soundtrackService.readEntry(key));
    }

    @Transactional(value = "transactionManager")
    public Entry updateEntry(@PathVariable("key") String key, @RequestBody EntrySpec entry) {
        return SoundtrackRestMapper.map(soundtrackService.updateEntry(key, SoundtrackRestMapper.map(entry)));
    }

    public List<Entry> readEntries() {
        return SoundtrackRestMapper.map(soundtrackService.readEntries());
    }

    public List<Entry> readSoundtrack() {
        return SoundtrackRestMapper.map(soundtrackService.readEntries());
    }

    public List<Entry> readEntries(@PathVariable("year") Integer year) {
        return SoundtrackRestMapper.map(soundtrackService.readEntries(year));
    }

    public Entry readEntry(@PathVariable("year") Integer year, @PathVariable("ordinal") Integer ordinal) {
        return SoundtrackRestMapper.map(soundtrackService.readEntry(year, ordinal));
    }
}
