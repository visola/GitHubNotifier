package com.github.visola.githubnotifier.ui;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.service.ConfigurationEvent;

@Component
public class SystemTrayManager implements ActionListener {

  private static final String ACTION_CONFIGURE = "configure";
  private static final String ACTION_REPOSITORIES = "repositories";
  private static final String ACTION_EXIT = "exit";

  private final ConfigurationFrame configurationFrame;
  private final RepositoriesFrame repositoriesFrame;

  private final PopupMenu popupMenu;
  private final TrayIcon trayIcon;

  private final MenuItem configureMenu = new MenuItem("Settings");
  private final MenuItem repositoriesMenu = new MenuItem("Repositories");
  private final MenuItem exitMenu = new MenuItem("Exit");

  private Optional<Configuration> configuration = Optional.empty();

  @Autowired
  public SystemTrayManager(ConfigurationFrame configurationFrame, RepositoriesFrame repositoriesFrame) {
    try {
      this.configurationFrame = configurationFrame;
      this.repositoriesFrame = repositoriesFrame;

      SystemTray st = SystemTray.getSystemTray();

      trayIcon = new TrayIcon(ImageIO.read(SystemTrayManager.class.getResource("/static/img/octocat.png")));
      st.add(trayIcon);

      popupMenu = new PopupMenu();
      trayIcon.setPopupMenu(popupMenu);

      configureMenuItems();
      buildMenu();
    } catch (IOException | AWTException e) {
      throw new RuntimeException("Error while initializing System Tray.", e);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case ACTION_CONFIGURE:
        configurationFrame.setVisible(true);
        break;
      case ACTION_REPOSITORIES:
        repositoriesFrame.setVisible(true);
        break;
      case ACTION_EXIT:
        System.exit(0);
        break;
    }
  }

  @EventListener
  public void configurationChangedOrLoaded(ConfigurationEvent event) {
    this.configuration = event.getConfiguration();
    buildMenu();
  }

  private void configureMenuItems() {
    configureMenu.setActionCommand(ACTION_CONFIGURE);
    configureMenu.addActionListener(this);
    repositoriesMenu.setActionCommand(ACTION_REPOSITORIES);
    repositoriesMenu.addActionListener(this);
    exitMenu.setActionCommand(ACTION_EXIT);
    exitMenu.addActionListener(this);
  }

  private void buildMenu() {
    popupMenu.removeAll();

    popupMenu.add(configureMenu);

    if (configuration.isPresent() && configuration.get().isValid()) {
      popupMenu.add(repositoriesMenu);
    }
    popupMenu.addSeparator();
    popupMenu.add(exitMenu);
  }

}
