package w.whateva.soundtrack.job.load;

import org.springframework.batch.item.ItemProcessor;
import w.whateva.soundtrack.domain.Entry;

/**
 * Created by rich on 4/3/16.
 */
public class SoundtrackEntryProcessor implements ItemProcessor<Entry, Entry> {

    @Override
    public Entry process(Entry entry) throws Exception {

        return entry;
    }
}
