package com.github.visola.githubnotifier.puller;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.event.ConfigurationEvent;
import com.github.visola.githubnotifier.service.PullRequestService;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class PullRequestPuller {

  private static final Logger LOGGER = LoggerFactory.getLogger(PullRequestPuller.class);

  private final PullRequestService prService;
  private final long pullRequestInterval;
  private ScheduledFuture<?> pullRequestPullFuture;
  private final TaskScheduler taskScheduler;

  public PullRequestPuller(PullRequestService prService,
      @Value("${pull.schedule.pull-requests}") int pullRequestIntervalInSeconds,
      TaskScheduler taskScheduler) {
    this.pullRequestInterval = TimeUnit.SECONDS.toMillis(pullRequestIntervalInSeconds);
    this.prService = prService;
    this.taskScheduler = taskScheduler;
  }

  @EventListener
  public void configurationChangedOrLoaded(ConfigurationEvent event) {
    Optional<Configuration> configuration = event.getConfiguration();
    if (configuration.isPresent() && configuration.get().isValid()) {
      pullRequestPullFuture = taskScheduler.scheduleAtFixedRate(this::updateAllPullRequests, pullRequestInterval);
      return;
    }

    if (pullRequestPullFuture != null) {
      pullRequestPullFuture.cancel(false);
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

}
