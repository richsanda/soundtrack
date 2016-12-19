package w.whateva.soundtrack.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import w.whateva.soundtrack.api.dto.Entry;
import w.whateva.soundtrack.api.dto.EntrySpec;
import w.whateva.soundtrack.service.data.ApiEntry;

import java.util.List;

/**
 * Created by rich on 12/18/16.
 */
public class SoundtrackRestMapper {

    public static ApiEntry map(Entry entry) {
        ApiEntry entryArg = new ApiEntry();
        BeanUtils.copyProperties(entry, entryArg);
        return entryArg;
    }

    public static ApiEntry map(EntrySpec entry) {
        ApiEntry entryArg = new ApiEntry();
        BeanUtils.copyProperties(entry, entryArg);
        return entryArg;
    }

    public static Entry map(ApiEntry apiEntry) {
        Entry entry = new Entry();
        BeanUtils.copyProperties(apiEntry, entry);
        return entry;
    }

    public static List<Entry> map(List<ApiEntry> apiEntries) {
        List<Entry> result = Lists.newArrayList();
        for (ApiEntry apiEntry : apiEntries) {
            result.add(map(apiEntry));
        }
        return result;
    }
}
