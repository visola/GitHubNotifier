package com.github.visola.githubnotifier.ui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.service.ConfigurationService;

@Component
public class SystemTrayManager {

  private final ConfigurationMenuManager configurationMenuManager;
  private final ConfigurationService configurationService;
  private final PullRequestMenuManager pullRequestMenuManager;
  private final RepositoryMenuManager repositoryMenuManager;
  private final VelocityEngine velocityEngine;

  private PopupMenu popupMenu;
  private TrayIcon trayIcon;
  private List<NotificationFrameHolder> notificationFrames = new ArrayList<>();

  @Autowired
  public SystemTrayManager(ConfigurationMenuManager configurationMenuManager,
                         ConfigurationService configurationService,
                         PullRequestMenuManager pullRequestMenuManager,
                         RepositoryMenuManager repositoryMenuManager,
                         VelocityEngine velocityEngine) throws IOException, AWTException {
    this.configurationMenuManager = configurationMenuManager;
    this.configurationService = configurationService;
    this.pullRequestMenuManager = pullRequestMenuManager;
    this.repositoryMenuManager = repositoryMenuManager;
    this.velocityEngine = velocityEngine;

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

    popupMenu.add(configurationMenuManager.getMenuItem());

    Optional<Configuration> configuration = configurationService.getConfiguration();
    if (configuration.isPresent()) {
      popupMenu.add(repositoryMenuManager.getMenu());
    }
  }

  public void showNotification(String title, String message, String link) {
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("title", title);
    model.put("message", message);
    String html = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "panel.vm", "UTF-8", model);

    NotificationFrameHolder holder = new NotificationFrameHolder(html, link);
    holder.notificationFrame.setVisible(true);
    notificationFrames.add(holder);

    repositionFrames();
  }

  @Scheduled(fixedDelay=5 * 1000)
  public void cleanNotifications() {
    int removed = 0;
    for (Iterator<NotificationFrameHolder> iterator = notificationFrames.iterator(); iterator.hasNext();) {
      NotificationFrameHolder holder = iterator.next();
      if (System.currentTimeMillis() - holder.created > 5000) {
        iterator.remove();
        holder.notificationFrame.dispose();
        removed++;
      }
    }
    if (removed > 0) {
      repositionFrames();
    }
  }

  private void repositionFrames() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int marginRight = 20;
    int verticalSpace = 10;
    int lastEndPosition = 100;
    for (int i = 0; i < notificationFrames.size(); i++) {
      NotificationFrameHolder holder = notificationFrames.get(i);
      int top = lastEndPosition + verticalSpace;
      holder.notificationFrame.setLocation(screenSize.width - holder.notificationFrame.getWidth() - marginRight, top);
      lastEndPosition = top + holder.notificationFrame.getHeight();
    }
  }

  private void updatePullRequests() {
    pullRequestMenuManager.getPullRequestMenuItems().forEach(mi -> popupMenu.add(mi));
  }

  private class NotificationFrameHolder {
    final NotificationFrame notificationFrame;
    final long created = System.currentTimeMillis();
    public NotificationFrameHolder(String message, String link) {
      this.notificationFrame = new NotificationFrame(message, link);
    }
  }

}
