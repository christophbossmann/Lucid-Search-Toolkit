package net.bossmannchristoph.lucidsearchtoolkit.web.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class LogController {
    public static final Logger LOGGER = LogManager.getLogger(LogController.class);
    private final SimpMessagingTemplate template;

    public LogController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Scheduled(fixedRate = 5000)
    public void sendLogs() {
        // Replace with your log fetching logic
        String log = "Sample log message";
        LOGGER.debug("sending log message: " + log);
        this.template.convertAndSend("/topic/logs", log);
    }
}
