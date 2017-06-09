package w.whateva.soundtrack.mapper;

import org.springframework.stereotype.Component;
import w.whateva.soundtrack.api.dto.Artist;
import w.whateva.soundtrack.api.dto.Person;
import w.whateva.soundtrack.service.iao.ApiArtist;
import w.whateva.soundtrack.service.iao.ApiPerson;

/**
 * Created by rich on 3/3/17.
 */
@Component
public class ArtistMapper extends Mapper<Artist, ApiArtist> {

    @Override
    public Class<Artist> getRestClass() {
        return Artist.class;
    }
}
