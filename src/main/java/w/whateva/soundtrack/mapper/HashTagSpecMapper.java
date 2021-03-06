package w.whateva.soundtrack.mapper;

import org.springframework.stereotype.Component;
import w.whateva.soundtrack.api.dto.HashTagSpec;
import w.whateva.soundtrack.service.iao.ApiHashTagSpec;

/**
 * Created by rich on 3/3/17.
 */
@Component
public class HashTagSpecMapper extends Mapper<HashTagSpec, ApiHashTagSpec> {

    @Override
    public Class<HashTagSpec> getRestClass() {
        return HashTagSpec.class;
    }

    @Override
    public Class<ApiHashTagSpec> getApiClass() {
        return ApiHashTagSpec.class;
    }
}
