package com.github.visola.githubnotifier.ui;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.data.ConfigurationRepository;

@Component
public class ConfigurationMenuManager implements ActionListener {

  private final ConfigurationRepository configurationRepository;

  private MenuItem menuItem;
  private List<Runnable> configurationListeners = new ArrayList<>();

  @Autowired
  public ConfigurationMenuManager(ConfigurationRepository configurationRepository) {
    this.configurationRepository = configurationRepository;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    ConfigurationPanel panel = new ConfigurationPanel();
    JOptionPane.showMessageDialog(null, panel);
    configurationRepository.save(panel.getConfiguration());
    configurationListeners.forEach(c->c.run());
  }

  public MenuItem getMenuItem() {
    menuItem = new MenuItem("Add Configuration");
    menuItem.addActionListener(this);
    return menuItem;
  }

  public void addConfigurationChangedListener(Runnable listener) {
    this.configurationListeners.add(listener);
  }

}
