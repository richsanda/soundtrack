package w.whateva.soundtrack.mapper;

import org.springframework.stereotype.Component;
import w.whateva.soundtrack.api.dto.Person;
import w.whateva.soundtrack.service.iao.ApiPerson;

/**
 * Created by rich on 3/3/17.
 */
@Component
public class PersonMapper extends Mapper<Person, ApiPerson> {

    @Override
    public Person newRestObject() {
        return new Person();
    }

    @Override
    public ApiPerson newApiObject() {
        return new ApiPerson();
    }
}
