package com.github.visola.githubnotifier.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.github.visola.githubnotifier.model.Repository;

public class RepositoryListCellRenderer implements ListCellRenderer<Repository> {

  private static final Color SELECTED_BACKGROUND = new Color(255, 255, 200);

  private final JLabel lbValue = new JLabel();

  @Override
  public Component getListCellRendererComponent(JList<? extends Repository> list, Repository value, int index, boolean isSelected, boolean cellHasFocus) {
    lbValue.setText(value.getFullName());

    if (cellHasFocus) {
      lbValue.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
    } else {
      lbValue.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    if (isSelected) {
      lbValue.setOpaque(true);
      lbValue.setBackground(SELECTED_BACKGROUND);
    } else {
      lbValue.setOpaque(false);
    }
    return lbValue;
  }

}
