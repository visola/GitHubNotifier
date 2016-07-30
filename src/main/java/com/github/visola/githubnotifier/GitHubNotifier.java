package com.github.visola.githubnotifier;

import java.awt.AWTException;
import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.visola.githubnotifier.schedule.EventPuller;
import com.github.visola.githubnotifier.ui.SystemTrayManager;

@SpringBootApplication
public class GitHubNotifier extends WebMvcConfigurerAdapter {

  public static void main(String[] args) throws BeansException, AWTException, IOException {
    ApplicationContext context = new SpringApplicationBuilder(GitHubNotifier.class)
      .headless(false)
      .web(false)
      .run(args);

    context.getBean(SystemTrayManager.class);
    context.getBean(EventPuller.class);
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
