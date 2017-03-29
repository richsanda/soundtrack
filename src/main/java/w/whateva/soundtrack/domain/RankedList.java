package w.whateva.soundtrack.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@Entity
public class RankedList {

    public RankedList() {}

    public RankedList(RankedListType type) {
        this.type = type;
    }

    @Id
   	@GeneratedValue
   	private Long id;

    @Column(unique = true)
    private RankedListType type;

    @OneToMany(mappedBy = "rankedList")
    @OrderColumn(name = "idx") // does this work ?
    private List<Ranking> rankings;

    public String getKey() {
        return id.toString();
    }

    public RankedListType getType() {
        return type;
    }

    public void setType(RankedListType type) {
        this.type = type;
    }

    public List<Ranking> getRankings() {
        return rankings;
    }

    public void setRankings(List<Ranking> rankings) {
        this.rankings = rankings;
    }
}
