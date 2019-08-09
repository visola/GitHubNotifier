package com.github.visola.githubnotifier.event;

import com.github.visola.githubnotifier.model.PullRequest;
import java.util.List;

public class PullRequestsSaved extends PullRequestsEvent {

  public PullRequestsSaved(List<PullRequest> pullRequests) {
    super(pullRequests);
  }

}
