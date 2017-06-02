package w.whateva.soundtrack.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.DateTime;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

/**
 * Created by rich on 4/3/16.
 */
@Entity
@XmlRootElement
@Table(uniqueConstraints = {
        // @UniqueConstraint(columnNames = {"year", "ordinal"}) // yes, i want this, but i can't swap in 1 transaction !!
})
public class Entry {

    @Transient
    private static final LocalDate EPOCH = LocalDate.parse("1962-01-01");
    @Transient
    private static final LocalDate SOUNDTRACK_START = LocalDate.parse("1992-01-01");
    @Transient
    private static final LocalDate SOUNDTRACK_END = LocalDate.parse("2017-01-01");
    @Transient
    private static final Period TIMELINE_DURATION = Period.between(EPOCH, SOUNDTRACK_END);
    @Transient
    private static final Long TIMELINE_DAYS = ChronoUnit.DAYS.between(EPOCH, SOUNDTRACK_END);

    @Id
   	@GeneratedValue
   	private Long id;

    /*

                <Entry id="19" created="2016-04-02T02:46:53Z" createdBy="whomever" updated="2016-04-02T02:46:53Z" updatedBy="whomever" unless="doqtgjswu02tv729rjcv40copvnw24qx" filename="Entry.19.doqtgjswu02tv729rjcv40copvnw24qx.xml" update="true">
                <MixReference id="1"/>
                <Title>Crazy For You</Title>
                <Artist>Madonna</Artist>
                <Story>this is the story</Story>
                <Notes>these are the notes</Notes>
                <TitleIndex>crazy for you</TitleIndex>
                <ArtistIndex>madonna</ArtistIndex>
                <Markers>
                    <MarkerReference id="70"/>
                </Markers>
                <Spotify>spotify:track:6iABG3ruJ1ArrulilbVhai</Spotify>
                <YouTube/>
            </Entry>

     */

    private DateTime created;

    private Integer year;
    private Integer ordinal;

    private String title;
    private String artist;

    @Column(length = 100000)
    private String story;

    @Column(length = 100000)
    private String notes;

    @ManyToMany
    @JoinTable(name="entry_person") // backwards compatibility w/db
    private List<Person> persons;

    @ManyToMany
    @JoinTable(name="entry_hashtag", inverseJoinColumns = @JoinColumn(name = "hashtags_id")) // backwards compatibility w/db
    private List<HashTag> hashTags;

    private String spotify;
    private String youtube;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") // for migration
    private LocalDate releaseDate;

    @OneToMany
    private Set<Ranking> rankings;

    public String getKey() {
        return id.toString();
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

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<HashTag> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<HashTag> hashTags) {
        this.hashTags = hashTags;
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

    @OneToMany(mappedBy = "entry")
    public Set<Ranking> getRankings() {
        return rankings;
    }

    public void setRankings(Set<Ranking> rankings) {
        this.rankings = rankings;
    }

    @Transient
    public Ranking getRanking(RankedListType type) {
        return getRankings().stream().filter(r -> type.equals(r.getRankedList().getType())).findFirst().orElse(null);
    }

    @Transient
    public BigDecimal getScore() {
        if (CollectionUtils.isEmpty(getRankings())) return null;
        return getRankings().stream().map(Ranking::score).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }

    @Transient
    public BigDecimal getReleasePosition() {
        Long epochToRelease = ChronoUnit.DAYS.between(EPOCH, getReleaseDate());
        double ratio = epochToRelease / (double) TIMELINE_DAYS;
        return BigDecimal.valueOf(ratio).movePointRight(2).setScale(0, RoundingMode.HALF_UP);
    }

    @Transient
    public BigDecimal getSoundtrackPosition() {
        Long epochToSoundtrack = ChronoUnit.DAYS.between(EPOCH, getSoundtrackDate());
        double ratio = epochToSoundtrack / (double) TIMELINE_DAYS;
        return BigDecimal.valueOf(ratio).movePointRight(2).setScale(0, RoundingMode.HALF_UP);
    }

    @Transient
    private LocalDate getSoundtrackDate() {
        LocalDate result = LocalDate.of(getYear(), 1, 1);
        Double daysToAdd = Double.valueOf(getOrdinal()) / 20 * 365;
        result = result.plus(daysToAdd.longValue(), ChronoUnit.DAYS);
        return result;
    }

    public static void print(Entry entry) throws Exception {

        JAXBContext jc = JAXBContext.newInstance(Entry.class);

        QName qname = new QName("entry");
        JAXBElement<Entry> jaxbElement = new JAXBElement<Entry>(qname, Entry.class, entry);

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(jaxbElement, System.out);
    }

    public void print() throws Exception {
        print(this);
    }
}
