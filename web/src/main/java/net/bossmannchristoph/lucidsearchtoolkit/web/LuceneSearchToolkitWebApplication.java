package net.bossmannchristoph.lucidsearchtoolkit.web;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling -> TEMPORARILIY DISABLED BECAUSE NOT USED!!
public class LuceneSearchToolkitWebApplication {

	public static final Logger LOGGER = LogManager.getLogger(LuceneSearchToolkitWebApplication.class);

	public static void main(String[] args) {
		LOGGER.log(Level.INFO, "startup debug test message");
		LOGGER.log(Level.DEBUG, "startup info test message");
		SpringApplication.run(net.bossmannchristoph.lucidsearchtoolkit.web.LuceneSearchToolkitWebApplication.class, args);
	}

}
