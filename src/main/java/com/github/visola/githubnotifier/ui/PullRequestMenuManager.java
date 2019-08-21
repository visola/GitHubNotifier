package com.github.visola.githubnotifier.ui;

import com.github.visola.githubnotifier.event.PullRequestsEvent;
import com.github.visola.githubnotifier.event.PullRequestsSaved;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.service.PullRequestService;
import humanize.Humanize;
import java.awt.Menu;
import java.awt.MenuItem;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PullRequestMenuManager {

  private static final Logger LOG = LoggerFactory.getLogger(PullRequestMenuManager.class);

  private final Menu pullRequestsMenu = new Menu("Pull Requests");
  private final ApplicationEventPublisher eventPublisher;
  private final PullRequestService pullRequestService;

  @Autowired
  public PullRequestMenuManager(
      ApplicationEventPublisher eventPublisher,
      PullRequestService pullRequestService) {
    this.eventPublisher = eventPublisher;
    this.pullRequestService = pullRequestService;
  }

  public Menu getPullRequestsMenu() {
    return pullRequestsMenu;
  }

  @EventListener
  public void pullRequestsChanged(PullRequestsEvent event) {
    updatePullRequests();
  }

  private MenuItem createPullRequestMenuItem(PullRequest pullRequest) {
    String text = String.format("%s - %s - %s",
        pullRequest.getBase().getRepository().getName(),
        pullRequest.getTitle(),
        Humanize.naturalTime(new Date(), pullRequest.getUpdatedAt().getTime()));
    MenuItem pullRequestMenu = new MenuItem(text);
    pullRequestMenu.addActionListener((e) -> {
      eventPublisher.publishEvent(new PullRequestsSaved(Arrays.asList(pullRequest)));
//      try {
//        Desktop.getDesktop().browse(new URI(pullRequest.getHtmlUrl()));
//      } catch (IOException | URISyntaxException ex) {
//        LOG.error("Error while navigating to PR URL", ex);
//      }
    });

    return pullRequestMenu;
  }

  private void updatePullRequests() {
    pullRequestsMenu.removeAll();

    List<PullRequest> openPullRequests = pullRequestService.getOpenPullRequests();
    if (openPullRequests.isEmpty()) {
      pullRequestsMenu.add(new MenuItem("No Open Pull Requests"));
    } else {
      openPullRequests.stream()
          .map(this::createPullRequestMenuItem)
          .forEach(pullRequestsMenu::add);
    }
  }

}
