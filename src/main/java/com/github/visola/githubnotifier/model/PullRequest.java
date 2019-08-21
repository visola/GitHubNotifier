package com.github.visola.githubnotifier.model;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PullRequest {

  @Id 
  private long id;
  private int number;
  private String title;
  private String state;
  private String htmlUrl;
  private Calendar createdAt;
  private Calendar updatedAt;

  @JoinColumn(name="base_sha")
  @ManyToOne(cascade=CascadeType.ALL)
  private Commit base;

  @JoinColumn(name="head_sha")
  @ManyToOne(cascade=CascadeType.ALL)
  private Commit head;

  public PullRequest() {
  }

  public Commit getBase() {
    return base;
  }

  public Calendar getCreatedAt() {
    return createdAt;
  }

  public Commit getHead() {
    return head;
  }

  public String getHtmlUrl() {
    return htmlUrl;
  }

  public long getId() {
    return id;
  }

  public int getNumber() {
    return number;
  }

  public String getState() {
    return state;
  }

  public String getTitle() {
    return title;
  }

  public Calendar getUpdatedAt() {
    return updatedAt;
  }

  public void setBase(Commit base) {
    this.base = base;
  }

  public void setCreatedAt(Calendar createdAt) {
    this.createdAt = createdAt;
  }

  public void setHead(Commit head) {
    this.head = head;
  }

  public void setHtmlUrl(String htmlUrl) {
    this.htmlUrl = htmlUrl;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setUpdatedAt(Calendar updatedAt) {
    this.updatedAt = updatedAt;
  }

}
