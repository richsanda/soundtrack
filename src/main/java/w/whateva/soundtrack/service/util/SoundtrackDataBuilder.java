package w.whateva.soundtrack.service.util;

import org.springframework.beans.BeanUtils;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.Person;
import w.whateva.soundtrack.service.data.IAEntry;
import w.whateva.soundtrack.service.data.IAEntrySpec;
import w.whateva.soundtrack.service.data.IAPerson;

/**
 * Created by rich on 12/18/16.
 */
public class SoundtrackDataBuilder {

    public static IAEntry buildIAEntry(Entry entry) {
        IAEntry result = new IAEntry();
        BeanUtils.copyProperties(entry, result);
        return result;
    }

    public static Entry buildEntry(IAEntrySpec iaEntrySpec) {

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

    public static IAPerson buildIAPerson(Person person) {
        IAPerson result = new IAPerson();
        BeanUtils.copyProperties(person, result);
        return result;
    }

    public static Person buildPerson(IAPerson iaPerson) {
        Person result = new Person();
        BeanUtils.copyProperties(iaPerson, result);
        return result;
    }
}
