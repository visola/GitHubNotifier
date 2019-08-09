package com.github.visola.githubnotifier.service;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.github.visola.githubnotifier.data.ConfigurationRepository;
import com.github.visola.githubnotifier.model.Configuration;

@Service
public class ConfigurationService {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final ConfigurationRepository configurationRepository;

  @Autowired
  public ConfigurationService(ApplicationEventPublisher applicationEventPublisher,
      ConfigurationRepository configurationRepository) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.configurationRepository = configurationRepository;
  }

  public Optional<Configuration> load() {
    Optional<Configuration> configuration = Optional.empty();
    for (Iterator<Configuration> it = configurationRepository.findAll().iterator();
        it.hasNext(); ) {
      configuration = Optional.of(it.next());
      break;
    }

    applicationEventPublisher.publishEvent(new ConfigurationEvent(this, configuration));
    return configuration;
  }

  public void save(Configuration configuration) {
    configurationRepository.save(configuration);
    applicationEventPublisher
        .publishEvent(new ConfigurationEvent(this, Optional.of(configuration)));
  }

}
