package com.github.visola.githubnotifier.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.visola.githubnotifier.model.Configuration;

public interface ConfigurationRepository extends PagingAndSortingRepository<Configuration, String> {

}
