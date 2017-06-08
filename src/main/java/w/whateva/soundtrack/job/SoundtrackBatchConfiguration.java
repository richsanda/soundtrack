package w.whateva.soundtrack.job;

import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import w.whateva.soundtrack.domain.Entry;
import w.whateva.soundtrack.job.load.SoundtrackEntryProcessor;
import w.whateva.soundtrack.job.load.SoundtrackEntryWriter;
import w.whateva.soundtrack.job.load.SoundtrackHashTagWriter;
import w.whateva.soundtrack.service.HashTagService;
import w.whateva.soundtrack.service.MigrationService;
import w.whateva.soundtrack.service.RankedListService;
import w.whateva.soundtrack.service.iao.ApiHashTagSpec;

import javax.persistence.EntityManagerFactory;

/**
 * Created by rich on 4/3/16.
 */
@Configuration
@EnableBatchProcessing
public class SoundtrackBatchConfiguration extends DefaultBatchConfigurer {

    private final EntityManagerFactory entityManagerFactory;

    private final MigrationService migrationService;
    private final RankedListService rankedListService;
    private final HashTagService hashTagService;

    @Autowired
    public SoundtrackBatchConfiguration(EntityManagerFactory entityManagerFactory, MigrationService migrationService, RankedListService rankedListService, HashTagService hashTagService) {
        this.entityManagerFactory = entityManagerFactory;
        this.migrationService = migrationService;
        this.rankedListService = rankedListService;
        this.hashTagService = hashTagService;
    }

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
    @StepScope
    ItemProcessor<Entry, Entry> soundtrackEntryProcessor() {
        SoundtrackEntryProcessor processor = new SoundtrackEntryProcessor();
        return processor;
    }

    @Bean
    ItemWriter<Entry> soundtrackEntryWriter() {
        SoundtrackEntryWriter writer = new SoundtrackEntryWriter();
        writer.setMigrationService(migrationService);
        writer.setRankedListService(rankedListService);
        return writer;
    }

    @Bean
    ItemWriter<ApiHashTagSpec> soundtrackHashTagWriter() {
        SoundtrackHashTagWriter writer = new SoundtrackHashTagWriter();
        writer.setHashTagService(hashTagService);
        return writer;
    }
}
