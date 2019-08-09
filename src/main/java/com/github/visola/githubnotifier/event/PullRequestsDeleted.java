package com.github.visola.githubnotifier.event;

import com.github.visola.githubnotifier.model.PullRequest;
import java.util.List;

public class PullRequestsDeleted extends PullRequestsEvent {

  public PullRequestsDeleted(List<PullRequest> pullRequests) {
    super(pullRequests);
  }

}
