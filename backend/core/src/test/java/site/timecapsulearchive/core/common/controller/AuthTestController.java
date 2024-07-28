package site.timecapsulearchive.core.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthTestController {

    @GetMapping("/pass")
    public String test() {
        return "pass";
    }

    @GetMapping("/not-pass")
    public String notPass() {
        return "not-pass";
    }
}
