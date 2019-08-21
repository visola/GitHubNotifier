package com.github.visola.githubnotifier.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationFrame extends JFrame {

  private static final long serialVersionUID = 1L;
  private static final int BORDER_TOP_BOTTOM = 20;
  private static final int BORDER_LEFT_RIGHT = 5;
  private static final Border BORDER = BorderFactory.createEmptyBorder(BORDER_TOP_BOTTOM, BORDER_LEFT_RIGHT, BORDER_TOP_BOTTOM, BORDER_LEFT_RIGHT);
  private static final int HEIGHT = 100;
  private static final Logger LOG = LoggerFactory.getLogger(NotificationFrame.class);

  private JLabel icon;
  private JLabel message;
  private final long createdAt;

  public NotificationFrame(String messageText, String link) {
    super("Git Notifier Notification");
    createdAt = System.currentTimeMillis();

    setLayout(new BorderLayout());
    getContentPane().setBackground(Color.WHITE);
    setAlwaysOnTop(true);
    setUndecorated(true);
    setPreferredSize(new Dimension(400, 100));

    setLayout(new BorderLayout());

    try {
      icon = new JLabel(new ImageIcon(getScaledImage()));
      icon.setPreferredSize(new Dimension(HEIGHT, HEIGHT));
      icon.setBorder(BORDER);
      icon.addMouseListener(new CloseFrameListener());
      // TODO - Make this an actual close button
      add(icon, BorderLayout.LINE_START);
    } catch (IOException e) {
      LOG.error("Error while loading image.", e);
    }

    String wrappedMessage = "<html><p style=\"width: 200px\">";
    wrappedMessage += messageText;
    wrappedMessage += "</p></html>";

    message = new JLabel(wrappedMessage);
    message.setBorder(BORDER);
    message.addMouseListener(new OpenLinkMouseListener(link));
    add(message, BorderLayout.CENTER);

    pack();
  }

  public long getCreatedAt() {
    return createdAt;
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

  private class CloseFrameListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      NotificationFrame.this.dispose();
    }
  }

  private class OpenLinkMouseListener extends MouseAdapter {
    private final String link;
    public OpenLinkMouseListener(String link) {
      this.link = link;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
      try {
        NotificationFrame.this.dispose();
        Desktop.getDesktop().browse(new URI(link));
      } catch (Exception ex) {
        LOG.error("Error while opening link: {}", link, ex);
      }
    }
  }

}
