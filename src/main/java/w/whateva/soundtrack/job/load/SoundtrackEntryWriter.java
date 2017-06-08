package w.whateva.soundtrack.job.load;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.Ranking;
import w.whateva.soundtrack.service.MigrationService;
import w.whateva.soundtrack.service.RankedListService;
import w.whateva.soundtrack.service.iao.ApiRankedListSpec;

import java.util.*;

/**
 * Created by rich on 4/3/16.
 */
public class SoundtrackEntryWriter implements ItemWriter<Entry>, StepExecutionListener {

    private MigrationService migrationService;
    private RankedListService rankedListService;

    private Map<String, Map<String, Integer>> rankings = Maps.newHashMap();
    private Map<String, String> keysToOldKeys = Maps.newHashMap();

    private static final String FAVORITE_KEY = "FAVORITE";
    private static final String REPRESENTATIVE_KEY = "REPRESENTATIVE";
    private static final String SHARED_KEY = "SHARED";

    public void setMigrationService(MigrationService migrationService) {
        this.migrationService = migrationService;
    }

    public void setRankedListService(RankedListService rankedListService) {
        this.rankedListService = rankedListService;
    }

    @Override
    public void write(List<? extends Entry> entries) throws Exception {
        for (Entry entry : entries) {
            String oldKey = entry.getKey();
            if (null != entry.getTitle()) {
                updateRankings(entry);
                System.out.println("here's an entry: " + entry.getTitle());
                migrationService.refreshEntry(entry);
            } else {
                Entry.print(entry);
            }
            keysToOldKeys.put(entry.getKey(), oldKey);
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        rankings.clear();
        rankings.put(FAVORITE_KEY, Maps.newHashMap());
        rankings.put(REPRESENTATIVE_KEY, Maps.newHashMap());
        rankings.put(SHARED_KEY, Maps.newHashMap());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        List<String> favoriteKeys = Lists.newArrayList(rankings.get(FAVORITE_KEY).keySet());
        List<String> representativeKeys = Lists.newArrayList(rankings.get(REPRESENTATIVE_KEY).keySet());
        List<String> sharedKeys = Lists.newArrayList(rankings.get(SHARED_KEY).keySet());

        favoriteKeys.sort(FAVORITES_COMPARATOR);
        representativeKeys.sort(REPRESENTATIVE_COMPARATOR);
        sharedKeys.sort(SHARED_COMPARATOR);

        ApiRankedListSpec favoritesSpec = buildRankedListSpec(FAVORITE_KEY, favoriteKeys);
        ApiRankedListSpec representativeSpec = buildRankedListSpec(REPRESENTATIVE_KEY, representativeKeys);
        ApiRankedListSpec sharedSpec = buildRankedListSpec(SHARED_KEY, sharedKeys);

        rankedListService.createRankedList(favoritesSpec);
        rankedListService.createRankedList(representativeSpec);
        rankedListService.createRankedList(sharedSpec);

        return stepExecution.getExitStatus();
    }

    private void updateRankings(Entry entry) {

        String key = entry.getKey();
        Collection<Ranking> entryRankings = entry.getRankings();

        for (Ranking entryRanking : entryRankings) {
            String type = entryRanking.getType();
            Integer index = entryRanking.getIdx();
            rankings.get(type).put(key, index);
        }
    }

    private Comparator<String> FAVORITES_COMPARATOR = makeRankComparator(FAVORITE_KEY);
    private Comparator<String> REPRESENTATIVE_COMPARATOR = makeRankComparator(REPRESENTATIVE_KEY);
    private Comparator<String> SHARED_COMPARATOR = makeRankComparator(SHARED_KEY);

    private Comparator<String> makeRankComparator(String type) {

        return (key1, key2) -> {

            String oldKey1 = keysToOldKeys.get(key1);
            String oldKey2 = keysToOldKeys.get(key2);
            Integer rank1 = rankings.get(type).get(oldKey1);
            Integer rank2 = rankings.get(type).get(oldKey2);
            return new CompareToBuilder().append(rank1, rank2).toComparison();
        };
    }

    private static ApiRankedListSpec buildRankedListSpec(String type, List<String> keys) {
        ApiRankedListSpec result = new ApiRankedListSpec();
        result.setType(Optional.of(type));
        result.setEntries(Optional.of(keys));
        return result;
    }
}
