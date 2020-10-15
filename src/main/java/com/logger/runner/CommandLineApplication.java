package com.logger.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommandLineApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(CommandLineApplication.class);

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Running command line with Spring");
        logArguments(args);
    }

    private void logArguments(String[] args) {
        String joinedArgs = String.join(" ", args);
        LOG.debug("ARGS : '" + joinedArgs + "'");
    }
}
