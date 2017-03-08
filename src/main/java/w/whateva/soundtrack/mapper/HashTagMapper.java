package w.whateva.soundtrack.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import w.whateva.soundtrack.api.dto.HashTag;
import w.whateva.soundtrack.service.sao.ApiHashTag;

/**
 * Created by rich on 3/3/17.
 */
@Component
public class HashTagMapper extends Mapper<HashTag, ApiHashTag> {

    private final HashTagTypeMapper hashTagTypeMapper;

    @Autowired
    public HashTagMapper(HashTagTypeMapper hashTagTypeMapper) {
        this.hashTagTypeMapper = hashTagTypeMapper;
    }

    public HashTag toRest(ApiHashTag apiObject) throws MapperException {
        HashTag result = new HashTag();
        BeanUtils.copyProperties(apiObject, result);
        result.setType(hashTagTypeMapper.toRest(apiObject.getType()));
        return result;
    }

    @Override
    public ApiHashTag toApi(HashTag restObject) throws MapperException {
        ApiHashTag result = new ApiHashTag();
        BeanUtils.copyProperties(restObject, result);
        result.setType(hashTagTypeMapper.toApi(restObject.getType()));
        return result;
    }
}
