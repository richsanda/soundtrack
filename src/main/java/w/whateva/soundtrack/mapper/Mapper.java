package w.whateva.soundtrack.mapper;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by rich on 3/3/17.
 */
abstract class Mapper<RestObjectType, ApiObjectType> {

    public RestObjectType newRestObject() throws MapperException {
        throw new UnimplementedMappingException();
    }

    public ApiObjectType newApiObject() throws MapperException {
        throw new UnimplementedMappingException();
    }

    public RestObjectType toRest(ApiObjectType apiObject) throws MapperException {
        RestObjectType result = newRestObject();
        if (null == result) return null;
        BeanUtils.copyProperties(apiObject, result);
        return result;
    }

    public ApiObjectType toApi(RestObjectType restObject) throws MapperException {
        ApiObjectType result = newApiObject();
        if (null == result) return null;
        BeanUtils.copyProperties(restObject, result);
        return result;
    }

    public List<RestObjectType> toRest(List<ApiObjectType> apiObjects) throws MapperException {
        List<RestObjectType> result = Lists.newArrayList();
        for (ApiObjectType apiObject : apiObjects) {
            result.add(toRest(apiObject));
        }
        return result;
    }

    public List<ApiObjectType> toApi(List<RestObjectType> restObjects) throws MapperException {
        List<ApiObjectType> result = Lists.newArrayList();
        for (RestObjectType restObject : restObjects) {
            result.add(toApi(restObject));
        }
        return result;
    }
}
