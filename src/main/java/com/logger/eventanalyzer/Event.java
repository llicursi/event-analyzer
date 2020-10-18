package com.logger.eventanalyzer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Event {

    String id;
    String state;
    String type;
    String host;
    long timestamp;

    public boolean isInvalid() {
        return id == null || id.isEmpty() || timestamp < 0;
    }
}
