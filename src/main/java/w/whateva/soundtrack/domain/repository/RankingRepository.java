package w.whateva.soundtrack.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import w.whateva.soundtrack.domain.Ranking;

/**
 * Created by rich on 3/7/17.
 */
public interface RankingRepository extends PagingAndSortingRepository<Ranking, Long> {

    @Query("SELECT DISTINCT r FROM Ranking r LEFT JOIN r.entry e WHERE e.id = :key")
    Ranking findByEntryKey(@Param("key") Long id);

    Ranking findByIndex(Integer index);
}
