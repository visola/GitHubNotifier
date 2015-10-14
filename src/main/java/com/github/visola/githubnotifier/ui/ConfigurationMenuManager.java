package com.github.visola.githubnotifier.ui;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.service.ConfigurationService;

@Component
public class ConfigurationMenuManager implements ActionListener {

  private final ConfigurationService configurationService;

  private MenuItem menuItem;
  private List<Runnable> configurationListeners = new ArrayList<>();

  @Autowired
  public ConfigurationMenuManager(ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    ConfigurationPanel panel = new ConfigurationPanel(configurationService.getConfiguration());
    JOptionPane.showMessageDialog(null, panel);
    configurationService.save(panel.getConfiguration());
    configurationListeners.forEach(c->c.run());
  }

  public MenuItem getMenuItem() {
    boolean hasConfiguration = configurationService.getConfiguration().isPresent();
    menuItem = new MenuItem((hasConfiguration ? "Edit" : "Add") + " Configuration");
    menuItem.addActionListener(this);
    return menuItem;
  }

  public void addConfigurationChangedListener(Runnable listener) {
    this.configurationListeners.add(listener);
  }

}
