package com.logger.runner;

import com.logger.eventanalyzer.EventService;
import com.logger.eventanalyzer.config.AnalyzerConfig;
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
        logDebugArguments(args);
        String fileName = getFilenameArg(args);
        LOG.info("\nAnalyzing events from : {}", fileName);
        logExecution(() -> {
            try {
                AnalyzerConfig config = createAnalyzerConfigFromArguments(fileName, args);
                eventService.processEvents(config);
            } catch (FileNotFoundException e) {
                LOG.error("File {} not found", fileName);
            }
        });
    }

    private void logExecution(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        LOG.info("Execution completed in {} ms", (System.currentTimeMillis() - startTime));
        LOG.info("Alert report generated at :\n\tout/reports/event-alerts.log");
    }


    private AnalyzerConfig createAnalyzerConfigFromArguments(String fileName, String[] args) throws FileNotFoundException {

        return AnalyzerConfig.builder()
                .sourceStream(new FileSourceStream(new File(fileName)))
                .thresholdDuration(4L)
                .parallel(getParallelArg(args))
                .build();
    }

    private String getFilenameArg(String[] args) {
        if (args.length > 0) {
            for (String arg : args) {
                if (!arg.startsWith("-")) {
                    return arg;
                }
            }
        }
        return DEFAULT_FILENAME;
    }

    private boolean getParallelArg(String[] args) {
        for (String arg : args) {
            if (arg.equals("--parallel")) {
                return true;
            }
        }
        return false;
    }


    private void logDebugArguments(String[] args) {
        String joinedArgs = String.join(" ", args);
        LOG.debug("ARGS : '" + joinedArgs + "'");
    }
}
