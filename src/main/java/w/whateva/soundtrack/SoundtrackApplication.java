package w.whateva.soundtrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 */

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class SoundtrackApplication {

    public static void main(String[] args) throws Exception {

        ApplicationContext context = SpringApplication.run(SoundtrackApplication.class, args);

        //SoundtrackLoadJobRunner runner = context.getBean(SoundtrackLoadJobRunner.class);
        //runner.run();
    }
}
