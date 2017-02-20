package w.whateva.soundtrack.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import w.whateva.soundtrack.domain.Person;
import w.whateva.soundtrack.domain.repository.PersonRepository;
import w.whateva.soundtrack.service.PersonService;
import w.whateva.soundtrack.service.sao.ApiPerson;
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
    public List<ApiPerson> readPersons() {
        List<Person> persons = Lists.newArrayList(personRepository.findAll());
        return convertAndSort(persons);
    }

    private List<ApiPerson> convertAndSort(List<Person> persons) {
        List<ApiPerson> result = Lists.newArrayList();
        for (Person person : persons) {
            result.add(SoundtrackDataBuilder.buildApiPerson(person));
        }
        Collections.sort(result, PERSON_COMPARATOR);
        Collections.reverse(result);
        return result;
    }

    private static final Comparator<ApiPerson> PERSON_COMPARATOR = new Comparator<ApiPerson>() {

        @Override
        public int compare(ApiPerson person1, ApiPerson person2) {
            return new CompareToBuilder()
                    .append(person1.getAppearances(), person2.getAppearances())
                    .append(person1.getTag(), person2.getTag())
                    .toComparison();
        }
    };
}
