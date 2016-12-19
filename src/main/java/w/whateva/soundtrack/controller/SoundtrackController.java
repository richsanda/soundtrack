package w.whateva.soundtrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rich on 4/3/16.
 */
@Controller
public class SoundtrackController {

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}
