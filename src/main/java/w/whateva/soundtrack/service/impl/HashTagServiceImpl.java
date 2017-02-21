package w.whateva.soundtrack.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import w.whateva.soundtrack.domain.HashTag;
import w.whateva.soundtrack.domain.repository.HashTagRepository;
import w.whateva.soundtrack.service.HashTagService;
import w.whateva.soundtrack.service.sao.ApiHashTag;
import w.whateva.soundtrack.service.sao.ApiHashTagSpec;
import w.whateva.soundtrack.service.util.SoundtrackDataBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rich on 12/26/16.
 */
@Component
public class HashTagServiceImpl implements HashTagService {

    @Autowired
    HashTagRepository hashTagRepository;

    @Override
    public List<ApiHashTag> readHashTags() {
        List<HashTag> hashTags = Lists.newArrayList(hashTagRepository.findAll());
        return convertAndSort(hashTags);
    }

    @Override
    public List<ApiHashTag> readHashTags(String type) {
        return filterByType(readHashTags(), type);
    }

    @Override
    public List<String> readHashTagTypes() {
        return null;
    }

    @Override
    public ApiHashTag readHashTag(String key) {
        return SoundtrackDataBuilder.buildApiHashTag(hashTagRepository.findById(new Long(key)));
    }

    @Override
    public ApiHashTag updateHashTag(String key, ApiHashTagSpec spec) {

        HashTag hashTag = hashTagRepository.findById(new Long(key));

        return updateHashTag(hashTag, spec);
    }

    @Override
    public ApiHashTag updateHashTag(ApiHashTagSpec spec) {

        HashTag hashTag = hashTagRepository.findByTag(spec.getTag().get());

        return updateHashTag(hashTag, spec);
    }

    private ApiHashTag updateHashTag(HashTag hashTag, ApiHashTagSpec spec) {

        if (spec.getFullTag() != null) {
            hashTag.setFullTag(spec.getFullTag().orElse(null));
        }

        if (spec.getName() != null) {
            hashTag.setName(spec.getName().orElse(null));
        }

        hashTagRepository.save(hashTag);

        return SoundtrackDataBuilder.buildApiHashTag(hashTag);
    }

    private List<ApiHashTag> convertAndSort(List<HashTag> hashTags) {
        List<ApiHashTag> result = Lists.newArrayList();
        for (HashTag hashTag : hashTags) {
            result.add(SoundtrackDataBuilder.buildApiHashTag(hashTag));
        }
        Collections.sort(result, PERSON_COMPARATOR);
        Collections.reverse(result);
        return result;
    }

    private static final Comparator<ApiHashTag> PERSON_COMPARATOR = new Comparator<ApiHashTag>() {

        @Override
        public int compare(ApiHashTag apiHashTag1, ApiHashTag apiHashTag2) {
            return new CompareToBuilder()
                    .append(apiHashTag1.getAppearances(), apiHashTag2.getAppearances())
                    .append(apiHashTag1.getTag(), apiHashTag2.getTag())
                    .toComparison();
        }
    };

    private static List<ApiHashTag> filterByType(Collection<ApiHashTag> hashTags, final String type) {

        if (StringUtils.isEmpty(type)) return null;

        Predicate<ApiHashTag> predicate = new Predicate<ApiHashTag>() {

            @Override
            public boolean apply(ApiHashTag apiHashTag) {

                String tag = apiHashTag.getTag();
                if (!tag.contains("/")) return false;
                String prefix = StringUtils.substringBefore(tag, "/");
                return type.equals(prefix);
            }
        };

        return Lists.newArrayList(Collections2.filter(hashTags, predicate));
    }
}
