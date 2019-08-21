package com.github.visola.githubnotifier.ui;

import com.github.visola.githubnotifier.event.PullRequestsEvent;
import com.github.visola.githubnotifier.model.PullRequest;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationManager {

  private static final int RIGHT_PAD = 20;
  private static final int TOP_PAD = 20;

  private final List<NotificationFrame> frames = new ArrayList<>();

  @EventListener
  public void pullRequestChanged(PullRequestsEvent event) {
    if (event.getPullRequests().isEmpty()) {
      return;
    }

    for (PullRequest pullRequest : event.getPullRequests()) {
      String message = "Pull request changed: " + pullRequest.getTitle();
      NotificationFrame frame = new NotificationFrame(message, pullRequest.getHtmlUrl());
      frame.setVisible(true);
      frame.addWindowListener(new CloseNotificationHandler());
      frames.add(frame);
    }

    positionFrames();
  }

  private void positionFrames() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = (int) screenSize.getWidth();
    int counter = 0;
    for (NotificationFrame frame : frames) {
      frame.setLocation(
          screenWidth - frame.getWidth() - RIGHT_PAD,
          counter * (frame.getHeight() + TOP_PAD) + TOP_PAD
      );
      counter++;
    }
  }

  private class CloseNotificationHandler extends WindowAdapter {
    @Override
    public void windowClosed(WindowEvent e) {
      NotificationManager.this.frames.remove(e.getWindow());
      NotificationManager.this.positionFrames();
    }
  }

}
