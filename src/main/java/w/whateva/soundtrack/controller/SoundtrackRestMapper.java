package w.whateva.soundtrack.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import w.whateva.soundtrack.api.dto.Entry;
import w.whateva.soundtrack.api.dto.EntrySpec;
import w.whateva.soundtrack.api.dto.Person;
import w.whateva.soundtrack.service.sao.SAEntry;
import w.whateva.soundtrack.service.sao.SAEntrySpec;
import w.whateva.soundtrack.service.sao.SAPerson;

import java.util.List;

/**
 * Created by rich on 12/18/16.
 */
public class SoundtrackRestMapper {

    public static class EntryMapper extends Mapper<Entry, SAEntry> {

        @Override
        public Entry newOuter() {
            return new Entry();
        }
    }

    public static class EntrySpecMapper extends Mapper<EntrySpec, SAEntrySpec> {

        @Override
        public SAEntrySpec newInner() {
            return new SAEntrySpec();
        }
    }

    public static class PersonMapper extends Mapper<Person, SAPerson> {

        @Override
        public Person newOuter() {
            return new Person();
        }

        @Override
        public SAPerson newInner() {
            return new SAPerson();
        }
    }

    private static abstract class Mapper<OuterType, InnerType> {

        public OuterType newOuter() throws MapperException {
            throw new UnimplementedMappingException();
        };

        public InnerType newInner() throws MapperException {
            throw new UnimplementedMappingException();
        };

        public OuterType toOuter(InnerType inner) throws MapperException {
            OuterType result = newOuter();
            if (null == result) return null;
            BeanUtils.copyProperties(inner, result);
            return result;
        }

        public InnerType toInner(OuterType outer) throws MapperException {
            InnerType result = newInner();
            if (null == result) return null;
            BeanUtils.copyProperties(outer, result);
            return result;
        }

        public List<OuterType> toOuter(List<InnerType> inners) throws MapperException {
            List<OuterType> result = Lists.newArrayList();
            for (InnerType inner : inners) {
                result.add(toOuter(inner));
            }
            return result;
        }

        public List<InnerType> toInner(List<OuterType> outers) throws MapperException {
            List<InnerType> result = Lists.newArrayList();
            for (OuterType outer : outers) {
                result.add(toInner(outer));
            }
            return result;
        }
    }

    public static class MapperException extends Exception {

    }

    public static class UnimplementedMappingException extends MapperException {

    }
}
