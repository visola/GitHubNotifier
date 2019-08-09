package com.github.visola.githubnotifier.ui;

import com.github.visola.githubnotifier.event.PullRequestsEvent;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.service.PullRequestService;
import humanize.Humanize;
import java.awt.Desktop;
import java.awt.Menu;
import java.awt.MenuItem;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PullRequestMenuManager {

  private static final Logger LOG = LoggerFactory.getLogger(PullRequestMenuManager.class);

  private final Menu pullRequestsMenu = new Menu("Pull Requests");
  private final PullRequestService pullRequestService;

  @Autowired
  public PullRequestMenuManager(PullRequestService pullRequestService) {
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
      try {
        Desktop.getDesktop().browse(new URI(pullRequest.getHtmlUrl()));
      } catch (IOException | URISyntaxException ex) {
        LOG.error("Error while navigating to PR URL", ex);
      }
    });

    return pullRequestMenu;
  }

  private void updatePullRequests() {
    pullRequestsMenu.removeAll();
    pullRequestsMenu.add(new MenuItem("-"));

    pullRequestService.getOpenPullRequests().stream()
        .map(this::createPullRequestMenuItem)
        .forEach(pullRequestsMenu::add);
  }

}
