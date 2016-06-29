package w.whateva.soundtrack.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@Entity
public class Person {

    @Id
   	@GeneratedValue
   	private Long id;

    private String name;

    @ManyToMany
    private List<Entry> entries;
}
