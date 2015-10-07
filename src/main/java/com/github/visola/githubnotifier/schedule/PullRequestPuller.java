package com.github.visola.githubnotifier.schedule;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.service.PullRequestService;
import com.github.visola.githubnotifier.ui.PullRequestMenuManager;
import com.github.visola.githubnotifier.ui.SystemTrayManager;

@Component
public class PullRequestPuller {

  private static final Logger LOG = LoggerFactory.getLogger(PullRequestPuller.class);

  private final SystemTrayManager tray;
  private final PullRequestService prService;
  private final PullRequestMenuManager prMenuManager;

  private Boolean updatingAll = false;

  @Autowired
  public PullRequestPuller(SystemTrayManager tray, PullRequestMenuManager prMenuManager, PullRequestService prService) {
    this.tray = tray;
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
        if (System.currentTimeMillis() - pr.getUpdatedAt().getTimeInMillis() < TimeUnit.MINUTES.toMillis(2)) {
          tray.showNotification(pr.getTitle(), "Pull Request updated");
        }
      }
      prMenuManager.updatePullRequests();
    }
  }

  @Scheduled(fixedDelay=10 * 60 * 1000)
  public void updateAllPullRequests() {
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
