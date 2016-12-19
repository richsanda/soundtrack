package w.whateva.soundtrack.domain.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import w.whateva.soundtrack.domain.Entry;

import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@Component
public interface EntryRepository extends PagingAndSortingRepository<Entry, Long> {

    Entry findById(Long id);

    Entry findByYearAndOrdinal(Integer year, Integer ordinal);

    List<Entry> findByYear(Integer year);
}
