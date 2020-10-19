package com.logger.eventanalyzer.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "events")
@DynamicUpdate
public final class Event implements Validatable {

    @Id
    private String id;
    private Long duration;
    private String type;
    private String host;
    private Boolean alert;

    public boolean isValid() {
        return duration > 0;
    }

}
