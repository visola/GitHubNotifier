package com.github.visola.githubnotifier.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.model.Event;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.model.Repository;
import com.github.visola.githubnotifier.event.ConfigurationEvent;

@Service
public class GitHubClient {

  private static final Logger LOG = LoggerFactory.getLogger(GitHubClient.class);

  private Optional<Configuration> configuration = Optional.empty();
  private Map<String, String> eventsETagsByRepositoryName = new HashMap<>();
  private final RestTemplate restTemplate;

  @Autowired
  public GitHubClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public PullRequest getPullRequest(String repoFullName, int id) {
    checkConfiguration();
    return restTemplate.exchange(
        configuration.get().getApiBase() + "/repos/" + repoFullName + "/pulls/" + id,
        HttpMethod.GET,
        createHttpEntity(),
        PullRequest.class
    ).getBody();
  }

  public List<PullRequest> getPullRequests(Collection<String> repositoryFullNames) {
    checkConfiguration();
    LOG.debug("Fetching PR data...");
    List<PullRequest> pullRequestsResult = new ArrayList<>();
    for (String fullName : repositoryFullNames) {
      try {
        LOG.trace("Fetching PRs for repo {}", fullName);
        List<PullRequest> pullRequests = restTemplate.exchange(
            configuration.get().getApiBase() + "/repos/" + fullName + "/pulls",
            HttpMethod.GET,
            createHttpEntity(),
            new ParameterizedTypeReference<List<PullRequest>>() {
            }
        ).getBody();
        pullRequestsResult.addAll(pullRequests);
      } catch (Exception e) {
        LOG.error("Error while fetching data from repository {}.", fullName, e);
      }
    }
    return pullRequestsResult;
  }

  public Optional<Repository> getRepoByFullNAme(String fullName) {
    return executeGet("/repos/" + fullName, Repository.class);
  }

  @EventListener
  public void configurationChangedOrLoaded(ConfigurationEvent event) {
    this.configuration = event.getConfiguration();
  }

  public List<Event> getEvents(Set<String> repositoryFullNames) {
    List<Event> result = new ArrayList<>();
    for (String fullName : repositoryFullNames) {
      HttpEntity<Void> entity;
      if (eventsETagsByRepositoryName.containsKey(fullName)) {
        entity = createHttpEntity("If-None-Match", eventsETagsByRepositoryName.get(fullName));
      } else {
        entity = createHttpEntity();
      }

      ResponseEntity<List<Event>> response = restTemplate.exchange(
          configuration.get().getApiBase() + "/repos/" + fullName + "/events",
          HttpMethod.GET,
          entity,
          new ParameterizedTypeReference<List<Event>>() {}
      );

      if (HttpStatus.NOT_MODIFIED.equals(response.getStatusCode())) {
        continue;
      }

      if (response.getHeaders().containsKey("ETag")) {
        eventsETagsByRepositoryName.put(fullName, response.getHeaders().get("ETag").get(0));
      }

      List<Event> events = response.getBody();
      if (events == null) {
        throw new RuntimeException("Events body is empty for repo: " + fullName);
      } else {
        result.addAll(events);
      }
    }
    return result;
  }

  private <T> Optional<T> executeGet(String path, Class<T> type) {
    try {
      return Optional.ofNullable(
          restTemplate.exchange(
              configuration.get().getApiBase() + path,
              HttpMethod.GET,
              createHttpEntity(),
              type
          ).getBody()
      );
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return Optional.empty();
      }
      throw e;
    }
  }

  private HttpEntity<Void> createHttpEntity(String... extraHeaders) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    for (int i = 0; i < extraHeaders.length; i += 2) {
      headers.add(extraHeaders[i], extraHeaders[i + 1]);
    }

    Configuration config = configuration.get();
    if (config.isToken()) {
      headers.add("Authorization", "Token " + config.getPassword());
    } else {
      String usernamePassword = config.getUsername() + ":" + config.getPassword();
      headers.add(
          "Authorization",
          "Basic " + Base64.getEncoder().encodeToString(usernamePassword.getBytes())
      );
    }

    HttpEntity<Void> entity = new HttpEntity<>(headers);
    return entity;
  }

  private void checkConfiguration() {
    if (!configuration.isPresent()) {
      throw new RuntimeException("Configuration not set.");
    }
  }

}
