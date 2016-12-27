package w.whateva.soundtrack.service.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rich on 12/26/16.
 */
public class SoundtrackUtil {

    private final static Pattern namePattern = Pattern.compile("\\@([0-9a-z\\-]*)\\{(.*?)\\}");
    private final static Pattern tagPattern = Pattern.compile("\\#([0-9a-z\\-]*)\\{(.*?)\\}");

    public static List<String> extractPersonTags(String text) {

        List<String> result = Lists.newArrayList();

        Matcher matcher = namePattern.matcher(text);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    public static List<String> extractTags(String text) {

        List<String> result = Lists.newArrayList();

        Matcher matcher = tagPattern.matcher(text);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }
}
