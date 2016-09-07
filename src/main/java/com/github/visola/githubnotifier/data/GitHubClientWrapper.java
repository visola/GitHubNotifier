package com.github.visola.githubnotifier.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.service.ConfigurationService;

@Component
public class GitHubClientWrapper {

  private final Optional<Configuration> configuration;
  private final GitHubClient gitHubClient;
  private final PullRequestService pullRequestService;
  private final RepositoryService repositoryService;

  private final Map<String, Repository> repositoryCache = new HashMap<>();

  @Autowired
  public GitHubClientWrapper(ConfigurationService configurationService) {
    this.configuration = configurationService.getConfiguration();

   this.gitHubClient = new GitHubClient(configuration.get().getGithubUrl());
   this.gitHubClient.setCredentials(this.configuration.get().getUsername(), this.configuration.get().getPassword());
   
   pullRequestService = new PullRequestService(gitHubClient);
   repositoryService = new RepositoryService(gitHubClient);
  }

  public List<com.github.visola.githubnotifier.model.PullRequest> getPullRequests(Set<String> repoFullNames) throws IOException {
    checkConfiguration();

    List<com.github.visola.githubnotifier.model.PullRequest> allPullRequests = new ArrayList<>();
    for (String repoFullName : repoFullNames) {
      if (repoFullName.startsWith("null/")) {
        continue;
      }

      pullRequestService.getPullRequests(fromFullName(repoFullName), "open").stream()
        .map(com.github.visola.githubnotifier.model.PullRequest::new)
        .forEach(allPullRequests::add);
    }

    return allPullRequests;
  }

  public PullRequest getPullRequest(String repoFullName, int id) throws IOException {
    checkConfiguration();

    String [] split = repoFullName.split("/");
    String owner = split[0];
    String repoName = split[1];

    Repository repo = repositoryService.getRepository(owner, repoName);

    PullRequestService prService = new PullRequestService(gitHubClient);
    return prService.getPullRequest(repo, id);
  }

  private void checkConfiguration() {
    if (!configuration.isPresent()) {
      throw new RuntimeException("Configuration not set.");
    }
  }

  private Repository fromFullName(String repoFullName) {
    return repositoryCache.computeIfAbsent(repoFullName, rfn -> {
      String [] split = rfn.split("/");
      String owner = split[0];
      String repoName = split[1];
      try {
        return repositoryService.getRepository(owner, repoName);
      } catch (IOException ioe) {
        throw new RuntimeException("Error while fetching repository.", ioe);
      }
    });
  }

}
