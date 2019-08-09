package com.github.visola.githubnotifier.event;

import com.github.visola.githubnotifier.model.Repository;

public class RepositorySaved extends RepositoryEvent {

  public RepositorySaved(Repository repository) {
    super(repository);
  }

}
