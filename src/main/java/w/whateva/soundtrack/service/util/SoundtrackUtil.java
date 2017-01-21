package w.whateva.soundtrack.service.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import w.whateva.soundtrack.service.TagType;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rich on 12/26/16.
 */
public class SoundtrackUtil {

    private final static Pattern tagPattern = Pattern.compile("(\\@|\\#)([0-9a-z\\-\\.]*)(\\{(.*?)\\})?");

    public static Multimap<TagType, String> extractTags(String text) {

        Multimap<TagType, String> result = HashMultimap.create();

        if (null == text) return result;

        Matcher matcher = tagPattern.matcher(text);

        while (matcher.find()) {

            TagType tagType = TagType.getTagType(matcher.group(1));
            String key = matcher.group(2);

            result.put(tagType, key);
        }

        return result;
    }

    public static List<String> extractTags(String text, TagType tagType) {

        return Lists.newArrayList(extractTags(text).get(tagType));
    }
}
