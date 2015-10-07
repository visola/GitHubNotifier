package com.github.visola.githubnotifier.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.service.ConfigurationService;

@Service
public class GitHubClient {

  private static final Logger LOG = LoggerFactory.getLogger(GitHubClient.class);

  private static final String BASE_PATH = "/api/v3";

  private final RestTemplate restTemplate;
  private final Optional<Configuration> configuration;

  @Autowired
  public GitHubClient(ConfigurationService configurationService, RestTemplate restTemplate) {
    this.configuration = configurationService.getConfiguration();
    this.restTemplate = restTemplate;
  }

  public PullRequest getPullRequest(String repoFullName, int id) {
    checkConfiguration();
    return restTemplate.exchange(configuration.get().getGithubUrl()+BASE_PATH+"/repos/"+repoFullName+"/pulls/"+id, HttpMethod.GET, createHttpEntity(), PullRequest.class).getBody();
  }

  public List<PullRequest> getPullRequests(List<String> repositoryFullNames) {
    checkConfiguration();
    LOG.debug("Fetching PR data...");
    List<PullRequest> pullRequestsResult = new ArrayList<>();
    for (String fullName: repositoryFullNames) {
      LOG.trace("Fetching PRs for repo {}", fullName);
      List<PullRequest> pullRequests = Arrays.asList(restTemplate.exchange(configuration.get().getGithubUrl()+BASE_PATH+"/repos/"+fullName+"/pulls?state=all", HttpMethod.GET, createHttpEntity(), PullRequest[].class).getBody());
      pullRequestsResult.addAll(pullRequests);
    }
    return pullRequestsResult;
  }

  private HttpEntity<Void> createHttpEntity() {
    HttpHeaders headers = new HttpHeaders();
    String usernamePassword = configuration.get().getUsername()+":"+configuration.get().getPassword();
    headers.add("Authorization", "Basic "+Base64.getEncoder().encodeToString(usernamePassword.getBytes()));
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity<Void> entity = new HttpEntity<>(headers);
    return entity;
  }

  private void checkConfiguration() {
    if (!configuration.isPresent()) {
      throw new RuntimeException("Configuration not set.");
    }
  }

}
