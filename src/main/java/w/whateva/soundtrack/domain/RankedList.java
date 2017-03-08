package w.whateva.soundtrack.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
@Entity
public class RankedList {

    public RankedList() {}

    public RankedList(String type) {
        this.type = type;
    }

    @Id
   	@GeneratedValue
   	private Long id;

    private String type;

    @OneToMany
    private List<Ranking> rankings;

    public String getKey() {
        return id.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Ranking> getRankings() {
        return rankings;
    }

    public void setRankings(List<Ranking> rankings) {
        this.rankings = rankings;
    }
}
