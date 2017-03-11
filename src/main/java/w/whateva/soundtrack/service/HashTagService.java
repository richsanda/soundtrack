package w.whateva.soundtrack.service;

import w.whateva.soundtrack.service.iao.ApiHashTag;
import w.whateva.soundtrack.service.iao.ApiHashTagSpec;
import w.whateva.soundtrack.service.iao.ApiHashTagSortSpec;

import java.util.List;

/**
 * Created by rich on 12/26/16.
 */
public interface HashTagService {

    List<ApiHashTag> readHashTags();

    List<ApiHashTag> readHashTags(String type, ApiHashTagSortSpec sortSpec);

    List<String> readHashTagTypes();

    ApiHashTag readHashTag(String key);

    ApiHashTag updateHashTag(String key, ApiHashTagSpec spec);

    ApiHashTag updateHashTag(ApiHashTagSpec spec);

    ApiHashTag deleteHashTag(String key);
}
