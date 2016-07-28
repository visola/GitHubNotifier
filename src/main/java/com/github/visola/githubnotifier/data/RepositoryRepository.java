package com.github.visola.githubnotifier.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.github.visola.githubnotifier.model.Repository;

public interface RepositoryRepository extends PagingAndSortingRepository<Repository, Long>{

  @Transactional
  void deleteByName(String name);

  @Query("SELECT r FROM Repository r ORDER BY r.fullName")
  List<Repository> findAllOrderByFullName();

  Optional<Repository> findByFullName(String fullName);

}
