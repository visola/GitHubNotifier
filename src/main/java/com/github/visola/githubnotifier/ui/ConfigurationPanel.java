package com.github.visola.githubnotifier.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.github.visola.githubnotifier.model.Configuration;

public class ConfigurationPanel extends JPanel {

  private static final int COLUMN_COUNT = 50;

  private static final long serialVersionUID = 1L;

  private JTextField githubUrlTextField = new JTextField();
  private JPasswordField passwordTextField = new JPasswordField();
  private JTextField usernameTextField = new JTextField();

  public ConfigurationPanel() {
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    setLayout(new GridBagLayout());

    GridBagConstraints gbcGithubUrlLabel = new GridBagConstraints();
    gbcGithubUrlLabel.anchor = GridBagConstraints.EAST;
    gbcGithubUrlLabel.insets = new Insets(0, 0, 5, 5);
    gbcGithubUrlLabel.gridx = 0;
    gbcGithubUrlLabel.gridy = 0;
    JLabel githubUrlLabel = new JLabel("GitHub URL");
    add(githubUrlLabel, gbcGithubUrlLabel);

    GridBagConstraints gbcGithubUrlTextField = new GridBagConstraints();
    gbcGithubUrlTextField.fill = GridBagConstraints.BOTH;
    gbcGithubUrlTextField.insets = new Insets(0, 0, 5, 0);
    gbcGithubUrlTextField.gridx = 1;
    gbcGithubUrlTextField.gridy = 0;
    githubUrlTextField.setColumns(COLUMN_COUNT);
    add(githubUrlTextField, gbcGithubUrlTextField);

    GridBagConstraints gbcUsernameLabel = new GridBagConstraints();
    gbcUsernameLabel.anchor = GridBagConstraints.EAST;
    gbcUsernameLabel.insets = new Insets(0, 0, 5, 5);
    gbcUsernameLabel.gridx = 0;
    gbcUsernameLabel.gridy = 1;
    JLabel userNameLabel = new JLabel("Username");
    add(userNameLabel, gbcUsernameLabel);

    GridBagConstraints gbcUsernameTextField = new GridBagConstraints();
    gbcUsernameTextField.fill = GridBagConstraints.BOTH;
    gbcUsernameTextField.insets = new Insets(0, 0, 5, 0);
    gbcUsernameTextField.gridx = 1;
    gbcUsernameTextField.gridy = 1;
    usernameTextField.setColumns(COLUMN_COUNT);
    add(usernameTextField, gbcUsernameTextField);

    GridBagConstraints gbcPasswordLabel = new GridBagConstraints();
    gbcPasswordLabel.anchor = GridBagConstraints.EAST;
    gbcPasswordLabel.insets = new Insets(0, 0, 0, 5);
    gbcPasswordLabel.gridx = 0;
    gbcPasswordLabel.gridy = 2;
    JLabel passwordLabel = new JLabel("Password");
    add(passwordLabel, gbcPasswordLabel);

    GridBagConstraints gbcPasswordTextField = new GridBagConstraints();
    gbcPasswordTextField.fill = GridBagConstraints.BOTH;
    gbcPasswordTextField.gridx = 1;
    gbcPasswordTextField.gridy = 2;
    passwordTextField.setColumns(COLUMN_COUNT);
    add(passwordTextField, gbcPasswordTextField);
  }

  public Configuration getConfiguration() {
    Configuration configuration = new Configuration();
    configuration.setGithubUrl(githubUrlTextField.getText());
    configuration.setUsername(usernameTextField.getText());
    configuration.setPassword(new String(passwordTextField.getPassword()));
    return configuration;
  }

}
