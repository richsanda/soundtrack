package w.whateva.soundtrack.service.iao;

/**
 * Created by rich on 12/26/16.
 */
public class ApiHashTag {

    private String key;
    private String tag;
    private String fullTag;
    private ApiHashTagType type;
    private String name;
    private String story;
    private Integer appearances;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFullTag() {
        return fullTag;
    }

    public void setFullTag(String fullTag) {
        this.fullTag = fullTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public Integer getAppearances() {
        return appearances;
    }

    public void setAppearances(Integer appearances) {
        this.appearances = appearances;
    }

    public ApiHashTagType getType() {
        return type;
    }

    public void setType(ApiHashTagType type) {
        this.type = type;
    }
}
