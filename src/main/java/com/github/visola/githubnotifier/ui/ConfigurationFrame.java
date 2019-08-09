package com.github.visola.githubnotifier.ui;

import com.github.visola.githubnotifier.model.Configuration;
import com.github.visola.githubnotifier.service.ConfigurationService;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationFrame extends JFrame implements ActionListener {

  private static final long serialVersionUID = 1L;

  private static final String ACTION_SAVE = "save";

  private final ConfigurationPanel configurationPanel;
  private final JButton btSave = new JButton("Save");
  private final JButton btCancel = new JButton("Cancel");

  private final ConfigurationService configurationService;

  @Autowired
  public ConfigurationFrame(ConfigurationService configurationService) {
    setAlwaysOnTop(true);
    setLocationRelativeTo(null);

    this.configurationService = configurationService;

    JComponent container = (JComponent) getContentPane();
    container.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

    configurationPanel = new ConfigurationPanel();
    configurationPanel.setConfiguration(configurationService.load());
    add(configurationPanel, BorderLayout.CENTER);

    JPanel buttonsPane = new JPanel();
    add(buttonsPane, BorderLayout.PAGE_END);

    btSave.addActionListener(this);
    btSave.setActionCommand(ACTION_SAVE);
    buttonsPane.add(btSave);

    btCancel.addActionListener(this);
    buttonsPane.add(btCancel);

    pack();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch(e.getActionCommand()) {
      case ACTION_SAVE:
        Configuration configuration = configurationPanel.getConfiguration();
        configurationService.save(configuration);
        break;
    }

    setVisible(false);
  }

}
