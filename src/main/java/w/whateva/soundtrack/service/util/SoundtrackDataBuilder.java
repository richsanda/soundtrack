package w.whateva.soundtrack.service.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumBiMap;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import w.whateva.soundtrack.domain.*;
import w.whateva.soundtrack.service.iao.*;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by rich on 12/18/16.
 */
public class SoundtrackDataBuilder {

    public static ApiEntry buildApiEntry(Entry entry) {
        ApiEntry result = new ApiEntry();
        BeanUtils.copyProperties(entry, result);
        if (!CollectionUtils.isEmpty(entry.getRankings())) {
            result.setRankings(entry.getRankings().stream().map(SoundtrackDataBuilder::buildApiRanking).collect(Collectors.toList()));
        }
        return result;
    }

    private static ApiRanking buildApiRanking(Ranking ranking) {
        ApiRanking result = new ApiRanking();
        result.setIndex(ranking.getIdx());
        result.setScore(ranking.score());
        result.setType(toApi(ranking.getRankedList().getType()));
        return result;
    }

    public static Entry buildEntry(ApiEntrySpec apiEntrySpec) {

        Entry entry = new Entry();

        entry.setYear(null != apiEntrySpec.getYear() ? apiEntrySpec.getYear().orElse(null) : null);
        entry.setOrdinal(null != apiEntrySpec.getOrdinal() ? apiEntrySpec.getOrdinal().orElse(null) : null);

        entry.setTitle(null != apiEntrySpec.getTitle() ? apiEntrySpec.getTitle().orElse(null) : null);
        entry.setArtist(null != apiEntrySpec.getArtist() ? apiEntrySpec.getArtist().orElse(null) : null);

        entry.setStory(null != apiEntrySpec.getStory() ? apiEntrySpec.getStory().orElse(null) : null);
        entry.setNotes(null != apiEntrySpec.getNotes() ? apiEntrySpec.getNotes().orElse(null) : null);

        entry.setSpotify(null != apiEntrySpec.getSpotify() ? apiEntrySpec.getSpotify().orElse(null) : null);
        entry.setYoutube(null != apiEntrySpec.getYoutube() ? apiEntrySpec.getYoutube().orElse(null) : null);

        entry.setReleaseDate(null != apiEntrySpec.getReleaseDate() ? apiEntrySpec.getReleaseDate().orElse(null) : null);

        return entry;
    }

    // for use in migration... starting with an Entry, give an equivalent ApiEntrySpec that can be used
    // to effect application code that should accompany installment into the database for the Entry
    public static ApiEntrySpec buildApiEntrySpec(Entry entry) {

        ApiEntrySpec apiEntrySpec = new ApiEntrySpec();

        apiEntrySpec.setYear(Optional.ofNullable(entry.getYear()));
        apiEntrySpec.setOrdinal(Optional.ofNullable(entry.getOrdinal()));

        apiEntrySpec.setTitle(Optional.ofNullable(entry.getTitle()));
        apiEntrySpec.setArtist(Optional.ofNullable(entry.getArtist()));

        apiEntrySpec.setStory(Optional.ofNullable(entry.getStory()));
        apiEntrySpec.setNotes(Optional.ofNullable(entry.getNotes()));

        apiEntrySpec.setSpotify(Optional.ofNullable(entry.getSpotify()));
        apiEntrySpec.setYoutube(Optional.ofNullable(entry.getYoutube()));

        apiEntrySpec.setReleaseDate(Optional.ofNullable(entry.getReleaseDate()));

        return apiEntrySpec;
    }

    public static ApiPerson buildApiPerson(Person person) {
        ApiPerson result = new ApiPerson();
        BeanUtils.copyProperties(person, result);
        result.setAppearances(person.getEntries().size());
        return result;
    }

    public static Person buildPerson(ApiPerson apiPerson) {
        Person result = new Person();
        BeanUtils.copyProperties(apiPerson, result);
        return result;
    }

    public static ApiHashTag buildApiHashTag(HashTag hashTag) {
        ApiHashTag result = new ApiHashTag();
        BeanUtils.copyProperties(hashTag, result);
        result.setAppearances(hashTag.getEntries().size());
        result.setType(domainToApiHashTagType.get(hashTag.getType()));
        return result;
    }

    private static final BiMap<HashTagType, ApiHashTagType> domainToApiHashTagType =
            EnumBiMap.create(HashTagType.class, ApiHashTagType.class);

    static {
        domainToApiHashTagType.put(HashTagType.FORMAT, ApiHashTagType.FORMAT);
        domainToApiHashTagType.put(HashTagType.MEDIA, ApiHashTagType.MEDIA);
        domainToApiHashTagType.put(HashTagType.MUSIC, ApiHashTagType.MUSIC);
        domainToApiHashTagType.put(HashTagType.TIMELINE, ApiHashTagType.TIMELINE);
        domainToApiHashTagType.put(HashTagType.GENERAL, ApiHashTagType.GENERAL);
        domainToApiHashTagType.put(HashTagType.PLAYER, ApiHashTagType.PLAYER);
    }

    private static final BiMap<RankedListType, ApiRankedListType> domainToApiRankedListType =
            EnumBiMap.create(RankedListType.class, ApiRankedListType.class);

    static {
        domainToApiRankedListType.put(RankedListType.FAVORITE, ApiRankedListType.FAVORITE);
        domainToApiRankedListType.put(RankedListType.SHARED, ApiRankedListType.SHARED);
        domainToApiRankedListType.put(RankedListType.REPRESENTATIVE, ApiRankedListType.REPRESENTATIVE);
    }

    private static ApiRankedListType toApi(RankedListType domain) {
        return domainToApiRankedListType.get(domain);
    }

    private static RankedListType toDomain(ApiRankedListType api) {
        return domainToApiRankedListType.inverse().get(api);
    }

    public static RankedList buildRankedList(ApiRankedListSpec apiRankedList) {

        RankedList rankedList = new RankedList();

        if (null != apiRankedList.getType()) {
            String type = apiRankedList.getType().orElse(null);
            rankedList.setType(RankedListType.valueOf(type));
        }

        return rankedList;
    }

    public static ApiRankedList buildApiRankedList(RankedList rankedList) {

        ApiRankedList result = new ApiRankedList();

        if (null == rankedList) return result;

        result.setType(rankedList.getType().toString());
        result.setEntries(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(rankedList.getRankings())) {
            Collections.sort(rankedList.getRankings()); // TODO: in db
            for (Ranking ranking : rankedList.getRankings()) {
                result.getEntries().add(buildApiEntry(ranking));
            }
        }

        return result;
    }

    private static ApiEntry buildApiEntry(Ranking ranking) {

        return buildApiEntry(ranking.getEntry());
    }
}
