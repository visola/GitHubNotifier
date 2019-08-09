package com.github.visola.githubnotifier.ui;

import com.github.visola.githubnotifier.event.RepositoryEvent;
import com.github.visola.githubnotifier.model.Repository;
import com.github.visola.githubnotifier.service.RepositoryService;
import java.awt.Desktop;
import java.awt.Menu;
import java.awt.MenuItem;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RepositoryMenuManager {

  private static final Logger LOG = LoggerFactory.getLogger(RepositoryMenuManager.class);

  private final MenuItem manageMenu = new MenuItem("Manage Repositories");
  private Menu repositoriesMenu;
  private final RepositoryService repositoryService;

  @Autowired
  public RepositoryMenuManager(RepositoriesFrame repositoriesFrame,
      RepositoryService repositoryService) {
    this.repositoryService = repositoryService;

    repositoriesMenu = new Menu("Repositories");

    repositoriesMenu.add(manageMenu);
    manageMenu.addActionListener((e) -> repositoriesFrame.setVisible(true));

    repositoriesMenu.addSeparator();
    updateRepositoriesMenu();
  }

  @EventListener
  public void repositoriesChanged(RepositoryEvent event) {
    updateRepositoriesMenu();
  }

  public Menu getMenu() {
    return this.repositoriesMenu;
  }

  private MenuItem createRepositoryMenuItem(Repository repository) {
    MenuItem repositoryMenu = new MenuItem(repository.getFullName());
    repositoryMenu.addActionListener((e) -> {
      try {
        Desktop.getDesktop().browse(new URI(repository.getHtmlUrl()));
      } catch (IOException | URISyntaxException ex) {
        LOG.error("Error while navigating to Repository URL", ex);
      }
    });

    return repositoryMenu;
  }

  private void updateRepositoriesMenu() {
    repositoriesMenu.removeAll();

    repositoryService.findAllOrderByFullName().stream()
        .map(this::createRepositoryMenuItem)
        .forEach(repositoriesMenu::add);
  }

}
