package w.whateva.soundtrack.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import w.whateva.soundtrack.domain.RankedList;
import w.whateva.soundtrack.domain.Ranking;
import w.whateva.soundtrack.domain.repository.RankedListRepository;
import w.whateva.soundtrack.domain.repository.RankingRepository;
import w.whateva.soundtrack.service.RankedListService;
import w.whateva.soundtrack.service.sao.ApiRankedList;
import w.whateva.soundtrack.service.sao.ApiRankedListSpec;

/**
 * Created by rich on 3/8/17.
 */
@Component
public class RankedListServiceImpl implements RankedListService {

    private final RankedListRepository rankedListRepository;
    private final RankingRepository rankingRepository;

    @Autowired
    public RankedListServiceImpl(RankedListRepository rankedListRepository, RankingRepository rankingRepository) {
        this.rankedListRepository = rankedListRepository;
        this.rankingRepository = rankingRepository;
    }

    @Override
    public ApiRankedList createRankedList(ApiRankedListSpec spec) {

        // TODO: this for real

        RankedList rankedList = new RankedList();
        Ranking ranking = new Ranking();
        rankedList.setRankings(Lists.newArrayList(ranking));

        rankedListRepository.save(rankedList);
        rankingRepository.save(ranking);

        return new ApiRankedList();
    }

    @Override
    public ApiRankedList readRankedList(String key) {
        return null;
    }

    @Override
    public ApiRankedList updateRankedList(String key, ApiRankedListSpec spec, boolean isAppend) {
        return null;
    }

    @Override
    public ApiRankedList deleteRankedList(String key) {
        return null;
    }
}