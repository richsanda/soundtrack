package w.whateva.soundtrack.service.util;

import org.springframework.beans.BeanUtils;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.Person;
import w.whateva.soundtrack.service.sao.ApiEntry;
import w.whateva.soundtrack.service.sao.ApiEntrySpec;
import w.whateva.soundtrack.service.sao.ApiPerson;

/**
 * Created by rich on 12/18/16.
 */
public class SoundtrackDataBuilder {

    public static ApiEntry buildApiEntry(Entry entry) {
        ApiEntry result = new ApiEntry();
        BeanUtils.copyProperties(entry, result);
        return result;
    }

    public static Entry buildEntry(ApiEntrySpec apiEntrySpec) {

        Entry entry = new Entry();

        entry.setYear(null != apiEntrySpec.getYear() ? apiEntrySpec.getYear().orElse(null) : null);
        entry.setOrdinal(null != apiEntrySpec.getOrdinal() ? apiEntrySpec.getOrdinal().orElse(null) : null);

        entry.setTitle(null != apiEntrySpec.getTitle() ? apiEntrySpec.getTitle().orElse(null) : null);
        entry.setArtist(null != apiEntrySpec.getArtist() ? apiEntrySpec.getArtist().orElse(null) : null);

        entry.setStory(null != apiEntrySpec.getStory() ? apiEntrySpec.getStory().orElse(null) : null);
        entry.setNotes(null != apiEntrySpec.getNotes() ? apiEntrySpec.getNotes().orElse(null) : null);

        entry.setSpotify(null != apiEntrySpec.getSpotify() ? apiEntrySpec.getSpotify().orElse(null) : null);
        entry.setYoutube(null != apiEntrySpec.getYoutube() ? apiEntrySpec.getYoutube().orElse(null) : null);

        return entry;
    }

    public static ApiPerson buildApiPerson(Person person) {
        ApiPerson result = new ApiPerson();
        BeanUtils.copyProperties(person, result);
        return result;
    }

    public static Person buildPerson(ApiPerson apiPerson) {
        Person result = new Person();
        BeanUtils.copyProperties(apiPerson, result);
        return result;
    }
}
