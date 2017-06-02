package w.whateva.soundtrack.mapper;

import org.springframework.stereotype.Component;
import w.whateva.soundtrack.api.dto.Ranking;
import w.whateva.soundtrack.service.iao.ApiRanking;

/**
 * Created by rich on 6/2/17.
 */
@Component
public class RankingMapper extends Mapper<Ranking, ApiRanking> {

    @Override
    public Ranking toRest(ApiRanking apiRanking) {
        Ranking ranking = new Ranking();
        ranking.setType(apiRanking.getType().name());
        ranking.setIndex(apiRanking.getIndex() + 1); // make it 1-based, just for whatever
        ranking.setScore(apiRanking.getScore());
        return ranking;
    }
}
