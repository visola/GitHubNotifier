package com.github.visola.githubnotifier.ui;

import com.github.visola.githubnotifier.model.Configuration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ConfigurationPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private static final int COLUMN_COUNT = 50;

  private final JTextField githubUrlTextField = new JTextField();
  private final JPasswordField passwordTextField = new JPasswordField();
  private final JTextField usernameTextField = new JTextField();
  private JCheckBox tokenCheckBox = new JCheckBox();

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

    GridBagConstraints gbcTokenLabel = new GridBagConstraints();
    gbcTokenLabel.anchor = GridBagConstraints.EAST;
    gbcTokenLabel.insets = new Insets(0, 0, 0, 5);
    gbcTokenLabel.gridx = 0;
    gbcTokenLabel.gridy = 3;
    JLabel tokenLabel = new JLabel("Token");
    add(tokenLabel, gbcTokenLabel);

    GridBagConstraints gbcTokenTextField = new GridBagConstraints();
    gbcTokenTextField.fill = GridBagConstraints.BOTH;
    gbcTokenTextField.gridx = 1;
    gbcTokenTextField.gridy = 3;
    add(tokenCheckBox, gbcTokenTextField);
  }

  public Configuration getConfiguration() {
    // TODO Validate configuration here and return emtpy Optional<Configuration> if failed
    Configuration configuration = new Configuration();
    configuration.setGithubUrl(githubUrlTextField.getText());
    configuration.setUsername(usernameTextField.getText());
    configuration.setPassword(new String(passwordTextField.getPassword()));
    configuration.setToken(tokenCheckBox.getModel().isSelected());
    return configuration;
  }

  public void setConfiguration(Optional<Configuration> configuration) {
    if (configuration.isPresent()) {
      Configuration config = configuration.get();
      githubUrlTextField.setText(config.getGithubUrl());
      usernameTextField.setText(config.getUsername());
      passwordTextField.setText(config.getPassword());
      tokenCheckBox.getModel().setSelected(config.isToken());
      return;
    }

    githubUrlTextField.setText("");
    usernameTextField.setText("");
    passwordTextField.setText("");
    tokenCheckBox.getModel().setSelected(false);
  }

}
