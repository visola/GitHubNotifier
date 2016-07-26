package com.github.visola.githubnotifier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.visola.githubnotifier.data.PullRequestRepository;
import com.github.visola.githubnotifier.data.RepositoryRepository;
import com.github.visola.githubnotifier.model.Repository;

@Lazy
@Service
public class RepositoryService {

  private final PullRequestRepository pullRequestRepository;
  private final RepositoryRepository repoRepository;

  @Autowired
  public RepositoryService(PullRequestRepository pullRequestRepository, RepositoryRepository repoRepository) {
    this.pullRequestRepository = pullRequestRepository;
    this.repoRepository = repoRepository;
  }

  public List<Repository> findAllOrderByFullName() {
    return repoRepository.findAllOrderByFullName();
  }

  @Transactional
  public void deleteByName(String name) {
    pullRequestRepository.deleteByBaseRepositoryName(name);
    repoRepository.deleteByName(name);
  }

}
