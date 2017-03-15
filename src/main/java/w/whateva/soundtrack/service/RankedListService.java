package w.whateva.soundtrack.service;

import w.whateva.soundtrack.service.iao.ApiRankedList;
import w.whateva.soundtrack.service.iao.ApiRankedListSpec;

/**
 * Created by rich on 3/8/17.
 */
public interface RankedListService {

    ApiRankedList createRankedList(ApiRankedListSpec spec);

    ApiRankedList readRankedList(String key);

    ApiRankedList updateRankedList(String key, ApiRankedListSpec spec, boolean isAppend);

    ApiRankedList deleteRankedList(String key);

    ApiRankedList readRankedListByType(String type);

    ApiRankedList updateRankedListByType(String type, ApiRankedListSpec spec, boolean isAppend);

    ApiRankedList deleteRankedListByType(String type);
}
