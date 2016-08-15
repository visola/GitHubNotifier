package com.github.visola.githubnotifier.service;

import java.util.List;
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
  private final RepositoryRepository repositoryRepository;

  @Autowired
  public EventService(EventRepository eventRepository, GitHubClient gitHubClient, RepositoryRepository repositoryRepository) {
    this.eventRepository = eventRepository;
    this.gitHubClient = gitHubClient;
    this.repositoryRepository = repositoryRepository;
  }

  public List<Event> getEvents() {
    Set<String> repositoryFullNames = StreamSupport.stream(repositoryRepository.findAll().spliterator(), false)
        .map(Repository::getFullName)
        .collect(Collectors.toSet());

    List<Event> allEvents = gitHubClient.getEvents(repositoryFullNames);
    eventRepository.save(allEvents);
    return allEvents;
  }

}
