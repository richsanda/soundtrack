package w.whateva.soundtrack.job.load;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by rich on 4/3/16.
 *
 * TODO: um, do i need this ?
 */
public class SoundtrackLoadFromJsonJobRunner implements ApplicationListener {

    private Job job;
    private JobLauncher jobLauncher;

    public void setJob(Job job) {
        this.job = job;
    }

    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public void run() throws Exception {
        jobLauncher.run(job, new JobParameters());
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            try {
                //run();
            } catch (Exception e) {
                System.out.println("COULDN'T RUN THE JOB TO LOAD DATA");
            }
        }
    }
}
