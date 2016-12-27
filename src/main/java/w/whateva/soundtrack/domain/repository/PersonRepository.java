package w.whateva.soundtrack.domain.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.Person;

import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

    Person findByTag(String tag);

    List<Person> findByTag(List<String> tag);
}
