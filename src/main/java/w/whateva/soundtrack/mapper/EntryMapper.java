package w.whateva.soundtrack.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import w.whateva.soundtrack.api.dto.Entry;
import w.whateva.soundtrack.api.dto.Ranking;
import w.whateva.soundtrack.service.iao.ApiEntry;

import java.util.stream.Collectors;

/**
 * Created by rich on 3/3/17.
 */
@Component
public class EntryMapper extends Mapper<Entry, ApiEntry> {

    private final RankingMapper rankingMapper;

    @Autowired
    public EntryMapper(RankingMapper rankingMapper) {
        this.rankingMapper = rankingMapper;
    }

    @Override
    public Class<Entry> getRestClass() {
        return Entry.class;
    }

    @Override
    public Entry toRest(ApiEntry apiEntry) throws MapperException {
        Entry entry = super.toRest(apiEntry);
        if (!CollectionUtils.isEmpty(apiEntry.getRankings())) {
            entry.setRankings(apiEntry.getRankings().stream().map(rankingMapper::toRest).collect(Collectors.toList()));
        }
        return entry;
    }
}
