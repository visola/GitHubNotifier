package com.github.visola.githubnotifier.ui;

import java.awt.Desktop;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.Repository;
import com.github.visola.githubnotifier.service.RepositoryService;

@Component
public class RepositoryMenuManager {

  private static final Logger LOG = LoggerFactory.getLogger(RepositoryMenuManager.class);

  private final PullRequestMenuManager prMenuManager;
  private final RepositoryService repoService;
  private Menu repositoriesMenu;
  private Menu removeRepoMenu;
  private List<MenuItem> repositoryMenus = new ArrayList<>();

  @Autowired
  public RepositoryMenuManager(AddRepositoryActionListener addRepoActionListener,
                               PullRequestMenuManager prMenuManager,
                               RepositoryService repoService) {
    this.prMenuManager = prMenuManager;
    this.repoService = repoService;

    repositoriesMenu = new Menu("Repositories");

    MenuItem addRepo = new MenuItem("Add Repository");
    addRepo.addActionListener(addRepoActionListener);
    addRepo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateRepositoriesMenu();
      }
    });

    repositoriesMenu.add(addRepo);

    removeRepoMenu = new Menu("Remove Repository");
    repositoriesMenu.add(removeRepoMenu);

    repositoriesMenu.addSeparator();
    updateRepositoriesMenu();
  }

  public Menu getMenu() {
    return this.repositoriesMenu;
  }

  private void updateRepositoriesMenu() {
    removeRepoMenu.removeAll();

    for (MenuItem repoMenu : repositoryMenus) {
      repositoriesMenu.remove(repoMenu);
    }

    Set<String> repoNames = new HashSet<>();
    for (Repository repo : repoService.findAllOrderByFullName()) {
      if (repoNames.contains(repo.getName())) {
        continue;
      }

      repoNames.add(repo.getName());

      MenuItem repoMenu = new MenuItem(repo.getFullName());
      repositoriesMenu.add(repoMenu);
      repositoryMenus.add(repoMenu);

      repoMenu.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try {
            Desktop.getDesktop().browse(new URI(repo.getHtmlUrl()));
          } catch (IOException | URISyntaxException ex) {
            LOG.error("Error while navigating to Repository URL", ex);
          }
        }
      });

      MenuItem removeRepoMenuItem = new MenuItem(repo.getFullName());
      removeRepoMenuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          repoService.deleteByName(repo.getName());
          prMenuManager.updatePullRequests();
          updateRepositoriesMenu();
        }
      });
      removeRepoMenu.add(removeRepoMenuItem);
    }
  }

}
