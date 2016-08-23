package com.github.visola.githubnotifier.model;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Entity
@JsonDeserialize(using = EventDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Event {

  @Id
  private Long id;
  private Calendar createdAt;

  @Enumerated(EnumType.STRING)
  private EventType type;

  @OneToOne(mappedBy="event", cascade={CascadeType.MERGE, CascadeType.PERSIST})
  private EventPayload payload;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Calendar getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Calendar createdAt) {
    this.createdAt = createdAt;
  }

  public EventType getType() {
    return type;
  }

  public void setType(EventType type) {
    this.type = type;
  }

  public EventPayload getPayload() {
    return payload;
  }

  public void setPayload(EventPayload payload) {
    this.payload = payload;
  }

}
