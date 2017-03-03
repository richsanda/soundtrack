package w.whateva.soundtrack.domain;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;

/**
 * Created by rich on 1/17/17.
 */
@Entity
public class HashTag {

    public HashTag() {}

    public HashTag(String tag) {
        this.tag = tag;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String tag;
    private String fullTag;
    private String name;

    @Column(length = 100000)
    private String story;

    @ManyToMany(mappedBy = "hashTags")
    private List<Entry> entries;

    public String getKey() {
        return id.toString();
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

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    @Transient
    public HashTagType getType() {
        if (StringUtils.isEmpty(tag)) {
            return HashTagType.GENERAL;
        } else if (tag.contains("/")) {
            return HashTagType.valueForType(tag.substring(0, tag.indexOf('/')));
        } else {
            return HashTagType.GENERAL;
        }
    }
}
