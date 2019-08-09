package com.github.visola.githubnotifier.puller;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.model.Event;
import com.github.visola.githubnotifier.service.ConfigurationEvent;
import com.github.visola.githubnotifier.service.EventService;
import com.github.visola.githubnotifier.service.EventsLoadedEvent;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class EventPuller {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventPuller.class);

  private final ApplicationEventPublisher applicationEventPublisher;
  private final long eventsInterval;
  private ScheduledFuture<?> eventPullFuture;
  private final EventService eventService;
  private final TaskScheduler taskScheduler;

  @Autowired
  public EventPuller(ApplicationEventPublisher applicationEventPublisher,
                     EventService eventService,
                     TaskScheduler taskScheduler,
                     @Value("${pull.schedule.events}") int eventIntervalInSeconds) {
    this.eventsInterval = TimeUnit.SECONDS.toMillis(eventIntervalInSeconds);
    this.applicationEventPublisher = applicationEventPublisher;
    this.eventService = eventService;
    this.taskScheduler = taskScheduler;
  }

  public void updateAllEvents() {
    LOGGER.debug("Fetching events...");
    List<Event> events = eventService.getEvents();
    applicationEventPublisher.publishEvent(new EventsLoadedEvent(this, events));
    LOGGER.debug("{} events fetched.", events.size());
  }

  @EventListener
  public void configurationChangedOrLoaded(ConfigurationEvent event) {
    Optional<Configuration> configuration = event.getConfiguration();
    if (configuration.isPresent() && configuration.get().isValid()) {
      eventPullFuture = taskScheduler.scheduleAtFixedRate(this::updateAllEvents, eventsInterval);
      return;
    }

    if (eventPullFuture != null) {
      eventPullFuture.cancel(false);
    }
  }

}
