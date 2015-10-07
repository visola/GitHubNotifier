package com.github.visola.githubnotifier.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationFrame extends JFrame {

  private static final long serialVersionUID = 1L;
  private static final int HEIGHT = 100;
  private static final Logger LOG = LoggerFactory.getLogger(NotificationFrame.class);
  private static final int WIDTH = 250;

  private JLabel icon;
  private JLabel title;
  private JLabel text;

  public NotificationFrame() {
    super("Git Notifier Notification");
    getContentPane().setBackground(Color.WHITE);
    setAlwaysOnTop(true);
    setUndecorated(true);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(screenSize.width - WIDTH, 100);
    setSize(WIDTH, HEIGHT);

    setLayout(new BorderLayout());

    try {
      icon = new JLabel(new ImageIcon(getScaledImage()));
      icon.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      add(icon, BorderLayout.LINE_START);
    } catch (IOException e) {
      LOG.error("Error while loading image.", e);
    }

    title = new JLabel();
    title.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    title.setHorizontalAlignment(JLabel.CENTER);
    add(title, BorderLayout.PAGE_START);

    text = new JLabel();
    text.setHorizontalAlignment(JLabel.CENTER);
    text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(text, BorderLayout.CENTER);
  }

  public void showMessage(String titleText, String message) {
    title.setText(titleText);
    text.setText(message);
    this.setVisible(true);
  }

  private Image getScaledImage() throws IOException {
    BufferedImage image = ImageIO.read(getClass().getResource("/static/img/octocat.png"));
    int h = HEIGHT / 2;
    int w = h * image.getWidth() / image.getHeight();

    BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2 = resizedImage.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(image, 0, 0, w, h, null);
    g2.dispose();

    return resizedImage;
  }

}
