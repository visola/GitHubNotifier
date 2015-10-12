package com.github.visola.githubnotifier.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.github.visola.githubnotifier.model.Repository;

public interface RepositoryRepository extends PagingAndSortingRepository<Repository, String>{

  @Transactional
  void deleteByName(String name);

}
