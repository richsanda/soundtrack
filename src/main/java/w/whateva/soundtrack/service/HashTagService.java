package w.whateva.soundtrack.service;

import w.whateva.soundtrack.service.sao.ApiHashTag;

import java.util.List;

/**
 * Created by rich on 12/26/16.
 */
public interface HashTagService {

    List<ApiHashTag> readHashTags();
}
