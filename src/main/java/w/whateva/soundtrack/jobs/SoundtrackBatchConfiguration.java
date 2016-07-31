package w.whateva.soundtrack.jobs;

import com.google.common.collect.Lists;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
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
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.domain.repositories.EntryRepository;
import w.whateva.soundtrack.jobs.load.SoundtrackEntryProcessor;
import w.whateva.soundtrack.jobs.load.SoundtrackEntryWriter;
import w.whateva.soundtrack.jobs.load.SoundtrackLoadJobRunner;

import java.util.List;
import java.util.Set;

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
    private EntryRepository entryRepository;

    @Autowired
    private EntryRepository personRepository;

    @Autowired
    private EntryRepository playlistRepository;

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


    @Override
    public JobExplorer getJobExplorer() {
        return new JobExplorer() {

            @Override
            public List<JobInstance> getJobInstances(String jobName, int start, int count) {
                return null;
            }

            @Override
            public JobExecution getJobExecution(Long executionId) {
                return null;
            }

            @Override
            public StepExecution getStepExecution(Long jobExecutionId, Long stepExecutionId) {
                return null;
            }

            @Override
            public JobInstance getJobInstance(Long instanceId) {
                return null;
            }

            @Override
            public List<JobExecution> getJobExecutions(JobInstance jobInstance) {
                return null;
            }

            @Override
            public Set<JobExecution> findRunningJobExecutions(String jobName) {
                return null;
            }

            @Override
            public List<String> getJobNames() {
                return null;
            }

            @Override
            public List<JobInstance> findJobInstancesByJobName(String jobName, int start, int count) {
                return null;
            }

            @Override
            public int getJobInstanceCount(String jobName) throws NoSuchJobException {
                return 0;
            }
        };
    }
}
