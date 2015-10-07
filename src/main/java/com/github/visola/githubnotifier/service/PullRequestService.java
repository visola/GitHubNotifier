package com.github.visola.githubnotifier.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.visola.githubnotifier.data.GitHubClient;
import com.github.visola.githubnotifier.data.PullRequestRepository;
import com.github.visola.githubnotifier.data.RepositoryRepository;
import com.github.visola.githubnotifier.data.UserRepository;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.model.Repository;

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

  public List<PullRequest> getPullRequests() {
    List<PullRequest> pullRequests = gitClient.getPullRequests(StreamSupport.stream(repositoryRepository.findAll().spliterator(), false)
        .map(Repository::getFullName).collect(Collectors.toList()));

    for (PullRequest pr : pullRequests) {
      save(pr);
    }

    return pullRequests;
  }

  public void save(PullRequest pr) {
    pr.getBase().setUser(userRepository.save(pr.getBase().getUser()));
    pr.getHead().setUser(userRepository.save(pr.getHead().getUser()));
    repositoryRepository.save(pr.getBase().getRepository());
    repositoryRepository.save(pr.getHead().getRepository());
    pullRequestRepository.save(pr);
  }

  public List<PullRequest> getOpenPullRequests() {
    return pullRequestRepository.findByStateOrderByUpdatedAtDesc("open");
  }

}
