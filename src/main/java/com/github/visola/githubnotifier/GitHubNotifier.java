package com.github.visola.githubnotifier;

import com.github.visola.githubnotifier.service.ConfigurationService;
import org.springframework.beans.BeansException;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class GitHubNotifier {

  public static void main(String[] args) throws BeansException {
    new SpringApplicationBuilder(GitHubNotifier.class)
      .headless(false)
      .web(WebApplicationType.NONE)
      .run(args)
      .getBean(ConfigurationService.class).load();
  }

}
