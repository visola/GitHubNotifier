package com.github.visola.githubnotifier;

import org.springframework.beans.BeansException;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import com.github.visola.githubnotifier.schedule.EventPuller;
import com.github.visola.githubnotifier.ui.SystemTrayManager;

@SpringBootApplication
public class GitHubNotifier {

  public static void main(String[] args) throws BeansException {
    new SpringApplicationBuilder(GitHubNotifier.class)
      .headless(false)
      .web(WebApplicationType.NONE)
      .run(args)
      .getBean(EventPuller.class);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public TaskScheduler taskScheduler() {
    return new ThreadPoolTaskScheduler();
  }

}
