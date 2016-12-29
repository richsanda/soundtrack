package w.whateva.soundtrack.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import w.whateva.soundtrack.domain.Person;
import w.whateva.soundtrack.domain.repository.PersonRepository;
import w.whateva.soundtrack.service.PersonService;
import w.whateva.soundtrack.service.iao.IAPerson;
import w.whateva.soundtrack.service.util.SoundtrackDataBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rich on 12/26/16.
 */
@Component
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository personRepository;

    @Override
    public List<IAPerson> readPersons() {
        List<Person> persons = Lists.newArrayList(personRepository.findAll());
        return sortAndConvert(persons);
    }

    private List<IAPerson> sortAndConvert(List<Person> persons) {
        Collections.sort(persons, PERSON_COMPARATOR);
        List<IAPerson> result = Lists.newArrayList();
        for (Person person : persons) {
            result.add(SoundtrackDataBuilder.buildIAPerson(person));
        }
        return result;
    }

    private static final Comparator<Person> PERSON_COMPARATOR = new Comparator<Person>() {

        @Override
        public int compare(Person person1, Person person2) {
            return new CompareToBuilder()
                    .append(person1.getName(), person2.getName())
                    .toComparison();
        }
    };
}
