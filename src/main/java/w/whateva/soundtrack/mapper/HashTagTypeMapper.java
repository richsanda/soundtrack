package w.whateva.soundtrack.mapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumBiMap;
import org.springframework.stereotype.Component;
import w.whateva.soundtrack.api.dto.HashTagType;
import w.whateva.soundtrack.service.sao.ApiHashTagType;

/**
 * Created by rich on 3/3/17.
 */
@Component
public class HashTagTypeMapper extends Mapper<HashTagType, ApiHashTagType> {

    private static final BiMap<HashTagType, ApiHashTagType> restToApiHashTagType =
            EnumBiMap.create(HashTagType.class, ApiHashTagType.class);

    static {
        restToApiHashTagType.put(HashTagType.FORMAT, ApiHashTagType.FORMAT);
        restToApiHashTagType.put(HashTagType.MEDIA, ApiHashTagType.MEDIA);
        restToApiHashTagType.put(HashTagType.MUSIC, ApiHashTagType.MUSIC);
        restToApiHashTagType.put(HashTagType.TIMELINE, ApiHashTagType.TIMELINE);
        restToApiHashTagType.put(HashTagType.GENERAL, ApiHashTagType.GENERAL);
        restToApiHashTagType.put(HashTagType.PLAYER, ApiHashTagType.PLAYER);
    }

    @Override
    public ApiHashTagType toApi(HashTagType rest) {
        return restToApiHashTagType.get(rest);
    }

    @Override
    public HashTagType toRest(ApiHashTagType api) {
        return restToApiHashTagType.inverse().get(api);
    }
}
