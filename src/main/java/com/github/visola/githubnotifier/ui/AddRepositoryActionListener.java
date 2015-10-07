package com.github.visola.githubnotifier.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.visola.githubnotifier.data.RepositoryRepository;
import com.github.visola.githubnotifier.model.Repository;

@Component
public class AddRepositoryActionListener implements ActionListener {

  private final RepositoryRepository repoRepository;

  @Autowired
  public AddRepositoryActionListener(RepositoryRepository repoRepository) {
    this.repoRepository = repoRepository;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String fullName = JOptionPane.showInputDialog("What's the repo full name?");
    if (fullName != null) {
      Repository repo = new Repository();
      repo.setFullName(fullName);
      repo.setName(fullName.split("/")[1]);
      repoRepository.save(repo);
    }
  }

}
