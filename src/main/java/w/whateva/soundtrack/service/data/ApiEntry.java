package w.whateva.soundtrack.service.data;

/**
 * Created by rich on 12/17/16.
 */
public class ApiEntry {

    private String key;

    private Integer year;
    private Integer ordinal;

    private String title;
    private String artist;

    private String story;
    private String notes;

    private String spotify;
    private String youtube;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getYear() { return year; }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getOrdinal() { return ordinal; }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSpotify() {
        return spotify;
    }

    public void setSpotify(String spotify) {
        this.spotify = spotify;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
}
