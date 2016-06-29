package w.whateva.soundtrack.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@Entity
public class Playlist {

    @Id
   	@GeneratedValue
   	private Long id;

    private int year;
    @OneToMany
    private List<Entry> entries;
}
