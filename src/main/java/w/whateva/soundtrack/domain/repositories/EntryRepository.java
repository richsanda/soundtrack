package w.whateva.soundtrack.domain.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import w.whateva.soundtrack.domain.Entry;

import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@Component
public interface EntryRepository extends PagingAndSortingRepository<Entry, Long> {

    List<Entry> findByTitle(String title);

    Entry findByYearAndOrdinal(Integer year, Integer ordinal);
}
