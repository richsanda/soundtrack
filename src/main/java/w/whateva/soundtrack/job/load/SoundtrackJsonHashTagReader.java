package w.whateva.soundtrack.job.load;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.Resource;
import w.whateva.soundtrack.api.dto.HashTagSpec;
import w.whateva.soundtrack.mapper.HashTagSpecMapper;
import w.whateva.soundtrack.mapper.MapperException;
import w.whateva.soundtrack.service.iao.ApiHashTagSpec;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rich on 2/19/17.
 */
public class SoundtrackJsonHashTagReader implements ItemReader<ApiHashTagSpec> {

    private Iterator<ApiHashTagSpec> iterator;

    public SoundtrackJsonHashTagReader(Resource resource) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            List<HashTagSpec> specs = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<HashTagSpec>>(){});
            List<ApiHashTagSpec> apiSpecs = Lists.newArrayList();
            try {
                apiSpecs = new HashTagSpecMapper().toApi(specs);
            } catch (MapperException e) {
                e.printStackTrace();
            }
            iterator = apiSpecs.iterator();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        iterator = Lists.<ApiHashTagSpec>newArrayList().iterator();
    }

    @Override
    public ApiHashTagSpec read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
