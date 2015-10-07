package com.github.visola.githubnotifier.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.visola.githubnotifier.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer>{

}
