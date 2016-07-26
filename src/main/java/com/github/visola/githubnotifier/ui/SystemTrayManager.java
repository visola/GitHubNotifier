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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.service.ConfigurationListener;
import com.github.visola.githubnotifier.service.ConfigurationService;

@Component
@Lazy
public class SystemTrayManager implements ActionListener, ConfigurationListener {

  private static final String ACTION_CONFIGURE = "configure";
  private static final String ACTION_EXIT = "exit";

  private final ConfigurationFrame configurationFrame;
  private final PopupMenu popupMenu;
  private final TrayIcon trayIcon;
  private final MenuItem configureMenu = new MenuItem("Settings");
  private final MenuItem exitMenu = new MenuItem("Exit");

  @Autowired
  public SystemTrayManager(ConfigurationService configurationService, ConfigurationFrame configurationFrame) {
    try {
      configurationService.addConfigurationListener(this);
      this.configurationFrame = configurationFrame;

      SystemTray st = SystemTray.getSystemTray();

      trayIcon = new TrayIcon(ImageIO.read(SystemTrayManager.class.getResource("/static/img/octocat.png")));
      st.add(trayIcon);

      popupMenu = new PopupMenu();
      trayIcon.setPopupMenu(popupMenu);

      buildMenu();
    } catch (IOException | AWTException e) {
      throw new RuntimeException("Error while initializing System Tray.", e);
    }
  }

  @Override
  public void configurationChanged(Optional<Configuration> configuration) {
    
  }

  public void addMenuItem(MenuItem item) {
    
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case ACTION_CONFIGURE:
        configurationFrame.setVisible(true);
        break;
      case ACTION_EXIT:
        System.exit(0);
        break;
    }
  }

  private void buildMenu() {
    configureMenu.setActionCommand(ACTION_CONFIGURE);
    configureMenu.addActionListener(this);
    popupMenu.add(configureMenu);

    popupMenu.addSeparator();

    exitMenu.setActionCommand(ACTION_EXIT);
    exitMenu.addActionListener(this);
    popupMenu.add(exitMenu);
  }

}
