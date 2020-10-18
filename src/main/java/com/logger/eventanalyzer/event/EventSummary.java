package com.logger.eventanalyzer.event;

import com.logger.eventanalyzer.Validatable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class EventSummary implements Validatable {

    private final String id;
    private final Long duration;
    private final Long startTime;
    private final String type;
    private final String host;

    public boolean isValid() {
        return duration > 0;
    }

}
