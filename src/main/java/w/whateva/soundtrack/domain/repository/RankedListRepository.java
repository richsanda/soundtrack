package w.whateva.soundtrack.domain.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import w.whateva.soundtrack.domain.RankedList;
import w.whateva.soundtrack.domain.RankedListType;

/**
 * Created by rich on 3/7/17.
 */
public interface RankedListRepository extends PagingAndSortingRepository<RankedList, Long> {

    RankedList findById(Long id);

    RankedList findByType(RankedListType type);
}
