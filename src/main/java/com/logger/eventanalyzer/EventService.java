package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.source.SourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private static final Logger LOG = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventAnalyzer eventAnalyzer;

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public void processEvents(SourceStream source, long thresholdDuration) {
        LOG.info("Inspecting events with duration bigger than {} ms", thresholdDuration);
        eventAnalyzer.analyze(source, thresholdDuration, event -> {
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
