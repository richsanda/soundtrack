package w.whateva.soundtrack.service.util;

import org.springframework.beans.BeanUtils;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.Person;
import w.whateva.soundtrack.service.sao.SAEntry;
import w.whateva.soundtrack.service.sao.SAEntrySpec;
import w.whateva.soundtrack.service.sao.SAPerson;

/**
 * Created by rich on 12/18/16.
 */
public class SoundtrackDataBuilder {

    public static SAEntry buildSAEntry(Entry entry) {
        SAEntry result = new SAEntry();
        BeanUtils.copyProperties(entry, result);
        return result;
    }

    public static Entry buildEntry(SAEntrySpec iaEntrySpec) {

        Entry entry = new Entry();

        entry.setYear(iaEntrySpec.getYear().orElse(null));

        entry.setTitle(iaEntrySpec.getTitle().orElse(null));
        entry.setArtist(iaEntrySpec.getArtist().orElse(null));

        entry.setStory(iaEntrySpec.getStory().orElse(null));
        entry.setNotes(iaEntrySpec.getNotes().orElse(null));

        entry.setSpotify(iaEntrySpec.getSpotify().orElse(null));
        entry.setYoutube(iaEntrySpec.getYoutube().orElse(null));

        return entry;
    }

    public static SAPerson buildSAPerson(Person person) {
        SAPerson result = new SAPerson();
        BeanUtils.copyProperties(person, result);
        return result;
    }

    public static Person buildPerson(SAPerson iaPerson) {
        Person result = new Person();
        BeanUtils.copyProperties(iaPerson, result);
        return result;
    }
}
