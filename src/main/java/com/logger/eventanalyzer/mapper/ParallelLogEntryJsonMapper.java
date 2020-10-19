package com.logger.eventanalyzer.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logger.eventanalyzer.event.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelLogEntryJsonMapper implements LogEntryJsonMapper {

    private static final Logger LOG = LoggerFactory.getLogger(ParallelLogEntryJsonMapper.class);
    private AtomicInteger errorCount = new AtomicInteger(0);

    @Override
    public LogEntry apply(String json) {
        try {
            return new ObjectMapper().readValue(json, LogEntry.class);
        } catch (IOException e) {
            LOG.debug(e.getMessage());
            errorCount.incrementAndGet();
            return null;
        }
    }

    @Override
    public int getErrorCount() {
        return errorCount.get();
    }
}
