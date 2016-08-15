package com.github.visola.githubnotifier.activity;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.Event;
import com.github.visola.githubnotifier.service.EventsLoadedEvent;

@Component
public class EventProcessor {

  @EventListener
  public void eventsLoaded(EventsLoadedEvent eventsLoadedEvent) {
    for (Event e : eventsLoadedEvent.getEvents()) {
      System.out.println(e.getPayload());
    }
  }

}
