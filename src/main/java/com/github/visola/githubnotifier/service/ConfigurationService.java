package com.github.visola.githubnotifier.service;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.data.ConfigurationRepository;
import com.github.visola.githubnotifier.model.Configuration;

@Component
public class ConfigurationService {

  private final ConfigurationRepository configurationRepository;

  @Autowired
  public ConfigurationService(ConfigurationRepository configurationRepository) {
    this.configurationRepository = configurationRepository;
  }

  public Optional<Configuration> getConfiguration() {
    Iterable<Configuration> configurations = configurationRepository.findAll();

    Optional<Configuration> configuration = Optional.empty();
    for (Iterator<Configuration> it = configurations.iterator(); it.hasNext();) {
      configuration = Optional.of(it.next());
    }
    return configuration;
  }

}
