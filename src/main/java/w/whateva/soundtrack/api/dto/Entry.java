package w.whateva.soundtrack.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by rich on 12/18/16.
 */
public class Entry {

    private String key;

    private Integer year;
    private Integer ordinal;

    private String title;
    private String artist;

    private String story;
    private String notes;

    private String spotify;
    private String youtube;

    private List<Ranking> rankings;
    private BigDecimal score;
    private BigDecimal soundtrackPosition;
    private BigDecimal releasePosition;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate releaseDate;

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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Ranking> getRankings() {
        return rankings;
    }

    public void setRankings(List<Ranking> rankings) {
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
