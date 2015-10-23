package com.github.visola.githubnotifier.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.visola.githubnotifier.model.Notification;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, Integer> {

}
