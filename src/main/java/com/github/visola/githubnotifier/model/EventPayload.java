package com.github.visola.githubnotifier.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class EventPayload implements Serializable{

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  @OneToOne
  private Event event;

  public EventPayload() {
  }

  public EventPayload(Event event) {
    this.id = event.getId();
    this.event = event;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
    this.id = event.getId();
  }

}
