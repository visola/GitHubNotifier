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
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.service.ConfigurationListener;
import com.github.visola.githubnotifier.service.ConfigurationService;
import com.github.visola.githubnotifier.service.PullRequestService;

@Component
@Lazy
public class EventPuller implements ConfigurationListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventPuller.class);

  private final PullRequestService prService;
  private final TaskScheduler taskScheduler;

  private ScheduledFuture<?> eventPullFuture;
  private ScheduledFuture<?> pullRequestPullFuture;

  private final long pullRequestInterval;

  private Boolean updatingAll = false;

  @Autowired
  public EventPuller(ConfigurationService configurationService,
                     PullRequestService prService,
                     TaskScheduler taskScheduler,
                     @Value("${pull.schedule.pull-requests}") int pullRequestIntervalInSeconds) {
    this.prService = prService;
    this.pullRequestInterval = TimeUnit.SECONDS.toMillis(pullRequestIntervalInSeconds);
    this.taskScheduler = taskScheduler;

    configurationService.addConfigurationListener(this);
    schedulePulls(configurationService.load());
  }

  public void checkOpenPullRequests() {
    if (updatingAll == true) {
      return;
    }

    synchronized (updatingAll) {
      for (PullRequest pr : prService.getOpenPullRequests()) {
        prService.save(pr);
        LOGGER.debug("Checking PR: {}", pr.getTitle());
      }
    }
  }

  public void updateAllPullRequests() {
    try {
      synchronized (updatingAll) {
        updatingAll = true;
          for (PullRequest pr : prService.getPullRequests()) {
            LOGGER.debug("Found PR: {}", pr.getTitle());
          }
        updatingAll = false;
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
