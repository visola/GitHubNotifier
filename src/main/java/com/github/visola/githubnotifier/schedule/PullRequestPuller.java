package com.github.visola.githubnotifier.schedule;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.service.PullRequestService;
import com.github.visola.githubnotifier.ui.PullRequestMenuManager;

@Component
@Lazy
public class PullRequestPuller {

  private static final Logger LOG = LoggerFactory.getLogger(PullRequestPuller.class);

  private final PullRequestService prService;
  private final PullRequestMenuManager prMenuManager;

  private Boolean updatingAll = false;

  @Autowired
  public PullRequestPuller(PullRequestMenuManager prMenuManager,
                           PullRequestService prService) {
    this.prMenuManager = prMenuManager;
    this.prService = prService;
  }

  @Scheduled(fixedDelay=30 * 1000)
  public void checkOpenPullRequests() {
    if (updatingAll == true) {
      return;
    }

    synchronized (updatingAll) {
      for (PullRequest pr : prService.getOpenPullRequests()) {
        prService.save(pr);
        LOG.debug("Checking PR: {}", pr.getTitle());
        Optional<Notification> maybeNotification = notificationRepository.findById(pr.getId());
        if (!maybeNotification.isPresent() || pr.getUpdatedAt().after(maybeNotification.get().getLastNotification())) {
          //tray.showNotification(pr.getTitle(), "Pull Request updated", pr.getHtmlUrl());

          Notification notification = new Notification();
          notification.setId(pr.getId());
          notification.setLastNotification(pr.getUpdatedAt());
          notificationRepository.save(notification);
        }
      }
      prMenuManager.updatePullRequests();
    }
  }

  @Scheduled(fixedDelay=10 * 60 * 1000)
  public void updateAllPullRequests() throws IOException {
    synchronized (updatingAll) {
      updatingAll = true;
      for (PullRequest pr : prService.getPullRequests()) {
        LOG.debug("Found PR: {}", pr.getTitle());
      }
      updatingAll = false;
      prMenuManager.updatePullRequests();
    }
  }

}
