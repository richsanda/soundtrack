package w.whateva.soundtrack.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import w.whateva.soundtrack.api.dto.Entry;
import w.whateva.soundtrack.api.dto.EntrySpec;
import w.whateva.soundtrack.api.dto.Person;
import w.whateva.soundtrack.service.data.ApiEntry;
import w.whateva.soundtrack.service.data.ApiPerson;

import java.util.List;

/**
 * Created by rich on 12/18/16.
 */
public class SoundtrackRestMapper {

    public static class EntryMapper extends Mapper<Entry, ApiEntry> {

        @Override
        public Entry newDto() {
            return new Entry();
        }

        @Override
        public ApiEntry newApi() {
            return new ApiEntry();
        }
    }

    public static class EntrySpecMapper extends Mapper<EntrySpec, ApiEntry> {

        @Override
        public EntrySpec newDto() {
            return new EntrySpec();
        }

        @Override
        public ApiEntry newApi() {
            return new ApiEntry();
        }
    }

    public static class PersonMapper extends Mapper<Person, ApiPerson> {

        @Override
        public Person newDto() {
            return new Person();
        }

        @Override
        public ApiPerson newApi() {
            return new ApiPerson();
        }
    }

    private static abstract class Mapper<DtoType, ApiType> {

        public abstract DtoType newDto();

        public abstract ApiType newApi();

        public DtoType toDto(ApiType api) {
            DtoType result = newDto();
            BeanUtils.copyProperties(api, result);
            return result;
        }

        public ApiType toApi(DtoType dto) {
            ApiType result = newApi();
            BeanUtils.copyProperties(dto, result);
            return result;
        }

        public List<DtoType> toDto(List<ApiType> apis) {
            List<DtoType> result = Lists.newArrayList();
            for (ApiType api : apis) {
                result.add(toDto(api));
            }
            return result;
        }

        public List<ApiType> toApi(List<DtoType> dtos) {
            List<ApiType> result = Lists.newArrayList();
            for (DtoType dto : dtos) {
                result.add(toApi(dto));
            }
            return result;
        }
    }
}
