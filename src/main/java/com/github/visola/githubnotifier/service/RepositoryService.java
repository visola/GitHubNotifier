package com.github.visola.githubnotifier.service;

import com.github.visola.githubnotifier.data.GitHubClient;
import com.github.visola.githubnotifier.data.PullRequestRepository;
import com.github.visola.githubnotifier.data.RepositoryRepository;
import com.github.visola.githubnotifier.event.PullRequestsDeleted;
import com.github.visola.githubnotifier.event.RepositoryDeleted;
import com.github.visola.githubnotifier.event.RepositorySaved;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.model.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class RepositoryService {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final GitHubClient gitHubClient;
  private final PullRequestRepository pullRequestRepository;
  private final RepositoryRepository repoRepository;

  @Autowired
  public RepositoryService(
      ApplicationEventPublisher applicationEventPublisher,
      GitHubClient gitHubClient, PullRequestRepository pullRequestRepository,
      RepositoryRepository repoRepository) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.gitHubClient = gitHubClient;
    this.pullRequestRepository = pullRequestRepository;
    this.repoRepository = repoRepository;
  }

  public List<Repository> findAllOrderByFullName() {
    return repoRepository.findAllOrderByFullName();
  }

  @Transactional
  public void deleteByName(String fullName) {
    Optional<Repository> maybeRepository = repoRepository.findByFullName(fullName);
    if (!maybeRepository.isPresent()) {
      return;
    }
    Repository repository = maybeRepository.get();

    List<PullRequest> deletePullRequests = pullRequestRepository
        .findByBaseRepositoryFullName(fullName);
    pullRequestRepository.deleteByBaseRepositoryFullName(fullName);
    pullRequestRepository.flush();

    repoRepository.deleteByFullName(fullName);
    repoRepository.flush();

    applicationEventPublisher.publishEvent(new PullRequestsDeleted(deletePullRequests));
    applicationEventPublisher.publishEvent(new RepositoryDeleted(repository));
  }

  public Optional<Repository> findByFullName(String fullName) {
    Optional<Repository> repo = repoRepository.findByFullName(fullName);
    if (!repo.isPresent()) {
      return gitHubClient.getRepoByFullNAme(fullName);
    }
    return repo;
  }

  public void save(Repository repository) {
    repoRepository.saveAndFlush(repository);
    applicationEventPublisher.publishEvent(new RepositorySaved(repository));
  }

}
