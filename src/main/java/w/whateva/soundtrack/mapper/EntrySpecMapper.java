package w.whateva.soundtrack.mapper;

import org.springframework.stereotype.Component;
import w.whateva.soundtrack.api.dto.EntrySpec;
import w.whateva.soundtrack.service.sao.ApiEntrySpec;

/**
 * Created by rich on 3/3/17.
 */
@Component
public class EntrySpecMapper extends Mapper<EntrySpec, ApiEntrySpec> {

    @Override
    public ApiEntrySpec newApiObject() {
        return new ApiEntrySpec();
    }
}
