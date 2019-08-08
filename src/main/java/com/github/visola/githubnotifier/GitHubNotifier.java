package com.github.visola.githubnotifier;

import com.github.visola.githubnotifier.data.QueryRunner;
import com.github.visola.githubnotifier.service.ConfigurationService;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GitHubNotifier implements ApplicationRunner {

  public static void main(String[] args) throws BeansException {
    new SpringApplicationBuilder(GitHubNotifier.class)
      .headless(false)
      .web(WebApplicationType.NONE)
      .run(args)
      .getBean(ConfigurationService.class).load();
  }

  private ApplicationContext context;

  public GitHubNotifier(ApplicationContext context) {
    this.context = context;
  }

  @Override
  public void run(ApplicationArguments args) {
    context.getBean(ConfigurationService.class).load();

    if (args.containsOption("query-runner")) {
      context.getBean(QueryRunner.class).run();
    }
  }

}
