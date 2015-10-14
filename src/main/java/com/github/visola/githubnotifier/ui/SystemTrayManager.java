package com.github.visola.githubnotifier.ui;

import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.service.ConfigurationService;

@Component
public class SystemTrayManager {

  private final ConfigurationMenuManager configurationMenuManager;
  private final ConfigurationService configurationService;
  private final PullRequestMenuManager pullRequestMenuManager;
  private final RepositoryMenuManager repositoryMenuManager;
  private final NotificationFrame notificationFrame;

  private PopupMenu popupMenu;
  private TrayIcon trayIcon;

  @Autowired
  public SystemTrayManager(ConfigurationMenuManager configurationMenuManager,
                         ConfigurationService configurationService,
                         PullRequestMenuManager pullRequestMenuManager,
                         RepositoryMenuManager repositoryMenuManager,
                         NotificationFrame notificationFrame) throws IOException, AWTException {
    this.configurationMenuManager = configurationMenuManager;
    this.configurationService = configurationService;
    this.pullRequestMenuManager = pullRequestMenuManager;
    this.repositoryMenuManager = repositoryMenuManager;
    this.notificationFrame = notificationFrame;

    initialize();
  }

  private void initialize() {
    try {
      SystemTray st = SystemTray.getSystemTray();

      trayIcon = new TrayIcon(ImageIO.read(SystemTrayManager.class.getResource("/static/img/octocat.png")));
      st.add(trayIcon);
  
      popupMenu = new PopupMenu();
      trayIcon.setPopupMenu(popupMenu);

      configurationMenuManager.addConfigurationChangedListener(() -> this.initializeSubMenus());
      pullRequestMenuManager.addPullRequestUpdateListener(() -> this.updatePullRequests());

      initializeSubMenus();
    } catch (IOException | AWTException e) {
      throw new RuntimeException("Error while initializing system tray.", e);
    }
  }

  private void initializeSubMenus() {
    popupMenu.removeAll();
    Optional<Configuration> configuration = configurationService.getConfiguration();

    if (configuration.isPresent()) {
      popupMenu.add(repositoryMenuManager.getMenu());
    } else {
      popupMenu.add(configurationMenuManager.getMenuItem());
    }
  }

  public void showNotification(String title, String message) {
    notificationFrame.showMessage(title, message);
    new Timer().schedule(new TimerTask() {
      public void run() {
        notificationFrame.setVisible(false);
      }
    }, 5000);
  }

  private void updatePullRequests() {
    pullRequestMenuManager.getPullRequestMenuItems().forEach(mi -> popupMenu.add(mi));
  }

}
