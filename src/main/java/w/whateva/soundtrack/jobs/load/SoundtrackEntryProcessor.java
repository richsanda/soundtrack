package w.whateva.soundtrack.jobs.load;

import org.springframework.batch.item.ItemProcessor;
import w.whateva.soundtrack.domain.Entry;

/**
 * Created by rich on 4/3/16.
 */
public class SoundtrackEntryProcessor implements ItemProcessor<Entry, Entry> {

    @Override
    public Entry process(Entry entry) throws Exception {

        // convert file to season, scoring period, and team
        // game.init();

        return entry;
    }
}
