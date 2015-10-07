package com.github.visola.githubnotifier.ui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.github.visola.githubnotifier.model.Configuration;

public class ConfigurationPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private JTextField githubUrlTextField = new JTextField();
  private JPasswordField passwordTextField = new JPasswordField();
  private JTextField usernameTextField = new JTextField();

  public ConfigurationPanel() {
    setLayout(new GridLayout(3, 2));
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    add(new JLabel("GitHub URL"));
    add(githubUrlTextField);

    add(new JLabel("Username"));
    add(usernameTextField);

    add(new JLabel("Password"));
    add(passwordTextField);
  }

  public Configuration getConfiguration() {
    Configuration configuration = new Configuration();
    configuration.setGithubUrl(githubUrlTextField.getText());
    configuration.setUsername(usernameTextField.getText());
    configuration.setPassword(new String(passwordTextField.getPassword()));
    return configuration;
  }

}
