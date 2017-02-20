package w.whateva.soundtrack.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import w.whateva.soundtrack.domain.HashTag;
import w.whateva.soundtrack.domain.Person;
import w.whateva.soundtrack.domain.repository.HashTagRepository;
import w.whateva.soundtrack.domain.repository.PersonRepository;
import w.whateva.soundtrack.service.HashTagService;
import w.whateva.soundtrack.service.PersonService;
import w.whateva.soundtrack.service.sao.ApiHashTag;
import w.whateva.soundtrack.service.sao.ApiPerson;
import w.whateva.soundtrack.service.util.SoundtrackDataBuilder;

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
}
