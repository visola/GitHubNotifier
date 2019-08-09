package com.github.visola.githubnotifier.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PullRequestEventPayload extends EventPayload {

  public static enum PullRequestEventAction {
    ASSIGNED,
    CLOSED,
    EDITED,
    LABELED,
    OPENED,
    REOPENED,
    SYNCHRONIZE,
    UNASSIGNED,
    UNLABELED,
    ;

    @JsonCreator
    public static PullRequestEventAction valueOfIgnoreCase(String value) {
      return PullRequestEventAction.valueOf(value.toUpperCase());
    }
  }

  private static final long serialVersionUID = 1L;

  @Enumerated(EnumType.STRING)
  private PullRequestEventAction action;

  @ManyToOne
  private PullRequest pullRequest;

  public PullRequestEventAction getAction() {
    return action;
  }

  public void setAction(PullRequestEventAction action) {
    this.action = action;
  }

  public PullRequest getPullRequest() {
    return pullRequest;
  }

  public void setPullRequest(PullRequest pullRequest) {
    this.pullRequest = pullRequest;
  }

}
