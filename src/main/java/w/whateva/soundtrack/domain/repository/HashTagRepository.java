package w.whateva.soundtrack.domain.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import w.whateva.soundtrack.domain.HashTag;
import w.whateva.soundtrack.domain.Person;

import java.util.Collection;
import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
public interface HashTagRepository extends PagingAndSortingRepository<HashTag, Long> {

    HashTag findByTag(String tag);

    List<HashTag> findByTagIn(Collection<String> tags);
}
