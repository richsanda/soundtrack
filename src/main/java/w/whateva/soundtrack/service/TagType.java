package w.whateva.soundtrack.service;

/**
 * Created by rich on 12/28/16.
 */
public enum TagType {

    PERSON("@"),
    HASH("#");

    private final String tag;

    TagType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public static TagType getTagType(String tag) {
        for(TagType tagType : values()) {
            if(tagType.getTag().equalsIgnoreCase(tag)) return tagType;
        }
        throw new IllegalArgumentException();
    }
}
