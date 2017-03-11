package w.whateva.soundtrack.mapper;

import org.springframework.stereotype.Component;
import w.whateva.soundtrack.api.dto.RankedListSpec;
import w.whateva.soundtrack.service.iao.ApiRankedListSpec;

/**
 * Created by rich on 3/3/17.
 */
@Component
public class RankedListSpecMapper extends Mapper<RankedListSpec, ApiRankedListSpec> {

    public Class<ApiRankedListSpec> getApiClass() {
        return ApiRankedListSpec.class;
    }
}
