package w.whateva.soundtrack.api.dto;

/**
 * Created by rich on 12/26/16.
 */
public class Artist {

    private String name;
    private Integer appearances;

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
