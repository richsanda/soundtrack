package w.whateva.soundtrack.jobs;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.repositories.EntryRepository;
import w.whateva.soundtrack.jobs.load.SoundtrackEntryProcessor;
import w.whateva.soundtrack.jobs.load.SoundtrackEntryWriter;
import w.whateva.soundtrack.jobs.load.SoundtrackLoadJobRunner;

import javax.persistence.EntityManagerFactory;

/**
 * Created by rich on 4/3/16.
 */
@Configuration
@EnableBatchProcessing
public class SoundtrackBatchConfiguration extends DefaultBatchConfigurer {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private EntryRepository personRepository;

    @Autowired
    private EntryRepository playlistRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    // this is, like, super crucial to tying the start job to the jpa entity manager used in the controller
    @Bean
    public PlatformTransactionManager platformTransactionManager() {
         return new JpaTransactionManager(entityManagerFactory);
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return platformTransactionManager();
    }

    @Bean
    public SoundtrackLoadJobRunner soundtrackLoadJobRunner() throws Exception {
        SoundtrackLoadJobRunner runner = new SoundtrackLoadJobRunner();
        runner.setJob(soundtrackLoadJob());
        runner.setJobLauncher(jobLauncher);
        return runner;
    }

    @Bean
    public Job soundtrackLoadJob() throws Exception {
        return this.jobs.get("soundtrackLoadJob").start(soundtrackLoadStep()).build();
    }

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Bean
    protected Step soundtrackLoadStep() throws Exception {
        return this.steps.get("soundtrackLoadStep")
                .<Entry, Entry>chunk(10)
                .reader(soundtrackEntryReader())
                .processor(soundtrackEntryProcessor())
                .writer(soundtrackEntryWriter())
                .build();
    }

    @Bean
    @StepScope
    protected ResourceAwareItemReaderItemStream<Entry> soundtrackEntryReader() {
        StaxEventItemReader<Entry> reader = new StaxEventItemReader<Entry>();
        //reader.setFragmentRootElementNames(new String[]{"entry", "person", "playlist"});
        reader.setFragmentRootElementName("entry");
        Resource resource = resourceLoader.getResource("data/data.formatted.xml");
        reader.setResource(resource);
        reader.setUnmarshaller(entryUnmarshaller());
        return reader;
    }

    @Bean
    @StepScope
    protected ItemProcessor<Entry, Entry> soundtrackEntryProcessor() {
        SoundtrackEntryProcessor processor = new SoundtrackEntryProcessor();
        return processor;
    }

    @Bean
    protected Jaxb2Marshaller entryUnmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        //marshaller.setClassesToBeBound(Entry.class, Person.class, Playlist.class);
        marshaller.setClassesToBeBound(Entry.class);
        return marshaller;
    }

    @Bean
    protected ItemWriter<Entry> soundtrackEntryWriter() {
        SoundtrackEntryWriter writer = new SoundtrackEntryWriter();
        writer.setEntryRepository(entryRepository);
        return writer;
    }
}
