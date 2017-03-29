package w.whateva.soundtrack.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.job.load.SoundtrackJsonEntryReader;
import w.whateva.soundtrack.job.load.SoundtrackJsonHashTagReader;
import w.whateva.soundtrack.job.load.SoundtrackLoadFromJsonJobRunner;
import w.whateva.soundtrack.service.iao.ApiHashTagSpec;

/**
 * Created by rich on 2/19/17.
 */
@Configuration
public class SoundtrackJsonBatchConfiguration {

    private final JobBuilderFactory jobs;

    private final StepBuilderFactory steps;

    private final JobLauncher jobLauncher;

    private final SoundtrackBatchConfiguration config;

    @Autowired
    public SoundtrackJsonBatchConfiguration(JobBuilderFactory jobs, StepBuilderFactory steps, JobLauncher jobLauncher, SoundtrackBatchConfiguration config) {
        this.jobs = jobs;
        this.steps = steps;
        this.jobLauncher = jobLauncher;
        this.config = config;
    }

    //@Bean
    //public SoundtrackLoadFromJsonJobRunner soundtrackLoadFromJsonJobRunner() throws Exception {
    //    SoundtrackLoadFromJsonJobRunner runner = new SoundtrackLoadFromJsonJobRunner();
    //    runner.setJob(soundtrackLoadFromJsonJob());
    //    runner.setJobLauncher(jobLauncher);
    //    return runner;
    //}

    @Bean
    public Job soundtrackLoadFromJsonJob() throws Exception {
        return this.jobs.get("soundtrackLoadFromJsonJob")
                .start(soundtrackLoadFromJsonStep())
                .next(hashTagsLoadFromJsonStep())
                .build();
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
    protected Step hashTagsLoadFromJsonStep() throws Exception {
        return this.steps.get("hashTagsLoadFromJsonStep")
                .<ApiHashTagSpec, ApiHashTagSpec>chunk(2)
                .reader(soundtrackJsonHashTagReader())
                .writer(config.soundtrackHashTagWriter())
                .build();
    }

    @Bean
    @StepScope
    protected ItemReader<Entry> soundtrackJsonEntryReader() {

        Resource resource = resourceLoader.getResource("data/data.json");
        return new SoundtrackJsonEntryReader(resource);
    }

    @Bean
    @StepScope
    protected ItemReader<ApiHashTagSpec> soundtrackJsonHashTagReader() {

        Resource resource = resourceLoader.getResource("data/tags.json");
        return new SoundtrackJsonHashTagReader(resource);
    }
}
