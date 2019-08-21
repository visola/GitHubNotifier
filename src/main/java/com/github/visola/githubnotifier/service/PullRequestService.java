package com.github.visola.githubnotifier.service;

import com.github.visola.githubnotifier.data.GitHubClient;
import com.github.visola.githubnotifier.data.PullRequestRepository;
import com.github.visola.githubnotifier.data.RepositoryRepository;
import com.github.visola.githubnotifier.data.UserRepository;
import com.github.visola.githubnotifier.event.PullRequestsSaved;
import com.github.visola.githubnotifier.model.Commit;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.model.Repository;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class PullRequestService {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final GitHubClient gitClient;
  private final PullRequestRepository pullRequestRepository;
  private final RepositoryRepository repositoryRepository;
  private final UserRepository userRepository;

  @Autowired
  public PullRequestService(
      ApplicationEventPublisher applicationEventPublisher,
      GitHubClient gitClient,
      PullRequestRepository pullRequestRepository,
      RepositoryRepository repositoryRepository,
      UserRepository userRepository) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.gitClient = gitClient;
    this.pullRequestRepository = pullRequestRepository;
    this.repositoryRepository = repositoryRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public List<PullRequest> getPullRequests() throws IOException {
    Set<String> repoFullNames = StreamSupport
        .stream(repositoryRepository.findAll().spliterator(), false)
        .map(Repository::getFullName)
        .collect(Collectors.toSet());

    List<PullRequest> pullRequests = gitClient.getPullRequests(repoFullNames);
    // API will only return the open PRs, so we mark all existing as closed and then
    // save the ones that came from the API. This will re-open the closed ones.
    // TODO - Find PRs that didn't come back and update them separated
    repositoryRepository.markAllAsClosed();
    save(pullRequests);
    return pullRequests;
  }

  @Transactional
  public void save(List<PullRequest> pullRequests) {
    List<Commit> commits = pullRequests.stream().map(pr -> pr.getBase()).collect(Collectors.toList());
    commits.addAll(pullRequests.stream().map(pr -> pr.getHead()).collect(Collectors.toList()));

    // Ensure all users exist
    userRepository.saveAll(commits.stream().map(Commit::getUser).collect(Collectors.toSet()));
    userRepository.flush();

    repositoryRepository.saveAll(commits.stream().map(Commit::getRepository)
        .collect(Collectors.toSet()));
    repositoryRepository.flush();

    pullRequestRepository.saveAll(pullRequests);
    applicationEventPublisher.publishEvent(new PullRequestsSaved(pullRequests));
  }

  public List<PullRequest> getOpenPullRequests() {
    return pullRequestRepository.findByStateOrderByUpdatedAtDesc("open");
  }

}
