package com.github.visola.githubnotifier.ui;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.data.RepositoryRepository;
import com.github.visola.githubnotifier.model.Repository;

@Component
public class RepositoryMenuManager {

  private final RepositoryRepository repoRepository;
  private Menu repositoriesMenu;
  private Menu removeRepoMenu;
  private List<MenuItem> repositoryMenus = new ArrayList<>();

  @Autowired
  public RepositoryMenuManager(AddRepositoryActionListener addRepoActionListener, RepositoryRepository repoRepository) {
    this.repoRepository = repoRepository;

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

    Set<String> repoNames = new TreeSet<>();
    for (Repository repo : repoRepository.findAll()) {
      repoNames.add(repo.getName());
    }

    repoNames.forEach(n -> {
      MenuItem repoMenu = new MenuItem(n);
      repositoriesMenu.add(repoMenu);
      repositoryMenus.add(repoMenu);

      MenuItem removeRepoMenuItem = new MenuItem(n);
      removeRepoMenuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          repoRepository.deleteByName(n);
        }
      });
      removeRepoMenu.add(removeRepoMenuItem);
    });
  }

}
