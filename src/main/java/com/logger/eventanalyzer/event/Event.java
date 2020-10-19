package com.logger.eventanalyzer.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@Getter
@Table
public final class Event implements Validatable {

    @Id
    private final String id;
    private final Long duration;
    private final String type;
    private final String host;
    private final Boolean alert;

    public boolean isValid() {
        return duration > 0;
    }

}
