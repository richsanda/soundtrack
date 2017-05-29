package w.whateva.soundtrack.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.RankedList;
import w.whateva.soundtrack.domain.RankedListType;
import w.whateva.soundtrack.domain.Ranking;
import w.whateva.soundtrack.domain.repository.EntryRepository;
import w.whateva.soundtrack.domain.repository.RankedListRepository;
import w.whateva.soundtrack.domain.repository.RankingRepository;
import w.whateva.soundtrack.service.RankedListService;
import w.whateva.soundtrack.service.iao.ApiRankedList;
import w.whateva.soundtrack.service.iao.ApiRankedListSpec;
import w.whateva.soundtrack.service.util.SoundtrackDataBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rich on 3/8/17.
 */
@Component
public class RankedListServiceImpl implements RankedListService {

    private final RankedListRepository rankedListRepository;
    private final RankingRepository rankingRepository;
    private final EntryRepository entryRepository;

    @Autowired
    public RankedListServiceImpl(RankedListRepository rankedListRepository, RankingRepository rankingRepository, EntryRepository entryRepository) {
        this.rankedListRepository = rankedListRepository;
        this.rankingRepository = rankingRepository;
        this.entryRepository = entryRepository;
    }

    @Override
    public ApiRankedList createRankedList(ApiRankedListSpec spec) {

        RankedList rankedList = SoundtrackDataBuilder.buildRankedList(spec);

        rankedListRepository.save(rankedList);

        return updateRankedList(rankedList, spec);
    }

    public ApiRankedList updateRankedList(String key, ApiRankedListSpec spec) {

        RankedList rankedList = rankedListRepository.findById(new Long(key));

        if (null != rankedList) {
            return updateRankedList(rankedList, spec);
        }

        return null;
    }

    private ApiRankedList updateRankedList(RankedList rankedList, ApiRankedListSpec spec) {

        if (spec.getEntries().isPresent()) {

            List<String> keysRanked = spec.getEntries().get();
            Set<String> keys = new HashSet<>(keysRanked);

            if (!CollectionUtils.isEmpty(rankedList.getRankings())) {
                for (Ranking ranking : rankedList.getRankings()) {
                    if (!keys.contains(ranking.getKey())) {
                        rankingRepository.delete(ranking);
                    }
                }
            }

            List<Ranking> rankings = Lists.newArrayList();

            int rankIndex = 1;
            for (String key : keysRanked) {

                Entry entry = entryRepository.findById(new Long(key));

                if (null == entry) continue;

                Ranking ranking = rankingRepository.findByEntryKey(rankedList.getType(), new Long(key));

                if (null == ranking) ranking = new Ranking();

                ranking.setEntry(entry);
                ranking.setIdx(rankIndex++);
                ranking.setRankedList(rankedList);

                rankingRepository.save(ranking);
                rankings.add(ranking);
            }

            rankedList.setRankings(rankings);
        }

        rankedListRepository.save(rankedList);

        return SoundtrackDataBuilder.buildApiRankedList(rankedList);
    }

    @Override
    public ApiRankedList readRankedList(String key) {

        RankedList rankedList = rankedListRepository.findById(new Long(key));

        return SoundtrackDataBuilder.buildApiRankedList(rankedList);
    }

    @Override
    public ApiRankedList updateRankedList(String key, ApiRankedListSpec spec, boolean isAppend) {

        RankedList rankedList = rankedListRepository.findById(new Long(key));

        return updateRankedList(rankedList, spec);
    }

    @Override
    public ApiRankedList deleteRankedList(String key) {

        RankedList rankedList = rankedListRepository.findById(new Long(key));
        rankedListRepository.delete(rankedList);

        return SoundtrackDataBuilder.buildApiRankedList(rankedList);
    }

    @Override
    public ApiRankedList readRankedListByType(String type) {

        RankedList rankedList = rankedListRepository.findByType(RankedListType.valueOf(type));

        return SoundtrackDataBuilder.buildApiRankedList(rankedList);
    }

    @Override
    public ApiRankedList updateRankedListByType(String type, ApiRankedListSpec spec, boolean isAppend) {

        RankedList rankedList = rankedListRepository.findByType(RankedListType.valueOf(type));

        return updateRankedList(rankedList, spec);
    }

    @Override
    public ApiRankedList deleteRankedListByType(String type) {

        RankedList rankedList = rankedListRepository.findByType(RankedListType.valueOf(type));
        rankedListRepository.delete(rankedList);

        return SoundtrackDataBuilder.buildApiRankedList(rankedList);    }
}
