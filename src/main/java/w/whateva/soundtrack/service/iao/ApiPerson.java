package w.whateva.soundtrack.service.iao;

/**
 * Created by rich on 12/26/16.
 */
public class ApiPerson {

    private String key;
    private String tag;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAppearances() {
        return appearances;
    }

    public void setAppearances(Integer appearances) {
        this.appearances = appearances;
    }
}
