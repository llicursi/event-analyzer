package com.logger.eventanalyzer;

import com.logger.eventanalyzer.config.AnalyzerConfig;
import com.logger.eventanalyzer.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;

import static com.logger.eventanalyzer.config.AnalyzerConfigUtil.getSequentialAnalyzerConfig;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventAnalyzer eventAnalyzer;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event mockEvent = new Event("id-1", 10L, null, null, true);
    private AnalyzerConfig mockConfig = getSequentialAnalyzerConfig("mocked-value");

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenSourceProduceSingleEvent_thenPersistsSingleEvent() {
        mockEventAnalyzerToConsumeSeveralMocks(1);
        when(eventRepository.save(any(Event.class))).thenReturn(mockEvent);
        eventService.processEvents(mockConfig);

        verify(eventAnalyzer, times(1)).analyze(any(AnalyzerConfig.class), anyConsumer());
        verify(eventRepository, times(1)).save(any(Event.class));
    }


    @Test
    void givenSourceProduceMultipleEvent_thenPersistsSameAmountEvent() {
        mockEventAnalyzerToConsumeSeveralMocks(3);
        when(eventRepository.save(any(Event.class))).thenReturn(mockEvent);
        eventService.processEvents(mockConfig);

        verify(eventAnalyzer, times(1)).analyze(any(AnalyzerConfig.class), anyConsumer());
        verify(eventRepository, times(3)).save(any(Event.class));
    }

    @Test
    void givenSourceProduceZeroEvent_thenDontInvokeRepository() {
        mockEventAnalyzerToConsumeSeveralMocks(0);
        eventService.processEvents(mockConfig);

        verify(eventAnalyzer, times(1)).analyze(any(AnalyzerConfig.class), anyConsumer());
        verify(eventRepository, never()).save(any(Event.class));
    }

    private Consumer anyConsumer() {
        return any(Consumer.class);
    }

    private void mockEventAnalyzerToConsumeSeveralMocks(int numberOfMocks) {
        doAnswer(answer -> {
            Consumer<? super Event> mockConsumer = (Consumer<Event>) answer.getArgument(1);
            for (int i = 0; i < numberOfMocks; i++) {
                mockConsumer.accept(mockEvent);
            }
            return null;
        }).when(eventAnalyzer).analyze(any(AnalyzerConfig.class), anyConsumer());
    }
}
