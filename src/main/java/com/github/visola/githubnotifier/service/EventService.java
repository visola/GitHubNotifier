package com.github.visola.githubnotifier.service;

import com.github.visola.githubnotifier.data.PullRequestRepository;
import com.github.visola.githubnotifier.model.EventType;
import com.github.visola.githubnotifier.model.PullRequest;
import com.github.visola.githubnotifier.model.PullRequestEventPayload;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.visola.githubnotifier.data.EventRepository;
import com.github.visola.githubnotifier.data.GitHubClient;
import com.github.visola.githubnotifier.data.RepositoryRepository;
import com.github.visola.githubnotifier.model.Event;
import com.github.visola.githubnotifier.model.Repository;

@Service
public class EventService {

  private final EventRepository eventRepository;
  private final GitHubClient gitHubClient;
  private final PullRequestRepository pullRequestRepository;
  private final RepositoryRepository repositoryRepository;

  @Autowired
  public EventService(EventRepository eventRepository, GitHubClient gitHubClient,
      PullRequestRepository pullRequestRepository,
      RepositoryRepository repositoryRepository) {
    this.eventRepository = eventRepository;
    this.gitHubClient = gitHubClient;
    this.pullRequestRepository = pullRequestRepository;
    this.repositoryRepository = repositoryRepository;
  }

  public List<Event> getEvents() {
    Set<String> repositoryFullNames = StreamSupport.stream(repositoryRepository.findAll().spliterator(), false)
        .map(Repository::getFullName)
        .collect(Collectors.toSet());

    List<Event> allEvents = gitHubClient.getEvents(repositoryFullNames);
    for (Event e : allEvents) {
      if (e.getType() == EventType.PullRequestEvent) {
        ensurePullRequest((PullRequestEventPayload) e.getPayload());
      }
      eventRepository.save(e);
    }
    return allEvents;
  }

  private void ensurePullRequest(PullRequestEventPayload eventPayload) {
    Optional<PullRequest> pr = pullRequestRepository.findById(eventPayload.getPullRequest().getId());
    if (!pr.isPresent()) {
      pullRequestRepository.save(eventPayload.getPullRequest());
    }
  }

}
