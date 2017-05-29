package w.whateva.soundtrack.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import w.whateva.soundtrack.domain.RankedListType;
import w.whateva.soundtrack.domain.Ranking;

/**
 * Created by rich on 3/7/17.
 */
public interface RankingRepository extends PagingAndSortingRepository<Ranking, Long> {

    @Query("SELECT DISTINCT r FROM Ranking r LEFT JOIN r.entry e LEFT JOIN r.rankedList l WHERE l.type = :type AND e.id = :key")
    Ranking findByEntryKey(@Param("type") RankedListType type, @Param("key") Long id);

    Ranking findByIdx(Integer index);
}
