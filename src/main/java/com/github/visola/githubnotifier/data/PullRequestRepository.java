package com.github.visola.githubnotifier.data;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.visola.githubnotifier.model.PullRequest;

public interface PullRequestRepository extends PagingAndSortingRepository<PullRequest, Integer>{

  List<PullRequest> findByStateOrderByUpdatedAtDesc(String state);

}
