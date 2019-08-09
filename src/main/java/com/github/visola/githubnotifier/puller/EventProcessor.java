package com.github.visola.githubnotifier.puller;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.Event;
import com.github.visola.githubnotifier.event.EventsLoadedEvent;

@Component
public class EventProcessor {

  @EventListener
  public void eventsLoaded(EventsLoadedEvent eventsLoadedEvent) {
    for (Event e : eventsLoadedEvent.getEvents()) {
      switch(e.getType()) {
        case CommitCommentEvent:
        case PullRequestEvent:
        case PullRequestReviewCommentEvent:
        default:
          break;
      }
    }
  }

}
