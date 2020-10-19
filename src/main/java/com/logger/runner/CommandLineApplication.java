package com.logger.runner;

import com.logger.eventanalyzer.EventService;
import com.logger.eventanalyzer.source.FileSourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootApplication
@ComponentScan(basePackages = "com.logger")
@EnableJpaRepositories(basePackages = "com.logger")
@EntityScan(basePackages = "com.logger")
public class CommandLineApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CommandLineApplication.class);
    private static final String DEFAULT_FILENAME = "logfile.json";

    @Autowired
    private EventService eventService;

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Running command line with Spring");
        logArguments(args);
        String fileName = getFileNameOrDefault(args);
        try {
            eventService.processEvents(new FileSourceStream(new File(fileName)), 4L);
        } catch (FileNotFoundException e) {
            LOG.error("File {} not found", fileName);
        }
    }

    private String getFileNameOrDefault(String[] args) {
        if (args.length > 0) {
            for (String arg : args) {
                if (!arg.startsWith("-")) {
                    return arg;
                }
            }
        }
        return DEFAULT_FILENAME;
    }


    private void logArguments(String[] args) {
        String joinedArgs = String.join(" ", args);
        LOG.debug("ARGS : '" + joinedArgs + "'");
    }
}
