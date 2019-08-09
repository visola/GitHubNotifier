package com.github.visola.githubnotifier.event;

import com.github.visola.githubnotifier.model.PullRequest;
import java.util.List;

public abstract class PullRequestsEvent {

  private final List<PullRequest> pullRequests;

  protected PullRequestsEvent(List<PullRequest> pullRequests) {
    this.pullRequests = pullRequests;
  }

  public List<PullRequest> getPullRequests() {
    return pullRequests;
  }

}
