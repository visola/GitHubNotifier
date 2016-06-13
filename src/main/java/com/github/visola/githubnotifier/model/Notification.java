package com.github.visola.githubnotifier.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="notifications")
public class Notification {

  @Id
  private long id;
  @Column(name="last_notification")
  private Calendar lastNotification;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Calendar getLastNotification() {
    return lastNotification;
  }

  public void setLastNotification(Calendar lastNotification) {
    this.lastNotification = lastNotification;
  }

}
