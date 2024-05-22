package net.bossmannchristoph.lucidsearchtoolkit.web.api.controller;

import com.squareup.okhttp.internal.Platform;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;


@RestController
public class HelloController {

    private static final Logger LOGGER = LogManager.getLogger(HelloController.class);

    @RequestMapping(value = "hello/{helloId}", method = RequestMethod.GET)
    public String helloWorld(@PathVariable int helloId) {
        return "Hello World: " + helloId + "!";
    }

    @RequestMapping(value = "ping", method = RequestMethod.GET)
    public String ping() {
        LOGGER.log(Level.DEBUG, "my test debug message");
        return "Pong!";
    }
}
