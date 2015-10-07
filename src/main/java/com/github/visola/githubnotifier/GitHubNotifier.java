package com.github.visola.githubnotifier;

import java.awt.AWTException;
import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.visola.githubnotifier.ui.SystemTrayManager;

@EnableScheduling
@SpringBootApplication
public class GitHubNotifier extends WebMvcConfigurerAdapter {

  public static void main(String[] args) throws BeansException, AWTException, IOException {
    ApplicationContext context = new SpringApplicationBuilder(GitHubNotifier.class)
      .headless(false)
      .web(false)
      .run(args);

    context.getBean(SystemTrayManager.class);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
