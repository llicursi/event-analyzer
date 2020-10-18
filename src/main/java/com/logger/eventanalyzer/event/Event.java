package com.logger.eventanalyzer.event;

import com.logger.eventanalyzer.Validatable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Event implements Validatable {

    String id;
    State state;
    String type;
    String host;
    long timestamp;

    public boolean isValid() {
        return id == null || state == null || id.isEmpty() || timestamp < 0;
    }
}
