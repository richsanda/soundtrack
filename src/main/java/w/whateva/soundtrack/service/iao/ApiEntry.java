package w.whateva.soundtrack.service.iao;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by rich on 12/17/16.
 */
public class ApiEntry implements Comparable<ApiEntry> {

    private String key;

    private Integer year;
    private Integer ordinal;

    private String title;
    private String artist;

    private String story;
    private String notes;

    private String spotify;
    private String youtube;

    private LocalDate releaseDate;

    private List<ApiRanking> rankings;
    private BigDecimal score;

    private BigDecimal soundtrackPosition;
    private BigDecimal releasePosition;

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

    @Override
    public int compareTo(ApiEntry other) {

        return new CompareToBuilder()
                .append(getYear(), other.getYear())
                .append(getOrdinal(), other.getOrdinal())
                .toComparison();
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<ApiRanking> getRankings() {
        return rankings;
    }

    public void setRankings(List<ApiRanking> rankings) {
        this.rankings = rankings;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getSoundtrackPosition() {
        return soundtrackPosition;
    }

    public void setSoundtrackPosition(BigDecimal soundtrackPosition) {
        this.soundtrackPosition = soundtrackPosition;
    }

    public BigDecimal getReleasePosition() {
        return releasePosition;
    }

    public void setReleasePosition(BigDecimal releasePosition) {
        this.releasePosition = releasePosition;
    }
}
