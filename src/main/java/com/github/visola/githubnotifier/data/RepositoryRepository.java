package com.github.visola.githubnotifier.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.visola.githubnotifier.model.Repository;

public interface RepositoryRepository extends PagingAndSortingRepository<Repository, String>{

}
