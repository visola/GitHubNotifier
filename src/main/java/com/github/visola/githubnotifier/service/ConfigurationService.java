package com.github.visola.githubnotifier.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.github.visola.githubnotifier.data.ConfigurationRepository;
import com.github.visola.githubnotifier.model.Configuration;

@Lazy
@Service
public class ConfigurationService {

  private final ConfigurationRepository configurationRepository;
  private final List<ConfigurationListener> configurationListeners = new ArrayList<>();

  @Autowired
  public ConfigurationService(ConfigurationRepository configurationRepository) {
    this.configurationRepository = configurationRepository;
  }

  public void addConfigurationListener(ConfigurationListener l) {
    if (!configurationListeners.contains(l)) {
      this.configurationListeners.add(l);
    }
  }

  public void removeConfigurationListener(ConfigurationListener l) {
    this.configurationListeners.remove(l);
  }

  public Optional<Configuration> load() {
    Optional<Configuration> configuration = Optional.empty();
    for (Iterator<Configuration> it = configurationRepository.findAll().iterator(); it.hasNext();) {
      configuration = Optional.of(it.next());
      break;
    }

    return configuration;
  }

  public void save(Configuration configuration) {
    configurationRepository.save(configuration);
    configurationListeners.forEach(l -> l.configurationChanged(Optional.of(configuration)));
  }

}
