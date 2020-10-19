package com.logger.eventanalyzer.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class LogEntry implements Validatable {

    String id;
    State state;
    String type;
    String host;
    long timestamp;

    public boolean isValid() {
        return id != null && state != null && !id.isEmpty() && timestamp > 0;
    }
}
