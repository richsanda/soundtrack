package w.whateva.soundtrack.service.iao;

import java.util.Date;
import java.util.Optional;

/**
 * Created by rich on 12/17/16.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ApiEntrySpec {

    private Optional<Integer> year;
    private Optional<Integer> ordinal;

    private Optional<String> title;
    private Optional<String> artist;

    private Optional<String> story;
    private Optional<String> notes;

    private Optional<String> spotify;
    private Optional<String> youtube;

    private Optional<Date> release;

    public Optional<Integer> getYear() { return year; }

    public void setYear(Optional<Integer> year) {
        this.year = year;
    }

    public Optional<Integer> getOrdinal() { return ordinal; }

    public void setOrdinal(Optional<Integer> ordinal) {
        this.ordinal = ordinal;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<String> getArtist() {
        return artist;
    }

    public void setArtist(Optional<String> artist) {
        this.artist = artist;
    }

    public Optional<String> getStory() {
        return story;
    }

    public void setStory(Optional<String> story) {
        this.story = story;
    }

    public Optional<String> getNotes() {
        return notes;
    }

    public void setNotes(Optional<String> notes) {
        this.notes = notes;
    }

    public Optional<String> getSpotify() {
        return spotify;
    }

    public void setSpotify(Optional<String> spotify) {
        this.spotify = spotify;
    }

    public Optional<String> getYoutube() {
        return youtube;
    }

    public void setYoutube(Optional<String> youtube) {
        this.youtube = youtube;
    }

    public Optional<Date> getRelease() {
        return release;
    }

    public void setRelease(Optional<Date> release) {
        this.release = release;
    }
}
