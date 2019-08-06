package com.github.visola.githubnotifier;

import org.springframework.beans.BeansException;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.github.visola.githubnotifier.ui.SystemTrayManager;

@EnableScheduling
@SpringBootApplication
public class GitHubNotifier {

  public static void main(String[] args) throws BeansException {
    new SpringApplicationBuilder(GitHubNotifier.class)
      .headless(false)
      .web(WebApplicationType.NONE)
      .run(args)
      .getBean(SystemTrayManager.class)
      .initialize();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
