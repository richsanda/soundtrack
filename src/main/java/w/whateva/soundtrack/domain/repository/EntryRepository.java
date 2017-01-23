package w.whateva.soundtrack.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT DISTINCT e FROM Entry e LEFT JOIN e.persons p" +
            " WHERE :personTags IS NULL OR p.tag in :personTags")
    List<Entry> findByPersonTags(@Param("personTags") List<String> personTags);

    @Query("SELECT DISTINCT e FROM Entry e LEFT JOIN e.hashTags h" +
            " WHERE :hashTags IS NULL OR h.tag in :hashTags")
    List<Entry> findByHashTags(@Param("hashTags") List<String> hashTags);

    @Query("SELECT MAX(e.year) FROM Entry e")
    Integer findGreatestYear();
}
