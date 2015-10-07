package com.github.visola.githubnotifier.ui;

import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.service.PullRequestService;

import humanize.Humanize;

@Component
public class PullRequestMenuManager {

  private static final Logger LOG = LoggerFactory.getLogger(PullRequestMenuManager.class);

  private final PullRequestService pullRequestService;

  private List<Runnable> pullRequestUpdateListeners = new ArrayList<>();
  private List<MenuItem> pullRequestMenus = new ArrayList<>();

  @Autowired
  public PullRequestMenuManager(PullRequestService pullRequestService) {
    this.pullRequestService = pullRequestService;
  }

  public void addPullRequestUpdateListener(Runnable r) {
    this.pullRequestUpdateListeners.add(r);
  }

  public List<MenuItem> getPullRequestMenuItems() {
    return Collections.unmodifiableList(pullRequestMenus);
  }

  public void updatePullRequests() {
    pullRequestMenus.forEach(mi -> mi.getParent().remove(mi));
    pullRequestMenus.clear();

    pullRequestMenus.add(new MenuItem("-"));

    for (PullRequest pr : pullRequestService.getOpenPullRequests()) {
      MenuItem prMenu = new MenuItem(pr.getTitle() + " - " + Humanize.naturalTime(new Date(), pr.getUpdatedAt().getTime()));
      prMenu.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try {
            Desktop.getDesktop().browse(new URI(pr.getHtmlUrl()));
          } catch (IOException | URISyntaxException ex) {
            LOG.error("Error while navigating to PR URL", ex);
          }
        }
      });
      pullRequestMenus.add(prMenu);
    }

    pullRequestUpdateListeners.forEach(r -> r.run());
  }

}
