package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import com.logger.eventanalyzer.source.SourceStream;
import com.logger.eventanalyzer.source.StringSourceStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EventServerTest {

    @Mock
    private EventAnalyzer eventAnalyzer;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event mockEvent = new Event("id-1", 10L, null, null, false);

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenSourceProduceSingleEvent_thenPersistsSingleEvent(){
        SourceStream sourceStream = new StringSourceStream("mocked-value");

        mockEventAnalyzerToConsumeSeveralMocks(sourceStream, 1);

        when(eventRepository.save(any(Event.class))).thenReturn(mockEvent);
        eventService.processEvents(sourceStream);

        verify(eventAnalyzer, times(1) ).analyze(eq(sourceStream), anyLong(), any(Consumer.class));
        verify(eventRepository, times(1) ).save(any(Event.class));
    }

    @Test
    void givenSourceProduceMultipleEvent_thenPersistsSameAmountEvent(){
        SourceStream sourceStream = new StringSourceStream("mocked-value");
        mockEventAnalyzerToConsumeSeveralMocks(sourceStream, 3);
        when(eventRepository.save(any(Event.class))).thenReturn(mockEvent);
        eventService.processEvents(sourceStream);

        verify(eventAnalyzer, times(1) ).analyze(eq(sourceStream), anyLong(), any(Consumer.class));
        verify(eventRepository, times(3) ).save(any(Event.class));
    }

    @Test
    void givenSourceProduceZeroEvent_thenDontInvokeRepository(){
        SourceStream sourceStream = new StringSourceStream("mocked-value");
        mockEventAnalyzerToConsumeSeveralMocks(sourceStream, 0);
        eventService.processEvents(sourceStream);

        verify(eventAnalyzer, times(1) ).analyze(eq(sourceStream), anyLong(), any(Consumer.class));
        verify(eventRepository, never()).save(any(Event.class));
    }


    private void mockEventAnalyzerToConsumeSeveralMocks(SourceStream sourceStream, int numberOfMocks) {
        doAnswer(answer -> {
            Consumer<? super Event> mockConsumer = (Consumer<Event>) answer.getArgument(2);
            for (int i = 0 ; i < numberOfMocks; i++) {
                mockConsumer.accept(mockEvent);
            }
            return null;
        }).when(eventAnalyzer).analyze(eq(sourceStream), anyLong(), any(Consumer.class));
    }
}
