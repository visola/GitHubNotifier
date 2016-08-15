package com.github.visola.githubnotifier.service;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.github.visola.githubnotifier.model.Event;

public class EventsLoadedEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1L;

  private final List<Event> events;

  public EventsLoadedEvent(Object source, List<Event> events) {
    super(source);
    this.events = events;
  }

  public List<Event> getEvents() {
    return events;
  }

}
