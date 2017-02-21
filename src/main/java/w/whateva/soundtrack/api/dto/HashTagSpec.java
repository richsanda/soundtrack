package w.whateva.soundtrack.api.dto;

import java.util.Optional;

/**
 * Created by rich on 12/26/16.
 */
public class HashTagSpec {

    private Optional<String> key;
    private Optional<String> tag;
    private Optional<String> fullTag;
    private Optional<String> name;
    private Optional<String> story;

    public Optional<String> getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = Optional.ofNullable(key);
    }

    public Optional<String> getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = Optional.ofNullable(tag);
    }

    public Optional<String> getFullTag() {
        return fullTag;
    }

    public void setFullTag(String fullTag) {
        this.fullTag = Optional.ofNullable(fullTag);
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

    public Optional<String> getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = Optional.ofNullable(story);
    }
}
