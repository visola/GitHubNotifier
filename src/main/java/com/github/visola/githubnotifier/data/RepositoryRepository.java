package com.github.visola.githubnotifier.data;

import com.github.visola.githubnotifier.model.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RepositoryRepository extends JpaRepository<Repository, Long> {

  @Modifying(clearAutomatically = true)
  @Transactional
  void deleteByFullName(String fullName);

  @Query("SELECT r FROM Repository r ORDER BY r.fullName")
  List<Repository> findAllOrderByFullName();

  Repository findByName(String name);

  Optional<Repository> findByFullName(String fullName);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE PullRequest SET state = 'close'")
  void markAllAsClosed();

}
