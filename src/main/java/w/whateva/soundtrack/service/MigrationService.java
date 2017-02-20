package w.whateva.soundtrack.service;

import w.whateva.soundtrack.domain.Entry;

/**
 * Created by rich on 2/19/17.
 */
public interface MigrationService {

    void refreshEntry(Entry entry);
}
