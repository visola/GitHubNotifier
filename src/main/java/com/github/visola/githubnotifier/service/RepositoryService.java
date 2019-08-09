package com.github.visola.githubnotifier.service;

import com.github.visola.githubnotifier.data.GitHubClient;
import com.github.visola.githubnotifier.data.PullRequestRepository;
import com.github.visola.githubnotifier.data.RepositoryRepository;
import com.github.visola.githubnotifier.model.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class RepositoryService {

  private final GitHubClient gitHubClient;
  private final PullRequestRepository pullRequestRepository;
  private final RepositoryRepository repoRepository;

  @Autowired
  public RepositoryService(GitHubClient gitHubClient, PullRequestRepository pullRequestRepository, RepositoryRepository repoRepository) {
    this.gitHubClient = gitHubClient;
    this.pullRequestRepository = pullRequestRepository;
    this.repoRepository = repoRepository;
  }

  public List<Repository> findAllOrderByFullName() {
    return repoRepository.findAllOrderByFullName();
  }

  @Transactional
  public void deleteByName(String name) {
    pullRequestRepository.deleteByBaseRepositoryName(name);
    repoRepository.deleteByName(name);
  }

  public Optional<Repository> findByFullName(String fullName) {
    Optional<Repository> repo = repoRepository.findByFullName(fullName);
    if (!repo.isPresent()) {
      return gitHubClient.getRepoByFullNAme(fullName);
    }
    return repo;
  }

  public void save(Repository repository) {
    repoRepository.save(repository);
  }

}
