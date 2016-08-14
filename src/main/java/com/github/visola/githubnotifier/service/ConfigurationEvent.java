package com.github.visola.githubnotifier.service;

import java.util.Optional;

import org.springframework.context.ApplicationEvent;

import com.github.visola.githubnotifier.model.Configuration;

public class ConfigurationEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1L;

  private final Optional<Configuration> configuration;

  public ConfigurationEvent(Object source, Optional<Configuration> configuration) {
    super(source);
    this.configuration = configuration;
  }

  public Optional<Configuration> getConfiguration() {
    return configuration;
  }

}
