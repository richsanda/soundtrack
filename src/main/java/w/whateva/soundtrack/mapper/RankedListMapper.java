package w.whateva.soundtrack.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import w.whateva.soundtrack.api.dto.RankedList;
import w.whateva.soundtrack.service.iao.ApiRankedList;

/**
 * Created by rich on 3/3/17.
 */
@Component
public class RankedListMapper extends Mapper<RankedList, ApiRankedList> {

    private final EntryMapper entryMapper;

    @Autowired
    public RankedListMapper(EntryMapper entryMapper) {
        this.entryMapper = entryMapper;
    }

    public RankedList toRest(ApiRankedList apiObject) throws MapperException {
        RankedList result = new RankedList();
        BeanUtils.copyProperties(apiObject, result);
        result.setEntries(entryMapper.toRest(apiObject.getEntries()));
        return result;
    }
}
