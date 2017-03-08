package w.whateva.soundtrack.api.dto;


import java.util.Optional;

/**
 * Created by rich on 12/18/16.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class EntrySpec {

    private Optional<Integer> year;
    private Optional<Integer> ordinal;

    private Optional<String> title;
    private Optional<String> artist;

    private Optional<String> story;
    private Optional<String> notes;

    private Optional<String> spotify;
    private Optional<String> youtube;

    public Optional<Integer> getYear() { return year; }

    public void setYear(Integer year) {
        this.year = Optional.ofNullable(year);
    }

    public Optional<Integer> getOrdinal() { return ordinal; }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = Optional.ofNullable(ordinal);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Optional.ofNullable(title);
    }

    public Optional<String> getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = Optional.ofNullable(artist);
    }

    public Optional<String> getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = Optional.ofNullable(story);
    }

    public Optional<String> getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = Optional.ofNullable(notes);
    }

    public Optional<String> getSpotify() {
        return spotify;
    }

    public void setSpotify(String spotify) {
       this.spotify = Optional.ofNullable(spotify);
    }

    public Optional<String> getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = Optional.ofNullable(youtube);
    }
}
