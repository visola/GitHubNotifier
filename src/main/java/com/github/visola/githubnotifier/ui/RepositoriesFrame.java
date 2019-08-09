package com.github.visola.githubnotifier.ui;

import com.github.visola.githubnotifier.model.Repository;
import com.github.visola.githubnotifier.service.RepositoryService;
import com.google.common.base.Strings;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoriesFrame extends JFrame implements ActionListener {

  private static final long serialVersionUID = 1L;

  private static final String ACTION_ADD = "add repo";
  private static final String ACTION_REMOVE = "remove repos";

  private final RepositoryService repositoryService;

  private final JList<Repository> repositoriesList;
  private final DefaultListModel<Repository> repositoriesModel;

  private final JButton btAddRepository = new JButton("Add");
  private final JButton btRemoveRepositories = new JButton("Remove");

  @Autowired
  public RepositoriesFrame(RepositoryService repositoryService) {
    super("Add/Remove Repositories");
    setAlwaysOnTop(true);

    setSize(400, 300);
    setLocationRelativeTo(null);

    ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    this.repositoryService = repositoryService;

    repositoriesModel = new DefaultListModel<>();
    repositoriesList = new JList<>(repositoriesModel);
    repositoriesList.setCellRenderer(new RepositoryListCellRenderer());

    add(new JScrollPane(repositoriesList), BorderLayout.CENTER);

    JPanel btPanel = new JPanel();

    btAddRepository.setActionCommand(ACTION_ADD);
    btAddRepository.addActionListener(this);
    btPanel.add(btAddRepository);

    btRemoveRepositories.setActionCommand(ACTION_REMOVE);
    btRemoveRepositories.addActionListener(this);
    btPanel.add(btRemoveRepositories);

    add(btPanel, BorderLayout.PAGE_END);

    refreshData();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case ACTION_ADD:
        String fullName = JOptionPane.showInputDialog(this, "What's the full path for the repo? (e.g.: torvalds/linux)");
        if (!Strings.isNullOrEmpty(fullName)) {
          Optional<Repository> loaded = repositoryService.findByFullName(fullName);
          if (!loaded.isPresent()) {
            JOptionPane.showMessageDialog(this, "Repository '" + fullName + "' wasn't found. Please check for typos.", "Repository Not Found", JOptionPane.ERROR_MESSAGE);
            break;
          }

          repositoryService.save(loaded.get());
          refreshData();
        }
        break;
      case ACTION_REMOVE:
        int [] selectedIndices = repositoriesList.getSelectedIndices();
        if (selectedIndices.length == 0) {
          JOptionPane.showMessageDialog(this, "Select a repository to remove from the list.", "Selection Empty", JOptionPane.INFORMATION_MESSAGE);
          break;
        }

        for (int index : selectedIndices) {
          repositoryService.deleteByName(repositoriesModel.getElementAt(index).getName());
        }
        refreshData();
        break;
    }
  }

  private void refreshData() {
    repositoriesModel.clear();
    for (Repository repo : repositoryService.findAllOrderByFullName()) {
      repositoriesModel.addElement(repo);
    }
  }

}
