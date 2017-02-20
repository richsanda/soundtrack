package w.whateva.soundtrack.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
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
import w.whateva.soundtrack.domain.repository.EntryRepository;
import w.whateva.soundtrack.job.load.*;

import javax.persistence.EntityManagerFactory;

/**
 * Created by rich on 2/19/17.
 */
@Configuration
public class SoundtrackJsonBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private SoundtrackBatchConfiguration config;

    @Bean
    public SoundtrackLoadFromJsonJobRunner soundtrackLoadFromJsonJobRunner() throws Exception {
        SoundtrackLoadFromJsonJobRunner runner = new SoundtrackLoadFromJsonJobRunner();
        runner.setJob(soundtrackLoadFromJsonJob());
        runner.setJobLauncher(jobLauncher);
        return runner;
    }

    @Bean
    public Job soundtrackLoadFromJsonJob() throws Exception {
        return this.jobs.get("soundtrackLoadFromJsonJob").start(soundtrackLoadFromJsonStep()).build();
    }

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Bean
    protected Step soundtrackLoadFromJsonStep() throws Exception {
        return this.steps.get("soundtrackLoadFromJsonStep")
                .<Entry, Entry>chunk(10)
                .reader(soundtrackJsonEntryReader())
                .processor(config.soundtrackEntryProcessor())
                .writer(config.soundtrackEntryWriter())
                .build();
    }

    @Bean
    @StepScope
    protected ItemReader<Entry> soundtrackJsonEntryReader() {

        Resource resource = resourceLoader.getResource("data/data.json");
        return new SoundtrackJsonEntryReader(resource);
    }
}
