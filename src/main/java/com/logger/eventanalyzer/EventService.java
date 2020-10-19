package com.logger.eventanalyzer;

import com.logger.eventanalyzer.config.AnalyzerConfig;
import com.logger.eventanalyzer.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class EventService {

    private static final Logger LOG = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventAnalyzer eventAnalyzer;

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public void processEvents(AnalyzerConfig config) {
        LOG.info("\n{}\nInspecting events with duration bigger than {} ms", new Date(), config.getThresholdDuration());
        LOG.info("===================================================");
        eventAnalyzer.analyze(config, event -> {
            inspectEventDuration(event);
            saveEventSummary(event);
        });
    }

    private void inspectEventDuration(Event event) {
        if (event.getAlert()) {
            LOG.info("Event {} has duration of {} ms ", event.getId(), event.getDuration());
        }
    }

    private void saveEventSummary(Event event) {
        LOG.debug("Saving event {}", event.getId());
        eventRepository.save(event);
    }
}
