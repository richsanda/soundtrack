package w.whateva.soundtrack.service.sao;

import java.util.Optional;

/**
 * Created by rich on 12/26/16.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ApiHashTagSpec {

    private Optional<String> key;
    private Optional<String> tag;
    private Optional<String> fullTag;
    private Optional<String> name;
    private Optional<String> story;

    public Optional<String> getKey() {
        return key;
    }

    public void setKey(Optional<String> key) {
        this.key = key;
    }

    public Optional<String> getTag() {
        return tag;
    }

    public void setTag(Optional<String> tag) {
        this.tag = tag;
    }

    public Optional<String> getFullTag() {
        return fullTag;
    }

    public void setFullTag(Optional<String> fullTag) {
        this.fullTag = fullTag;
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getStory() {
        return story;
    }

    public void setStory(Optional<String> story) {
        this.story = story;
    }
}
