package w.whateva.soundtrack.jobs.load;

import org.springframework.batch.item.ItemWriter;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.repositories.EntryRepository;

import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
public class SoundtrackEntryWriter implements ItemWriter<Entry> {

    private EntryRepository gameRepository;

    public void setGameRepository(EntryRepository entryRepository) {
        this.gameRepository = entryRepository;
    }

    @Override
    public void write(List<? extends Entry> entries) throws Exception {
        for (Entry entry : entries) {
            System.out.println(entry.toString());
            gameRepository.save(entry);
        }
    }
}
