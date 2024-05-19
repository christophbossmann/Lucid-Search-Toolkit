package net.bossmannchristoph.lucidsearchtoolkit.web.api.controller;

import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;


@RestController
public class HelloController {

    @RequestMapping(value = "hello/{helloId}", method = RequestMethod.GET)
    public String helloWorld(@PathVariable int helloId) {
        return "Hello World: " + helloId + "!";
    }

    @RequestMapping(value = "ping", method = RequestMethod.GET)
    public String ping() {
        return "Pong!";
    }
}
