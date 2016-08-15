package com.github.visola.githubnotifier.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.visola.githubnotifier.model.Event;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

}
