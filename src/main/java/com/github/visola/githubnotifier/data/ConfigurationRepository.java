package com.github.visola.githubnotifier.data;

import com.github.visola.githubnotifier.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, String> {

}
