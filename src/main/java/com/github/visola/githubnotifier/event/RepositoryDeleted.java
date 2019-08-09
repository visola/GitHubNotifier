package com.github.visola.githubnotifier.event;

import com.github.visola.githubnotifier.model.Repository;

public class RepositoryDeleted extends RepositoryEvent {

  public RepositoryDeleted(Repository repository) {
    super(repository);
  }

}
