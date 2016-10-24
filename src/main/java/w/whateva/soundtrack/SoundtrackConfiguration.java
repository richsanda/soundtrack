package w.whateva.soundtrack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * Created by rich on 10/23/16.
 */
@Configuration
public class SoundtrackConfiguration extends WebMvcConfigurationSupport {

    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(converter());
        addDefaultHttpMessageConverters(converters);
    }

    @Bean
    MappingJackson2HttpMessageConverter converter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //do your customizations here...
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }
}
