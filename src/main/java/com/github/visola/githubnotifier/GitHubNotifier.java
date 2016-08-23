package com.github.visola.githubnotifier;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import com.github.visola.githubnotifier.data.QueryRunner;
import com.github.visola.githubnotifier.service.ConfigurationService;

@SpringBootApplication
public class GitHubNotifier extends WebMvcConfigurerAdapter implements ApplicationRunner {

  public static void main(String[] args) throws BeansException {
    new SpringApplicationBuilder(GitHubNotifier.class)
      .headless(false)
      .web(WebApplicationType.NONE)
      .run(args)
      .getBean(ConfigurationService.class).load();
  }

  private ApplicationContext context;

  @Autowired
  public GitHubNotifier(ApplicationContext context) {
    this.context = context;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public TaskScheduler taskScheduler() {
    return new ThreadPoolTaskScheduler();
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    context.getBean(ConfigurationService.class).load();

    if (args.containsOption("query-runner")) {
      context.getBean(QueryRunner.class).run();
    }
  }

}
