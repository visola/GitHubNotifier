package com.github.visola.githubnotifier.schedule;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.model.Event;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.service.ConfigurationListener;
import com.github.visola.githubnotifier.service.ConfigurationService;
import com.github.visola.githubnotifier.service.EventService;
import com.github.visola.githubnotifier.service.PullRequestService;

@Component
@Lazy
public class EventPuller implements ConfigurationListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventPuller.class);

  private final EventService eventService;
  private final PullRequestService prService;
  private final TaskScheduler taskScheduler;

  private ScheduledFuture<?> eventPullFuture;
  private ScheduledFuture<?> pullRequestPullFuture;

  private final long eventsInterval;
  private final long pullRequestInterval;


  @Autowired
  public EventPuller(ConfigurationService configurationService,
                     EventService eventService,
                     PullRequestService prService,
                     TaskScheduler taskScheduler,
                     @Value("${pull.schedule.events}") int eventIntervalInSeconds,
                     @Value("${pull.schedule.pull-requests}") int pullRequestIntervalInSeconds) {
    this.eventsInterval = TimeUnit.SECONDS.toMillis(eventIntervalInSeconds);
    this.pullRequestInterval = TimeUnit.SECONDS.toMillis(pullRequestIntervalInSeconds);

    this.eventService = eventService;
    this.prService = prService;
    this.taskScheduler = taskScheduler;

    configurationService.addConfigurationListener(this);
    schedulePulls(configurationService.load());
  }

  public void updateAllEvents() {
    for (Event e : eventService.getEvents()) {
      System.out.println(e.getId() + " - " + e.getType());
    }
  }

  public void updateAllPullRequests() {
    try {
      for (PullRequest pr : prService.getPullRequests()) {
        LOGGER.debug("Found PR: {}", pr.getTitle());
      }
    } catch (IOException e) {
      LOGGER.error("Error while updating all pull requests.", e);
    }
  }

  @Override
  public void configurationChanged(Optional<Configuration> configuration) {
    schedulePulls(configuration);
  }

  private void schedulePulls(Optional<Configuration> configuration) {
    if (configuration.isPresent() && configuration.get().isValid()) {
      pullRequestPullFuture = taskScheduler.scheduleAtFixedRate(this::updateAllPullRequests, pullRequestInterval);
      eventPullFuture = taskScheduler.scheduleAtFixedRate(this::updateAllEvents, eventsInterval);
    } else {
      if (eventPullFuture != null) {
        eventPullFuture.cancel(false);
      }
      if (pullRequestPullFuture != null) {
        pullRequestPullFuture.cancel(false);
      }
    }
  }

}
