package com.github.visola.githubnotifier.data;

import com.github.visola.githubnotifier.model.PullRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {

  void deleteByBaseRepositoryFullName(String name);

  List<PullRequest> findByBaseRepositoryFullName(String name);

  List<PullRequest> findByStateOrderByUpdatedAtDesc(String state);

}
