package w.whateva.soundtrack.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@Entity
@XmlRootElement
public class Person {

    public Person() {}

    public Person(String tag) {
        this.tag = tag;
    }

    @Id
   	@GeneratedValue
   	private Long id;

    private String tag;
    private String name;

    @ManyToMany(mappedBy = "persons")
    private List<Entry> entries;

    public String getKey() {
        return id.toString();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}
