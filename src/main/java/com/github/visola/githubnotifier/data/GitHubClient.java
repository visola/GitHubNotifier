package com.github.visola.githubnotifier.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.model.Event;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.model.Repository;
import com.github.visola.githubnotifier.service.ConfigurationEvent;

@Service
public class GitHubClient {

  private static final Logger LOG = LoggerFactory.getLogger(GitHubClient.class);

  private final RestTemplate restTemplate;

  private Optional<Configuration> configuration = Optional.empty();

  @Autowired
  public GitHubClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public PullRequest getPullRequest(String repoFullName, int id) {
    checkConfiguration();
    return restTemplate.exchange(configuration.get().getApiBase()+"/repos/"+repoFullName+"/pulls/"+id, HttpMethod.GET, createHttpEntity(), PullRequest.class).getBody();
  }

  public List<PullRequest> getPullRequests(Collection<String> repositoryFullNames) {
    checkConfiguration();
    LOG.debug("Fetching PR data...");
    List<PullRequest> pullRequestsResult = new ArrayList<>();
    for (String fullName: repositoryFullNames) {
      try {
        LOG.trace("Fetching PRs for repo {}", fullName);
        List<PullRequest> pullRequests = Arrays.asList(restTemplate.exchange(configuration.get().getApiBase()+"/repos/"+fullName+"/pulls", HttpMethod.GET, createHttpEntity(), PullRequest[].class).getBody());
        pullRequestsResult.addAll(pullRequests);
      } catch (Exception e) {
        LOG.error("Error while fetching data from repository {}.", fullName, e);
      }
    }
    return pullRequestsResult;
  }

  public Optional<Repository> getRepoByFullNAme(String fullName) {
    return executeGet("/repos/"+fullName, Repository.class);
  }

  @EventListener
  public void configurationChangedOrLoaded(ConfigurationEvent event) {
    this.configuration =  event.getConfiguration();
  }

  public List<Event> getEvents(Set<String> repositoryFullNames) {
    List<Event> result = new ArrayList<>();
    for (String fullName : repositoryFullNames) {
      List<Event> events = Arrays.asList(restTemplate.exchange(configuration.get().getApiBase()+"/repos/"+fullName+"/events", HttpMethod.GET, createHttpEntity(), Event[].class).getBody());
      result.addAll(events);
    }
    return result;
  }

  private <T> Optional<T> executeGet(String path, Class<T> type) {
    try {
      return Optional.of(restTemplate.exchange(configuration.get().getApiBase()+path, HttpMethod.GET, createHttpEntity(), type).getBody());
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return Optional.empty();
      }
      throw e;
    }
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
