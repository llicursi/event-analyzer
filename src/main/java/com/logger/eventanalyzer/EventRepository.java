package com.logger.eventanalyzer;

import com.logger.eventanalyzer.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EventRepository extends JpaRepository<Event, String> {
}
