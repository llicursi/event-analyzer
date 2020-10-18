package com.logger.eventanalyzer.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logger.eventanalyzer.Event;
import com.logger.eventanalyzer.EventAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SequentialEventJsonMapper implements EventJsonMapper {

    private static final Logger LOG = LoggerFactory.getLogger(EventAnalyzer.class);
    private int errorCount = 0;

    @Override
    public Event apply(String json) {
        try {
            return new ObjectMapper().readValue(json, Event.class);
        } catch (IOException e) {
            LOG.debug(e.getMessage());
            errorCount++;
            return null;
        }
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }
}
