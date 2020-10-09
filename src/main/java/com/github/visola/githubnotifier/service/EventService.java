package com.github.visola.githubnotifier.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.github.visola.githubnotifier.data.EventRepository;
import com.github.visola.githubnotifier.data.GitHubClient;
import com.github.visola.githubnotifier.data.RepositoryRepository;
import com.github.visola.githubnotifier.model.Event;
import com.github.visola.githubnotifier.model.Repository;
import com.google.common.base.Throwables;

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
    for (Event e : allEvents) {
      try {
        eventRepository.save(e);
      } catch (JpaObjectRetrievalFailureException exception) {
        if (Throwables.getRootCause(exception) instanceof EntityNotFoundException) {
          // Event has some relationship with objects we don't care about
          continue;
        }
      }
    }
    return allEvents;
  }

}
