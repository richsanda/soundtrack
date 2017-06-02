package w.whateva.soundtrack.service.iao;

import java.math.BigDecimal;

/**
 * Created by rich on 3/12/17.
 */
public class ApiRanking {

    private ApiRankedListType type;
    private Integer index;
    private BigDecimal score;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public ApiRankedListType getType() {
        return type;
    }

    public void setType(ApiRankedListType type) {
        this.type = type;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
