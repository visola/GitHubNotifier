package com.github.visola.githubnotifier.service;

import java.util.Optional;

import com.github.visola.githubnotifier.model.Configuration;

public interface ConfigurationListener {

  void configurationChanged(Optional<Configuration> configuration);

}
