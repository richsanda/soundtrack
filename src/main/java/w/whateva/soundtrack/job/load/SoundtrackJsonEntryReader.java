package w.whateva.soundtrack.job.load;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.Resource;
import w.whateva.soundtrack.domain.Entry;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rich on 2/19/17.
 */
public class SoundtrackJsonEntryReader implements ItemReader<Entry> {

    private Iterator<Entry> iterator;

    public SoundtrackJsonEntryReader(Resource resource) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // from: http://stackoverflow.com/questions/28802544/java-8-localdate-jackson-format
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            List<Entry> entries = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Entry>>(){});
            iterator = entries.iterator();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        iterator = Lists.<Entry>newArrayList().iterator();
    }

    @Override
    public Entry read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
