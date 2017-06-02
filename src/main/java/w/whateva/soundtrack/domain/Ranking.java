package w.whateva.soundtrack.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.CompareToBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * Created by rich on 4/3/16.
 */
@Entity
@XmlRootElement
public class Ranking implements Comparable<Ranking> {

    @Transient
    private static final int MAX_LEGIT_POSITION = 300;
    @Transient
    private static final int SCORE_SCALE = 5;
    @Transient
    private static final int SCORE_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    public Ranking() {}

    public Ranking(Integer idx) {
        this.idx = idx;
    }

    @Id
   	@GeneratedValue
   	private Long id;

    private Integer idx;

    @ManyToOne
    private Entry entry;

    @ManyToOne
    private RankedList rankedList;

    public String getKey() {
        return id.toString();
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
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
        return new CompareToBuilder().append(idx, o.idx).toComparison();
    }

    @Transient
    public BigDecimal score() {

        int raw = MAX_LEGIT_POSITION - getIdx();
        if (raw <= 0) return BigDecimal.ONE;
        BigDecimal scaled = new BigDecimal(raw);
        scaled = scaled.setScale(SCORE_SCALE, SCORE_ROUNDING_MODE);
        scaled = scaled.divide(new BigDecimal(MAX_LEGIT_POSITION / 2), SCORE_ROUNDING_MODE);
        return BigDecimal.valueOf(Math.pow(BigDecimal.TEN.doubleValue(), scaled.doubleValue()))
                .setScale(SCORE_SCALE, SCORE_ROUNDING_MODE);
    }
}
