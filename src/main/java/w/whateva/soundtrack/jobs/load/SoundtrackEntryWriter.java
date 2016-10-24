package w.whateva.soundtrack.jobs.load;

import org.springframework.batch.item.ItemWriter;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.repositories.EntryRepository;

import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
public class SoundtrackEntryWriter implements ItemWriter<Entry> {

    private EntryRepository entryRepository;

    public void setEntryRepository(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Override
    public void write(List<? extends Entry> entries) throws Exception {
        for (Entry entry : entries) {
            if (null != entry.getTitle()) {
                System.out.println("here's an entry: " + entry.getTitle());
                entryRepository.save(entry);
            } else {
                Entry.print(entry);
            }
        }
    }
}
