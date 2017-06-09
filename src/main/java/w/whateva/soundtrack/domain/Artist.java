package w.whateva.soundtrack.domain;

/**
 * Created by rich on 6/8/17.
 */
public class Artist {

    private String name;
    private Integer appearances;

    public Artist(String name, Long appearances) {
        this.name = name;
        this.appearances = Math.toIntExact(appearances);
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
