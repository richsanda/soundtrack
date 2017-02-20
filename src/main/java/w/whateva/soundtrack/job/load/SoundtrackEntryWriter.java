package w.whateva.soundtrack.job.load;

import org.springframework.batch.item.ItemWriter;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.service.MigrationService;

import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
public class SoundtrackEntryWriter implements ItemWriter<Entry> {

    private MigrationService migrationService;

    public void setMigrationService(MigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @Override
    public void write(List<? extends Entry> entries) throws Exception {
        for (Entry entry : entries) {
            if (null != entry.getTitle()) {
                System.out.println("here's an entry: " + entry.getTitle());
                migrationService.refreshEntry(entry);
            } else {
                Entry.print(entry);
            }
        }
    }
}
