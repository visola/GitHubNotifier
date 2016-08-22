package com.github.visola.githubnotifier.model;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class StringEventPayload extends EventPayload{

  private static final long serialVersionUID = 1L;

  @Lob
  private String payload;

  public StringEventPayload() {
  }

  public StringEventPayload(Event event, String payload) {
    super(event);
    this.payload = payload;
  }

  public String getPayload() {
    return payload;
  }
 
  public void setPayload(String payload) {
    this.payload = payload;
  }

}
