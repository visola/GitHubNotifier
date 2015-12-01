package com.github.visola.githubnotifier.data;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.github.visola.githubnotifier.model.PullRequest;

public interface PullRequestRepository extends PagingAndSortingRepository<PullRequest, Integer>{

  @Modifying(clearAutomatically = true)
  @Query("UPDATE PullRequest"
      + " SET state = 'close'"
      + " WHERE id IN ("
      + "  SELECT p.id"
      + "  FROM PullRequest p"
      + "  JOIN p.base.repository r"
      + "  WHERE r.fullName = :fullName"
      + ")")
  void closePullRequests(@Param("fullName") String repositoryFullName);

  List<PullRequest> findByStateOrderByUpdatedAtDesc(String state);

  @Modifying(clearAutomatically = true)
  @Query("DELETE FROM PullRequest pr"
      + " WHERE pr.id IN ("
      + "   SELECT pr1.id"
      + "   FROM PullRequest pr1"
      + "   JOIN pr1.base.repository r"
      + "   WHERE r.name = :name)")
  void deleteByBaseRepositoryName(@Param("name") String name);

}
