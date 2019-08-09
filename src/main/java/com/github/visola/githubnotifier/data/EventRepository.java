package com.github.visola.githubnotifier.data;

import com.github.visola.githubnotifier.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
