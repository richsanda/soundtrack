package w.whateva.soundtrack.api.dto;

import java.math.BigDecimal;

/**
 * Created by rich on 6/2/17.
 */
public class Ranking {

    private String type;
    private int index;
    private BigDecimal score;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
