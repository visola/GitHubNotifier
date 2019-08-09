package com.github.visola.githubnotifier.event;

import com.github.visola.githubnotifier.model.Repository;

public abstract class RepositoryEvent {

  private final Repository repository;

  public RepositoryEvent(Repository repository) {
    this.repository = repository;
  }

  public Repository getRepository() {
    return repository;
  }

}
