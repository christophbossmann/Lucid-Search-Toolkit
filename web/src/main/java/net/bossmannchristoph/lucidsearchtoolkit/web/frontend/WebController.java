package net.bossmannchristoph.lucidsearchtoolkit.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String index() {
        return "Filter_search_result_page.html";
    }
}
