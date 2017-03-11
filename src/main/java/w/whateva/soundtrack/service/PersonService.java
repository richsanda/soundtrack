package w.whateva.soundtrack.service;

import w.whateva.soundtrack.service.iao.ApiPerson;

import java.util.List;

/**
 * Created by rich on 12/26/16.
 */
public interface PersonService {

    List<ApiPerson> readPersons();
}
