package w.whateva.soundtrack.domain.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import w.whateva.soundtrack.domain.Entry;

/**
 * Created by rich on 4/3/16.
 */
public interface EntryRepository extends PagingAndSortingRepository<Entry, Long> {

    Entry findByTitle(String title);
}
