package w.whateva.soundtrack.service;

import w.whateva.soundtrack.service.sao.ApiHashTag;
import w.whateva.soundtrack.service.sao.ApiHashTagSpec;

import java.util.List;

/**
 * Created by rich on 12/26/16.
 */
public interface HashTagService {

    List<ApiHashTag> readHashTags();

    List<ApiHashTag> readHashTags(String type);

    List<String> readHashTagTypes();

    ApiHashTag readHashTag(String key);

    ApiHashTag updateHashTag(String key, ApiHashTagSpec spec);
}