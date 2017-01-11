package w.whateva.soundtrack.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import w.whateva.soundtrack.api.dto.Entry;
import w.whateva.soundtrack.api.dto.EntrySpec;
import w.whateva.soundtrack.api.dto.Person;
import w.whateva.soundtrack.service.sao.ApiEntry;
import w.whateva.soundtrack.service.sao.ApiEntrySpec;
import w.whateva.soundtrack.service.sao.ApiPerson;

import java.util.List;

/**
 * Created by rich on 12/18/16.
 */
public class SoundtrackRestMapper {

    public static class EntryMapper extends Mapper<Entry, ApiEntry> {

        @Override
        public Entry newRestObject() {
            return new Entry();
        }
    }

    public static class EntrySpecMapper extends Mapper<EntrySpec, ApiEntrySpec> {

        @Override
        public ApiEntrySpec newApiObject() {
            return new ApiEntrySpec();
        }
    }

    public static class PersonMapper extends Mapper<Person, ApiPerson> {

        @Override
        public Person newRestObject() {
            return new Person();
        }

        @Override
        public ApiPerson newApiObject() {
            return new ApiPerson();
        }
    }

    private static abstract class Mapper<RestObjectType, ApiObjectType> {

        public RestObjectType newRestObject() throws MapperException {
            throw new UnimplementedMappingException();
        };

        public ApiObjectType newApiObject() throws MapperException {
            throw new UnimplementedMappingException();
        };

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

    public static class MapperException extends Exception {

    }

    public static class UnimplementedMappingException extends MapperException {

    }
}
