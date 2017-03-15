package w.whateva.soundtrack.domain;

import org.apache.commons.lang3.builder.CompareToBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@Entity
@XmlRootElement
public class Ranking implements Comparable<Ranking> {

    public Ranking() {}

    public Ranking(Integer index) {
        this.index = index;
    }

    @Id
   	@GeneratedValue
   	private Long id;

    private Integer index;

    @ManyToOne
    private Entry entry;

    @ManyToOne
    private RankedList rankedList;

    public String getKey() {
        return id.toString();
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public RankedList getRankedList() {
        return rankedList;
    }

    public void setRankedList(RankedList rankedList) {
        this.rankedList = rankedList;
    }

    @Override
    public int compareTo(Ranking o) {
        return new CompareToBuilder().append(index, o.index).toComparison();
    }
}
