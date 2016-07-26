package com.github.visola.githubnotifier.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.visola.githubnotifier.data.GitHubClient;
import com.github.visola.githubnotifier.data.PullRequestRepository;
import com.github.visola.githubnotifier.data.RepositoryRepository;
import com.github.visola.githubnotifier.data.UserRepository;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.model.Repository;

@Lazy
@Service
public class PullRequestService {

  private final GitHubClient gitClient;
  private final PullRequestRepository pullRequestRepository;
  private final RepositoryRepository repositoryRepository;
  private final UserRepository userRepository;

  @Autowired
  public PullRequestService(GitHubClient gitClient,
                            PullRequestRepository pullRequestRepository,
                            RepositoryRepository repositoryRepository,
                            UserRepository userRepository) {
    this.gitClient = gitClient;
    this.pullRequestRepository = pullRequestRepository;
    this.repositoryRepository = repositoryRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public List<PullRequest> getPullRequests() throws IOException {
    Set<String> repoFullNames = StreamSupport.stream(repositoryRepository.findAll().spliterator(), false)
        .map(Repository::getFullName)
        .collect(Collectors.toSet());

    // Mark all PRs as closed
    repoFullNames.forEach(pullRequestRepository::closePullRequests);

    // Load only PRs that are open
    List<PullRequest> pullRequests = gitClient.getPullRequests(repoFullNames);

    for (PullRequest pr : pullRequests) {
      save(pr);
    }

    return pullRequests;
  }

  public void save(PullRequest pr) {
    pr.getBase().setUser(userRepository.save(pr.getBase().getUser()));
    pr.getHead().setUser(userRepository.save(pr.getHead().getUser()));
    if (pr.getBase().getRepository() != null) {
      repositoryRepository.save(pr.getBase().getRepository());
    }
    if (pr.getHead().getRepository() != null) {
      repositoryRepository.save(pr.getHead().getRepository());
    }
    pullRequestRepository.save(pr);
  }

  public List<PullRequest> getOpenPullRequests() {
    return pullRequestRepository.findByStateOrderByUpdatedAtDesc("open");
  }

}
