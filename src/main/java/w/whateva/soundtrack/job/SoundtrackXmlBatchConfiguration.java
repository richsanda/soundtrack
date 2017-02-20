package w.whateva.soundtrack.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.repository.EntryRepository;
import w.whateva.soundtrack.job.load.SoundtrackEntryProcessor;
import w.whateva.soundtrack.job.load.SoundtrackEntryWriter;
import w.whateva.soundtrack.job.load.SoundtrackLoadFromXmlJobRunner;

/**
 * Created by rich on 2/19/17.
 */
public class SoundtrackXmlBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private SoundtrackBatchConfiguration config;

    @Bean
    public SoundtrackLoadFromXmlJobRunner soundtrackLoadFromXmlJobRunner() throws Exception {
        SoundtrackLoadFromXmlJobRunner runner = new SoundtrackLoadFromXmlJobRunner();
        runner.setJob(soundtrackLoadFromXmlJob());
        runner.setJobLauncher(jobLauncher);
        return runner;
    }

    @Bean
    public Job soundtrackLoadFromXmlJob() throws Exception {
        return this.jobs.get("soundtrackLoadFromXmlJob").start(soundtrackLoadFromXmlStep()).build();
    }

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Bean
    protected Step soundtrackLoadFromXmlStep() throws Exception {
        return this.steps.get("soundtrackLoadFromXmlStep")
                .<Entry, Entry>chunk(10)
                .reader(soundtrackXmlEntryReader())
                .processor(config.soundtrackEntryProcessor())
                .writer(config.soundtrackEntryWriter())
                .build();
    }

    @Bean
    @StepScope
    protected ResourceAwareItemReaderItemStream<Entry> soundtrackXmlEntryReader() {
        StaxEventItemReader<Entry> reader = new StaxEventItemReader<Entry>();
        //reader.setFragmentRootElementNames(new String[]{"entry", "person", "playlist"});
        reader.setFragmentRootElementName("entry");
        Resource resource = resourceLoader.getResource("data/data.formatted.xml");
        reader.setResource(resource);
        reader.setUnmarshaller(entryUnmarshaller());
        return reader;
    }

    @Bean
    protected Jaxb2Marshaller entryUnmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        //marshaller.setClassesToBeBound(Entry.class, Person.class, Playlist.class);
        marshaller.setClassesToBeBound(Entry.class);
        return marshaller;
    }
}
