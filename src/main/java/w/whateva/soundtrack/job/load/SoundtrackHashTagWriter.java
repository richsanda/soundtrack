package w.whateva.soundtrack.job.load;

import org.springframework.batch.item.ItemWriter;
import w.whateva.soundtrack.service.HashTagService;
import w.whateva.soundtrack.service.iao.ApiHashTagSpec;

import java.util.List;

/**
 * Created by rich on 4/3/16.
 */
public class SoundtrackHashTagWriter implements ItemWriter<ApiHashTagSpec> {

    private HashTagService hashTagService;

    public void setHashTagService(HashTagService hashTagService) {
        this.hashTagService = hashTagService;
    }

    @Override
    public void write(List<? extends ApiHashTagSpec> specs) throws Exception {
        for (ApiHashTagSpec spec : specs) {
            if (null != spec.getTag()) {
                System.out.println("here's a tag: " + spec.getTag().get());
                hashTagService.updateHashTag(spec);
            } else {
                System.out.println("Error updating tag (tag was null)");
            }
        }
    }
}
